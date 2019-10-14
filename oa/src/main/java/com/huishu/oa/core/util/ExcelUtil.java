package com.huishu.oa.core.util;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

public class ExcelUtil {
    public static void exportExcel(String name, JSONArray ja, Map<String, String> mapColumns, String tbName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(name);
        XSSFRow row = sheet.createRow(0);
        int index = 0;
        if (tbName.equals("")) {
            tbName = "excel";
        }
        File file = new File("C:\\" + tbName);
        if (!file.exists()) {
            file.mkdir();
        }
        JSONObject first = ja.getJSONObject(0);
        Iterator<String> iterator = first.keys(); // 得到第一项的key集合
        while (iterator.hasNext()) { // 遍历key集合
            String key = (String) iterator.next(); // 得到key
            String value = first.getString(key);
            XSSFCell cell = row.createCell(index);

            //cell.setCellValue(key);
            System.out.println("mapColumns=" + mapColumns);
            for (Map.Entry<String, String> str : mapColumns.entrySet()) {
                System.out.println("str=" + str);
                if (key.equals(str.getKey())) {
                    //resultKey = str.getValue();
                    cell.setCellValue(str.getValue());
                    System.out.println("values=" + str.getValue());
                }
            }

            System.out.println("key1111=" + key);
            index++;
        }
        for (int i = 0; i < ja.size(); i++) {
            row = sheet.createRow(i + 1);
            JSONObject jaa = ja.getJSONObject(i);
            Iterator<String> iterator1 = jaa.keys();
            int index1 = 0;
            while (iterator1.hasNext()) { // 遍历key集合
                String key1 = (String) iterator1.next(); // 得到key
                String value = jaa.getString(key1);
                System.out.println(value);
                XSSFCell cell = row.createCell(index1);
                cell.setCellValue(value);
                index1++;
            }
        }
        try {
            String tmpPath = "C:\\" + tbName + "\\" + name + ".xlsx";
            OutputStream outputStream = new FileOutputStream(tmpPath);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
