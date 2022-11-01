package cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 服务器创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ServerCreateReqVO extends ServerBaseVO {
    private Set<Long> processIds;
}
