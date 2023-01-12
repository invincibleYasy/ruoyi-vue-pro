package cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectProcessRespVo {

    private ProjectProcess initEnv;

    private ProjectProcess deployMidware;

    private ProjectProcess initMidware;

    private ProjectProcess deployApp;


    @Data
    @Builder
    public static class ProjectProcess {

        private String name;

        private List<ProcessDO> processes;

    }

}
