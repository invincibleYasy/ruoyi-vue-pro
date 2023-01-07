package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 私有项目 Excel 导出 Request VO", description = "参数和 ProjectPageReqVO 是一致的")
@Data
public class ProjectExportReqVO {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "项目配置")
    private String projConfYaml;

    @ApiModelProperty(value = "项目配置Json")
    private String projConfJson;

    @ApiModelProperty(value = "自定义中间件")
    private String midwareCustomTags;

    @ApiModelProperty(value = "项目关联Tag")
    private String allProjTags;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "开始创建时间")
    private Date beginCreateTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
