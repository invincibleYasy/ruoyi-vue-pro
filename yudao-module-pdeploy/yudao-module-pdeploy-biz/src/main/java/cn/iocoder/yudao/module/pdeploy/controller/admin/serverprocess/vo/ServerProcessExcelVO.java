package cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 服务器进程关系 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ServerProcessExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("服务器")
    private Long serverId;

    @ExcelProperty("进程")
    private Long processId;

    @ExcelProperty("创建时间")
    private Date createTime;

}
