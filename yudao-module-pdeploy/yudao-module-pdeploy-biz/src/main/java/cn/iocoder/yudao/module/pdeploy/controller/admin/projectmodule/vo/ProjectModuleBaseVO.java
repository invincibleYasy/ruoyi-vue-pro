package cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 项目模块关系 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProjectModuleBaseVO {

    @ApiModelProperty(value = "项目")
    private Long projectId;

    @ApiModelProperty(value = "模块")
    private Long moduleId;

}
