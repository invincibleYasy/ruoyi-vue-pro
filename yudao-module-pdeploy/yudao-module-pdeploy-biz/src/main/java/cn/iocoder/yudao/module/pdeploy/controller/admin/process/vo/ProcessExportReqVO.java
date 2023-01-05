package cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 进程 Excel 导出 Request VO", description = "参数和 ProcessPageReqVO 是一致的")
@Data
public class ProcessExportReqVO {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = "代码库")
    private Long codebaseId;

    @ApiModelProperty(value = "进程名称")
    private String name;

    @ApiModelProperty(value = "进程标签")
    private String tag;

    @ApiModelProperty(value = "进程版本")
    private String version;

    @ApiModelProperty(value = "进程类型")
    private Integer processType;

    @ApiModelProperty(value = "CPU")
    private Integer cpu;

    @ApiModelProperty(value = "内存(GB)")
    private Integer memory;

    @ApiModelProperty(value = "高可用")
    private Boolean isHa;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "开始创建时间")
    private Date beginCreateTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
