package cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

@ApiModel("管理后台 - 代码库 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CodebaseRespVO extends CodebaseBaseVO {

    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
