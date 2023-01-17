package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 私有项目更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectUpdateReqVO extends ProjectBaseVO {

    @ApiModelProperty(value = "ID", required = true)
    @NotNull(message = "ID不能为空")
    private Long id;
    private Set<Long> moduleIds;
    private Integer type;
    private List<Integer> types;
}
