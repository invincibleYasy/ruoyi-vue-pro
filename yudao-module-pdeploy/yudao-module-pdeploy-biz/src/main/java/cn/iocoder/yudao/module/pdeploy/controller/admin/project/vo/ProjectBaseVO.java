package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 私有项目 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProjectBaseVO {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "项目配置Yaml")
    private String projConfYaml;

    @ApiModelProperty(value = "项目配置Json")
    private String projConfJson;

    @ApiModelProperty(value = "自定义中间件")
    private String midwareCustomTags;

    @ApiModelProperty(value = "项目关联Tag")
    private String allProjTags;

}
