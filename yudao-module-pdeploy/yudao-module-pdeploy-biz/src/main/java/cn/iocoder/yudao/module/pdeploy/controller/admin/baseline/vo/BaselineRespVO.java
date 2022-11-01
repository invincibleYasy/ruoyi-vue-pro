package cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

@ApiModel("管理后台 - 基线版本 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaselineRespVO extends BaselineBaseVO {

    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
