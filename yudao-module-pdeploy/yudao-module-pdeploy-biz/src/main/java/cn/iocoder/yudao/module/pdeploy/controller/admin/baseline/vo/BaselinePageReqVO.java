package cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel("管理后台 - 基线版本分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaselinePageReqVO extends PageParam {

    @ApiModelProperty(value = "基线名称")
    private String name;

    @ApiModelProperty(value = "基线版本")
    private String version;

    @ApiModelProperty(value = "基线配置yaml")
    private String baselineConfYaml;

    @ApiModelProperty(value = "基线配置json")
    private String baselineConfJson;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "开始创建时间")
    private Date beginCreateTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
