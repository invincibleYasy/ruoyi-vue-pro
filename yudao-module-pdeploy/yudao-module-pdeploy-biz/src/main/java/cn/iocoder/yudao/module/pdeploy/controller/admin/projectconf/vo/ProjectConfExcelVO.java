package cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 项目配置 Excel VO
 *
 * @author 管理员
 */
@Data
public class ProjectConfExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("基线版本")
    private Long baselineId;

    @ExcelProperty(" 所属项目")
    private Long projectId;

    @ExcelProperty("键")
    private String confKey;

    @ExcelProperty("值")
    private String confValue;

    @ExcelProperty(value = "是否需修改", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Boolean modifyFlag;

    @ExcelProperty(value = "配置类型", converter = DictConvert.class)
    @DictFormat("ansible_conf_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer type;

    @ExcelProperty("键描述")
    private String keyDesc;

    @ExcelProperty("配置版本")
    private String version;

    @ExcelProperty("创建时间")
    private Date createTime;

}
