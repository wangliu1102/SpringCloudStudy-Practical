package com.huishu.oa.modular.system.wrapper;

import cn.hutool.core.util.StrUtil;
import com.huishu.oa.core.common.constant.factory.ConstantFactory;
import com.huishu.oa.modular.system.model.Dict;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * 字典列表的包装
 *
 * @author zx
 * @Date 2019年4月25日 18:10:31
 */
public class DictWrapper extends BaseControllerWrapper {

    public DictWrapper(Map<String, Object> single) {
        super(single);
    }

    public DictWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public DictWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public DictWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        StringBuffer detail = new StringBuffer();
        Integer id = Integer.valueOf(map.get("id").toString());
        List<Dict> dicts = ConstantFactory.me().findInDict(id);
        if (dicts != null) {
            for (Dict dict : dicts) {
                detail.append(dict.getCode() + ":" + dict.getName() + ",");
            }
            map.put("detail", StrUtil.removeSuffix(detail.toString(), ","));
        }
    }
}
