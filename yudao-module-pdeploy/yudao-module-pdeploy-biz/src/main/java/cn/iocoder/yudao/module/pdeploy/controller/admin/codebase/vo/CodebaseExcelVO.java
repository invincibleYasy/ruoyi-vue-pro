package cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 代码库 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class CodebaseExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("地址")
    private String gitUrl;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("备注")
    private String remark;

}
