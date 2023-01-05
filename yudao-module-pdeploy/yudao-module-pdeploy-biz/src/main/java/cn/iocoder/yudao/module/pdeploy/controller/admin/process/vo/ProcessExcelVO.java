package cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 进程 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ProcessExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty(value = "基线版本", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Long baselineId;

    @ExcelProperty("代码库")
    private Long codebaseId;

    @ExcelProperty("进程名称")
    private String name;

    @ExcelProperty("进程标签")
    private String tag;

    @ExcelProperty("进程版本")
    private String version;

    @ExcelProperty(value = "进程类型", converter = DictConvert.class)
    @DictFormat("process_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer processType;

    @ExcelProperty("CPU")
    private Integer cpu;

    @ExcelProperty("内存(GB)")
    private Integer memory;

    @ExcelProperty("高可用")
    private Boolean isHa;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("备注")
    private String remark;

}
