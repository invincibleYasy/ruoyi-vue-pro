package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@ApiModel("管理后台 - 私有项目继承 Request VO")
@Data
@ToString(callSuper = true)
public class ProjectExtendReqVO {
    private Long extendProjectId;
    private Long currentProjectId;
}
