package cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import lombok.*;

import java.util.*;

import io.swagger.annotations.*;

@ApiModel("管理后台 - 服务器 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ServerRespVO extends ServerBaseVO {

    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Set<Long> processIds;

    private List<ProcessDO> processes;

}
