package cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 进程 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProcessBaseVO {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = "代码库")
    private Long codebaseId;

    @ApiModelProperty(value = "进程名称")
    private String name;

    @ApiModelProperty(value = "进程标签")
    private String tag;

    @ApiModelProperty(value = "过滤标签")
    private String tagFilter;

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

    @ApiModelProperty(value = "备注")
    private String remark;

}
