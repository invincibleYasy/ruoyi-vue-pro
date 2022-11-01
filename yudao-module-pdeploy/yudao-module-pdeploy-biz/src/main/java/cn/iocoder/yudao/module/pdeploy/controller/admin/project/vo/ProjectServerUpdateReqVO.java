package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Set;

@ApiModel("管理后台 - 私有项目更新项目服务器 Request VO")
@Data
@ToString(callSuper = true)
public class ProjectServerUpdateReqVO {

   private Long serverId;
   private Set<Long> processIds;
}
