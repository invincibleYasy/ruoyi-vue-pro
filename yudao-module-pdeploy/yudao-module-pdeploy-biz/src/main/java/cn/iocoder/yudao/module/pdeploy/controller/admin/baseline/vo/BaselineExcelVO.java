package cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 基线版本 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class BaselineExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("基线名称")
    private String name;

    @ExcelProperty("基线版本")
    private String version;

    @ExcelProperty(value = "基线配置yaml")
    private String baselineConfYaml;

    @ExcelProperty(value = "基线配置json")
    private String baselineConfJson;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("备注")
    private String remark;

}
