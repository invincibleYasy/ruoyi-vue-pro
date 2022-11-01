package cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 模块进程关系 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ModuleProcessExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("模块")
    private Long moduleId;

    @ExcelProperty("进程")
    private Long processId;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("备注")
    private String remark;

}
