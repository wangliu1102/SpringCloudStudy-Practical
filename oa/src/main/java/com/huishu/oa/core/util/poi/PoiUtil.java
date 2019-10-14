package com.huishu.oa.core.util.poi;

import com.huishu.oa.core.util.DateUtil;
import com.huishu.oa.core.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SXSSFWorkbook导出工Excel具类（针对大数据量的情况）
 * <p>
 * Created by zx
 * Date 2018/11/14 14:16
 * version:1.0
 */
@Slf4j
public class PoiUtil {

    /**
     * 初始化EXCEL(sheet个数和标题)
     *
     * @param totalRowCount 总记录数
     * @param titles        标题集合
     * @return XSSFWorkbook对象
     */
    public static SXSSFWorkbook initExcel(Integer totalRowCount, String[] titles) {

        // 在内存当中保持 100 行 , 超过的数据放到硬盘中在内存当中保持 100 行 , 超过的数据放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);

        Integer sheetCount = ((totalRowCount % ExcelConstant.PER_SHEET_ROW_COUNT == 0) ?
                (totalRowCount / ExcelConstant.PER_SHEET_ROW_COUNT) : (totalRowCount / ExcelConstant.PER_SHEET_ROW_COUNT + 1));

        // 根据总记录数创建sheet并分配标题
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet sheet = wb.createSheet("sheet" + (i + 1));
            SXSSFRow headRow = (SXSSFRow) sheet.createRow(0);
            sheet.trackAllColumnsForAutoSizing();

            for (int j = 0; j < titles.length; j++) {
                SXSSFCell headRowCell = (SXSSFCell) headRow.createCell(j);
                headRowCell.setCellValue(titles[j]);
                headRowCell.setCellStyle(getHeadStyle(wb));
                //  让列宽随着导出的列长自动适应
                sheet.autoSizeColumn((short) j); //调整列宽度
            }
        }

        return wb;
    }

    /**
     * 下载EXCEL到本地指定的文件夹
     *
     * @param wb         EXCEL对象SXSSFWorkbook
     * @param exportPath 导出路径
     */
    public static void downLoadExcelToLocalPath(SXSSFWorkbook wb, String exportPath) {
        FileOutputStream fops = null;
        try {
            fops = new FileOutputStream(exportPath);
            wb.write(fops);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != fops) {
                try {
                    fops.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 下载EXCEL到浏览器
     *
     * @param wb       EXCEL对象XSSFWorkbook
     * @param response
     * @param fileName 文件名称
     * @throws IOException
     */
    public static void downLoadExcelToWebsite(SXSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {

        response.setHeader("Content-disposition", "attachment; filename="
                + new String((fileName + ".xlsx").getBytes("utf-8"), "ISO8859-1"));//设置下载的文件名

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            request.getSession().removeAttribute("exportFlag");
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 导出Excel到本地指定路径
     *
     * @param totalRowCount           总记录数
     * @param titles                  标题
     * @param exportPath              导出路径
     * @param writeExcelDataDelegated 向EXCEL写数据/处理格式的委托类 自行实现
     * @throws Exception
     */
    public static final void exportExcelToLocalPath(Integer start, Integer totalRowCount, String[] titles, String exportPath, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {
        log.info("开始导出：" + DateUtil.getTime(new Date()));
        // 初始化EXCEL
        SXSSFWorkbook wb = PoiUtil.initExcel(totalRowCount, titles);
        Boolean isDownLoadExcel = false;
        // 调用委托类分批写数据
        int sheetCount = wb.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet eachSheet = wb.getSheetAt(i);
            for (int j = 1; j <= start; j++) {
                int currentPage = i * ExcelConstant.PER_SHEET_WRITE_COUNT + j;
                int pageSize = ExcelConstant.PER_WRITE_ROW_COUNT;
                int startRowCount = (j - 1) * ExcelConstant.PER_WRITE_ROW_COUNT + 1;
                int endRowCount = startRowCount + pageSize - 1;
                isDownLoadExcel = writeExcelDataDelegated.writeExcelData(eachSheet, startRowCount, endRowCount, currentPage, pageSize);
            }
        }
        if (isDownLoadExcel) {
            // 下载EXCEL
            PoiUtil.downLoadExcelToLocalPath(wb, exportPath);
            log.info("导出完成：" + DateUtil.getTime(new Date()));
        } else {
            log.info("无数据，不导出：" + DateUtil.getTime(new Date()));
        }

    }


    /**
     * 导出Excel到浏览器
     *
     * @param response
     * @param totalRowCount           总记录数
     * @param fileName                文件名称
     * @param titles                  标题
     * @param writeExcelDataDelegated 向EXCEL写数据/处理格式的委托类 自行实现
     * @throws Exception
     */
    public static final void exportExcelToWebsite(Integer start, HttpServletRequest request, HttpServletResponse response, Integer totalRowCount, String fileName, String[] titles, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {

        log.info("开始导出：" + DateUtil.getTime(new Date()));

        // 初始化EXCEL
        SXSSFWorkbook wb = PoiUtil.initExcel(totalRowCount, titles);
        Boolean isDownLoadExcel = false;

        // 调用委托类分批写数据
        int sheetCount = wb.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet eachSheet = wb.getSheetAt(i);

            for (int j = 1; j <= start; j++) {

                int currentPage = i * ExcelConstant.PER_SHEET_WRITE_COUNT + j;
                int pageSize = ExcelConstant.PER_WRITE_ROW_COUNT;
                int startRowCount = (j - 1) * ExcelConstant.PER_WRITE_ROW_COUNT + 1;
                int endRowCount = startRowCount + pageSize - 1;

                isDownLoadExcel = writeExcelDataDelegated.writeExcelData(eachSheet, startRowCount, endRowCount, currentPage, pageSize);

            }
        }

        if (isDownLoadExcel) {
            // 下载EXCEL
            PoiUtil.downLoadExcelToWebsite(wb, request, response, fileName);
            log.info("导出完成：" + DateUtil.getTime(new Date()));
        } else {
            log.info("无数据，不导出：" + DateUtil.getTime(new Date()));
        }

    }


    // 设置表头的单元格样式
    public static CellStyle getHeadStyle(SXSSFWorkbook workbook) {
        // 创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();

        // 设置单元格的背景颜色为淡蓝色
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        // 设置填充字体的样式
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // cellStyle.setShrinkToFit(true);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // 设置单元格字体样式
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 这是字体加粗
        font.setFontName("宋体");// 设置字体的样式
        font.setFontHeight(12);// 设置字体的大小
        cellStyle.setFont(font);// 将字体填充到表格中去

        // 设置单元格边框为细线条（上下左右）
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

        return cellStyle;

    }

    // 设置表体的单元格样式
    public static CellStyle getBodyStyle(SXSSFWorkbook workbook) {
        // 创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容不显示自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = (XSSFFont) workbook.createFont();
        //   font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 这是字体加粗
        font.setFontName("宋体");// 设置字体
        font.setFontHeight(10);// 设置字体的大小
        cellStyle.setFont(font);// 将字体添加到表格中去

        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

        return cellStyle;

    }


    /**
     * @Description 将数据写入Excel工作簿Workbook
     * @auther 王柳
     * @date 2019/6/4 15:29
     * @params [datas, result, eachSheet, startRowCount, endRowCount]
     */
    public static Boolean writeWorkbook(String[] datas, List<Map<String, Object>> result, SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount) {
        if (!CollectionUtils.isEmpty(result)) {

            for (int i = startRowCount; i <= endRowCount; i++) {
                SXSSFRow eachDataRow = eachSheet.createRow(i);
                // 调整列宽以适合内容,配合sheet.autoSizeColumn方法使用
//                eachSheet.trackAllColumnsForAutoSizing();
                if ((i - startRowCount) < result.size()) {
                    Map<String, Object> map = result.get(i - startRowCount);

                    for (int j = 0; j < datas.length; j++) {
                        SXSSFCell cell = eachDataRow.createCell(j);

                        // 设置单元格样式会严重影响性能
//                        cell.setCellStyle(getBodyStyle(eachSheet.getWorkbook()));
                        String value = objectToString(map.get(datas[j]));
                        if (ToolUtil.isEmpty(value)) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(value);
                        }
//                        eachSheet.autoSizeColumn((short) j); //调整列宽度
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Description 将Object转换为String
     * @auther 王柳
     * @date 2019/6/5 8:27
     * @params [o]
     */
    public static String objectToString(Object o) {
        if (o == null) {
            return "";
        } else if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Integer) {
            return String.valueOf((Integer) o);
        } else if (o instanceof Long) {
            return String.valueOf((Long) o);
        } else if (o instanceof Double) {
            return String.valueOf((Double) o);
        } else if (o instanceof Float) {
            return String.valueOf((Float) o);
        } else if (o instanceof Boolean) {
            return String.valueOf((Boolean) o);
        } else if (o instanceof Date) {
            return DateUtil.getTime((Date) o);
        } else {
            return "";
        }
    }

    /**
     * @Description 获取查询次数
     * @auther 王柳
     * @date 2019/6/4 15:03
     * @params [totalRowCount]
     */
    public static Integer getStart(Integer totalRowCount) {
        Integer start = 0;
        if (totalRowCount <= ExcelConstant.PER_WRITE_ROW_COUNT) { // 数据小于20万 查一次
            start = 1;
        } else if (totalRowCount > ExcelConstant.PER_WRITE_ROW_COUNT && totalRowCount < ExcelConstant.PER_SHEET_ROW_COUNT) {  // 数据大于20万小于100万 查2-4次
            start = ((totalRowCount % ExcelConstant.PER_WRITE_ROW_COUNT == 0) ?
                    (totalRowCount / ExcelConstant.PER_WRITE_ROW_COUNT) : (totalRowCount / ExcelConstant.PER_WRITE_ROW_COUNT + 1));
        } else { // 数据大于等于100万 查5次
            start = ExcelConstant.PER_SHEET_WRITE_COUNT;
        }

        return start;
    }


    /**
     * 导出Excel到浏览器
     *
     * @param response
     * @param totalRowCount           总记录数
     * @param fileName                文件名称
     * @param titles                  标题
     * @param writeExcelDataDelegated 向EXCEL写数据/处理格式的委托类 自行实现
     * @throws Exception
     * @auther 王柳
     */
    public static final void exportExcelToWebsite(Integer start, HttpServletRequest request, HttpServletResponse response, Integer totalRowCount, String fileName, String[] titles, String exportFlag, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {

        log.info("开始导出：" + DateUtil.getTime(new Date()));

        // 初始化EXCEL
        SXSSFWorkbook wb = PoiUtil.initExcel(totalRowCount, titles);

        Boolean isDownLoadExcel = isDownLoadExcel(start, totalRowCount, wb, writeExcelDataDelegated);

        if (isDownLoadExcel) {
            // 下载EXCEL
            PoiUtil.downLoadExcelToWebsite(wb, request, response, fileName, exportFlag);
            log.info("导出完成：" + DateUtil.getTime(new Date()));
        } else {
            log.info("无数据，不导出：" + DateUtil.getTime(new Date()));
        }

    }

    /**
     * @Description 是否可以下载excel
     * @auther 王柳
     * @date 2019/6/4 19:51
     * @params [start, totalRowCount, wb, writeExcelDataDelegated]
     */
    public static Boolean isDownLoadExcel(Integer start, Integer totalRowCount, SXSSFWorkbook wb, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {

        Boolean isDownLoadExcel = false;

        // 调用委托类分批写数据
        int sheetCount = wb.getNumberOfSheets();

        int maxCurrentPage = ((totalRowCount % ExcelConstant.PER_WRITE_ROW_COUNT == 0) ?
                (totalRowCount / ExcelConstant.PER_WRITE_ROW_COUNT) : (totalRowCount / ExcelConstant.PER_WRITE_ROW_COUNT + 1));
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet eachSheet = wb.getSheetAt(i);

            for (int j = 1; j <= start; j++) {

                int currentPage = i * ExcelConstant.PER_SHEET_WRITE_COUNT + j;
                if (currentPage > maxCurrentPage) {
                    break;
                }
                int pageSize = ExcelConstant.PER_WRITE_ROW_COUNT;
                int startRowCount = (j - 1) * ExcelConstant.PER_WRITE_ROW_COUNT + 1;
                int endRowCount = startRowCount + pageSize - 1;


                isDownLoadExcel = writeExcelDataDelegated.writeExcelData(eachSheet, startRowCount, endRowCount, currentPage, pageSize);

            }
        }
        return isDownLoadExcel;
    }


    /**
     * 下载EXCEL到浏览器
     *
     * @param wb       EXCEL对象SXSSFWorkbook
     * @param response
     * @param fileName 文件名称
     * @throws IOException
     * @auther 王柳
     */
    public static void downLoadExcelToWebsite(SXSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response, String fileName, String exportFlag) throws IOException {

        response.setHeader("Content-disposition", "attachment; filename="
                + new String((fileName + ".xlsx").getBytes("utf-8"), "ISO8859-1"));//设置下载的文件名

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            request.getSession().removeAttribute(exportFlag);
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
