package cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo;

import cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo.ProcessRespVO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

@ApiModel("管理后台 - 模块 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ModuleRespVO extends ModuleBaseVO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Set<Long> processIds;

    private List<ProcessDO> processes;

}
