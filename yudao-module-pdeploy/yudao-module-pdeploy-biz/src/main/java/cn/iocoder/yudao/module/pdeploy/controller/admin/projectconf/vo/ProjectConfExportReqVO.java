package cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 项目配置 Excel 导出 Request VO", description = "参数和 ProjectConfPageReqVO 是一致的")
@Data
public class ProjectConfExportReqVO {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = " 所属项目")
    private Long projectId;

    @ApiModelProperty(value = "配置标签")
    private String tag;

    @ApiModelProperty(value = "标签过滤")
    private String tagFilter;

    @ApiModelProperty(value = "键")
    private String confKey;

    @ApiModelProperty(value = "值")
    private String confValue;

    @ApiModelProperty(value = "值占位符")
    private String confValuePlaceholder;

    @ApiModelProperty(value = "是否需修改")
    private Boolean modifyFlag;

    @ApiModelProperty(value = "配置类型")
    private Integer type;

    @ApiModelProperty(value = "键描述")
    private String keyDesc;

    @ApiModelProperty(value = "配置版本")
    private String version;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

}
