package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("管理后台 - 服务器合并 Request VO")
@Data
public class MergeServerReqVO {
    private Long projectId;
    private List<Long> mergeServers;
    private String name;
    private Integer cpu;
    private Integer memory;
    private String ip;
    private String remark;
}
