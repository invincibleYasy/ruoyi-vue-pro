package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


public class AnsibleTask {

    private String name;
    private Boolean active;
    private String tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
