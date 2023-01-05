package cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 基线版本 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BaselineBaseVO {

    @ApiModelProperty(value = "基线名称", required = true)
    @NotNull(message = "基线名称不能为空")
    private String name;

    @ApiModelProperty(value = "基线版本", required = true)
//    @NotNull(message = "基线版本不能为空")
    private String version;

    @ApiModelProperty(value = "主配置")
    private String mainConf;

    @ApiModelProperty(value = "ccpass配置")
    private String mainConfCcpass;

    @ApiModelProperty(value = "备注")
    private String remark;

}
