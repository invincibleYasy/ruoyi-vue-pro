package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


import java.util.List;
import java.util.Map;

public class BaseVars {
    private String name;
    private String tag;

    private Map<String, String> image_tags;

    private Map<String, Object> vars;

    private List<AnsibleService> ansible_services;

    private List<String> midware_tags;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, String> getImage_tags() {
        return image_tags;
    }

    public void setImage_tags(Map<String, String> image_tags) {
        this.image_tags = image_tags;
    }

    public Map<String, Object> getVars() {
        return vars;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    public List<AnsibleService> getAnsible_services() {
        return ansible_services;
    }

    public void setAnsible_services(List<AnsibleService> ansible_services) {
        this.ansible_services = ansible_services;
    }

    public List<String> getMidware_tags() {
        return midware_tags;
    }

    public void setMidware_tags(List<String> midware_tags) {
        this.midware_tags = midware_tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
