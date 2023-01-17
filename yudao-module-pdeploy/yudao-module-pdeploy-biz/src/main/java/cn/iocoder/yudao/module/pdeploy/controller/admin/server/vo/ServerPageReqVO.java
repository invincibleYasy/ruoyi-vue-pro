package cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel("管理后台 - 服务器分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ServerPageReqVO extends PageParam {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = "项目")
    private Long projectId;

    @ApiModelProperty(value = "服务器名称")
    private String name;

    @ApiModelProperty(value = "服务器IP")
    private String ip;

    @ApiModelProperty(value = "CPU")
    private Integer cpu;

    @ApiModelProperty(value = "内存(GB)")
    private Integer memory;

    @ApiModelProperty(value = "所属环境")
    private Integer envType;

    @ExcelProperty(value = "标签")
    private String tag;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "开始创建时间")
    private Date beginCreateTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
