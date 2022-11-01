package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.ModuleRespVO;
import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.ServerRespVO;
import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

@ApiModel("管理后台 - 私有项目 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectRespVO extends ProjectBaseVO {

    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Set<Long> moduleIds ;

    private List<ModuleRespVO> modules;

    private List<ServerRespVO> servers;
}
