package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.ServerRespVO;
import io.swagger.annotations.ApiModel;

import lombok.Data;
import lombok.ToString;


import java.util.List;


@ApiModel("管理后台 - 生成服务器 Response VO")
@Data
@ToString(callSuper = true)
public class GenServersRespVO {
    private List<ServerRespVO> servers;
}
