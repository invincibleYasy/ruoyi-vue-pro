package cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 模块创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ModuleCreateReqVO extends ModuleBaseVO {
    private Set<Long> processIds;
}
