package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


public class AnsibleService {
    private String name;
    private Boolean cluster = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCluster() {
        return cluster;
    }

    public void setCluster(Boolean cluster) {
        this.cluster = cluster;
    }
}
