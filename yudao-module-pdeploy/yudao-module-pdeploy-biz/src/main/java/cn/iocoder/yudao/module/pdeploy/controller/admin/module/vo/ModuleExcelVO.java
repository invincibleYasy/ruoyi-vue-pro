package cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 模块 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ModuleExcelVO {

    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty(value = "基线版本", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Long baselineId;

    @ExcelProperty("模块名称")
    private String name;

    @ExcelProperty("模块标签")
    private String tag;

    @ExcelProperty( "模块标签")
    private String midwareTags;

    @ExcelProperty(value = "镜像tags")
    private String imageTags;

    @ExcelProperty(value = "模块类型", converter = DictConvert.class)
    @DictFormat("module_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer moduleType;

    @ExcelProperty("估时")
    private Long estimatedTime;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("备注")
    private String remark;

}
