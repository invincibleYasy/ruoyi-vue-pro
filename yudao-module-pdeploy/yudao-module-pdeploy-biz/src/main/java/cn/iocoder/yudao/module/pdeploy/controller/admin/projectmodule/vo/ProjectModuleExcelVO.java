package cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 项目模块关系 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ProjectModuleExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("项目")
    private Long projectId;

    @ExcelProperty("模块")
    private Long moduleId;

    @ExcelProperty("创建时间")
    private Date createTime;

}
