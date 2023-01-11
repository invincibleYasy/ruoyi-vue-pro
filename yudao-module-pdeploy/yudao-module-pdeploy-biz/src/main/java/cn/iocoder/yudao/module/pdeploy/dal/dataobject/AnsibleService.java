package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


public class AnsibleService {
    private String name;
    private Boolean cluster = true;
    private String tag_filter = "all";

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

    public String getTag_filter() {
        return tag_filter;
    }

    public void setTag_filter(String tag_filter) {
        this.tag_filter = tag_filter;
    }
}
