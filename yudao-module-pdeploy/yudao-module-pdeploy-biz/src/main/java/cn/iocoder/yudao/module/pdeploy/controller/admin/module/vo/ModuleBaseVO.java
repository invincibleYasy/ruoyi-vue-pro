package cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 模块 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ModuleBaseVO {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = "模块名称")
    private String name;

    @ApiModelProperty(value = "模块标签")
    private String tag;

    @ApiModelProperty(value = "模块标签")
    private String midwareTags;

    @ApiModelProperty(value = "镜像tags")
    private String imageTags;

    @ApiModelProperty(value = "模块类型")
    private Integer moduleType;

    @ApiModelProperty(value = "估时")
    private Long estimatedTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
