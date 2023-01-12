package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


public class AnsibleService {
    private String name;
    private String check_sub_domain;
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

    public String getCheck_sub_domain() {
        return check_sub_domain;
    }

    public void setCheck_sub_domain(String check_sub_domain) {
        this.check_sub_domain = check_sub_domain;
    }
}
