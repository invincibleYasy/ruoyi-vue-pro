package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 私有项目 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ProjectExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty(value = "基线版本", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Long baselineId;

    @ExcelProperty("项目名称")
    private String name;

    @ExcelProperty(value = "项目配置")
    private String projConf;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("备注")
    private String remark;

}
