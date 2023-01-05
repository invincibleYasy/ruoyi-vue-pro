package cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 项目配置 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProjectConfBaseVO {

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

    @ApiModelProperty(value = "是否需修改")
    private Boolean modifyFlag;

    @ApiModelProperty(value = "配置类型")
    private Integer type;

    @ApiModelProperty(value = "键描述")
    private String keyDesc;

    @ApiModelProperty(value = "配置版本")
    private String version;

}
