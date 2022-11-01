package cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 项目模块关系 Excel 导出 Request VO", description = "参数和 ProjectModulePageReqVO 是一致的")
@Data
public class ProjectModuleExportReqVO {

    @ApiModelProperty(value = "项目")
    private Long projectId;

    @ApiModelProperty(value = "模块")
    private Long moduleId;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "开始创建时间")
    private Date beginCreateTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

}
