package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@ApiModel("管理后台 - 项目ansible配置 Response VO")
@Data
@ToString(callSuper = true)
public class ProjectConfRespVO {

    private String mainConf;

    private String ccpassConf;

}
