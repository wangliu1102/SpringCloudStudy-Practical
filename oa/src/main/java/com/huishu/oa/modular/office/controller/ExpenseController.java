package com.huishu.oa.modular.office.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.huishu.oa.config.properties.OaProperties;
import com.huishu.oa.core.common.GlobalData;
import com.huishu.oa.core.common.annotation.BusinessLog;
import com.huishu.oa.core.common.annotation.Permission;
import com.huishu.oa.core.common.constant.Const;
import com.huishu.oa.core.common.constant.dictmap.DeleteDict;
import com.huishu.oa.core.common.constant.dictmap.ExpenseMap;
import com.huishu.oa.core.common.exception.BizExceptionEnum;
import com.huishu.oa.core.log.LogObjectHolder;
import com.huishu.oa.core.shiro.ShiroKit;
import com.huishu.oa.core.util.DateUtil;
import com.huishu.oa.core.util.FileUploadUtil;
import com.huishu.oa.modular.office.model.Expense;
import com.huishu.oa.modular.office.service.IExpenseService;
import com.huishu.oa.modular.office.wrapper.ExpenseWrapper;
import com.huishu.oa.modular.system.model.User;
import com.huishu.oa.modular.system.wrapper.UserWrapper;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 报销控制器
 *
 * @author zx
 * @Date 2019-05-09 23:02:21
 */
@Log4j
@Controller
@RequestMapping("/expense")
public class ExpenseController extends BaseController {

    private String PREFIX = "/office/expense/";

    @Autowired
    private IExpenseService expenseService;

    @Autowired
    private OaProperties oaProperties;

    /**
     * 跳转到报销列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "expense.html";
    }

    /**
     * 跳转到添加报销
     */
    @RequestMapping("/expense_add")
    public String expenseAdd() {
        return PREFIX + "expense_add.html";
    }

    /**
     * 跳转到修改报销
     */
    @RequestMapping("/expense_update/{expenseId}")
    public String expenseUpdate(@PathVariable Long expenseId, Model model) {
        Expense expense = this.expenseService.selectById(expenseId);
        model.addAttribute("expense", expense);
        LogObjectHolder.me().set(expense);
        return PREFIX + "expense_edit.html";
    }

    /**
     * 获取报销列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Expense expense) {
        List<Map<String, Object>> leave = expenseService.selectExpense(expense);
        return new ExpenseWrapper(leave).wrap();
    }

    /**
     * 新增报销
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BusinessLog(value = "新增报销", key = "title", dict = ExpenseMap.class)
    public Object add(@RequestParam(value = "file", required = false) MultipartFile file,
                      @RequestParam("title") String title, @RequestParam("money") String money, @RequestParam("comment") String comment) {
        try {

            if (ToolUtil.isEmpty(file)) {
                throw new ServiceException(BizExceptionEnum.FILE_NOT_FOUND);
            }
            String attachmentName = ShiroKit.getUser().getName() + DateUtil.getDays() + new Random().nextInt(99) + "." + ToolUtil.getFileSuffix(file.getOriginalFilename());
            //上传文件
            boolean fileUpload = FileUploadUtil.fileUpload(file, attachmentName, Const.EXPENSE_FILE);

            Expense expense = new Expense();
            expense.setTitle(title);
            expense.setMoney(Double.parseDouble(money));
            expense.setComment(comment);
            //文件是否上传成功
            if (fileUpload) {
                expense.setAttachment(attachmentName);
            } else {
                throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
            }
            expense.setCreateBy(ShiroKit.getUser().getId() + "");
            expense.setCreateTime(new Date());
            expense.setUpdateBy(ShiroKit.getUser().getId() + "");
            expense.setUpdateTime(new Date());
            this.expenseService.insert(expense);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加报销异常--" + e.getMessage());
        }
        return SUCCESS_TIP;
    }

    /**
     * 删除报销
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BusinessLog(value = "删除报销", key = "expenseId", dict = DeleteDict.class)
    public Object delete(@RequestParam Integer expenseId) {
        Expense expense = expenseService.selectById(expenseId);
        if (ToolUtil.isEmpty(expense)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //文件所在路径
        String filePath = oaProperties.getFileUploadPath() + Const.EXPENSE_FILE + expense.getAttachment();
        FileUploadUtil.deleteFile(filePath);
        expenseService.deleteById(expenseId);
        return SUCCESS_TIP;
    }

    /**
     * 下载附件
     */
    @RequestMapping(value = "/downloadFile")
    @ResponseBody
    @BusinessLog(value = "下载附件", key = "expenseId")
    public Object downloadFile(@RequestParam Long expenseId) {
        Expense expense = expenseService.selectById(expenseId);
        String config = GlobalData.CONFIGPARAMETERS.get("expenseFile");
        String existFilePath = oaProperties.getFileUploadPath() + (config == null ? Const.EXPENSE_FILE : config) + expense.getAttachment();
        File file = new File(existFilePath);
        if (file.exists()) {
            return oaProperties.getFileUploadMapping() + (config == null ? Const.EXPENSE_FILE : config) + expense.getAttachment();
        } else {
            throw new ServiceException(BizExceptionEnum.FILE_NOT_FOUND);
        }
    }

    /**
     * 修改报销
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BusinessLog(value = "修改报销", key = "title", dict = ExpenseMap.class)
    public Object update(@RequestParam(value = "file", required = false) MultipartFile file, Expense expense) {
        try {
            if (ToolUtil.isAllEmpty(expense, expense.getId(), expense.getTitle(), expense.getMoney())) {
                throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
            }
            Expense old = this.expenseService.selectById(expense.getId());
            old.setTitle(expense.getTitle());
            old.setMoney(expense.getMoney());
            old.setComment(expense.getComment());
            old.setUpdateBy(ShiroKit.getUser().getId().toString());
            old.setUpdateTime(new Date());
            //判读附件是否存在
            if (ToolUtil.isNotEmpty(file)) {
                //删除原文件
                String filePath = oaProperties.getFileUploadPath() + Const.EXPENSE_FILE + old.getAttachment();
                FileUploadUtil.deleteFile(filePath);

                //附件名
                String attachment = ShiroKit.getUser().getName() + DateUtil.getDays() + new Random().nextInt(99) + "." + ToolUtil.getFileSuffix(file.getOriginalFilename());

                //上传文件
                FileUploadUtil.fileUpload(file, attachment, Const.EXPENSE_FILE);
                old.setAttachment(attachment);
            }
            this.expenseService.updateById(old);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS_TIP;
    }

    /**
     * 文件流下载
     *
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @Deprecated
    @RequestMapping("/download")
    public Object downloadFile(HttpServletResponse response, String expenseId) throws UnsupportedEncodingException {
        Expense expense = expenseService.selectById(expenseId);
        // 获取指定目录下的第一个文件
        String fileName = expense.getAttachment();
        // 如果文件名不为空，则进行下载
        if (fileName != null) {
            //设置文件路径
            String realPath = oaProperties.getFileUploadPath() + Const.EXPENSE_FILE;
            File file = new File(realPath, fileName);
            // 如果文件名存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}
