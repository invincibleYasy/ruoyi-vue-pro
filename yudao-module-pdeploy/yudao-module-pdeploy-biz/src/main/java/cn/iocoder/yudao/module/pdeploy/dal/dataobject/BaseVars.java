package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


import cn.iocoder.yudao.module.pdeploy.service.project.DynamicVarsValFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public class BaseVars {
    //    @JsonIgnore
    private String name;

    private List<String> sub_domains;

    private String check_sub_domain;

    //    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DynamicVarsValFilter.class)
    private Map<String, String> image_tags;

    //    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DynamicVarsValFilter.class)
    private Map<String, Object> vars;

    //    @JsonIgnore
    private List<AnsibleService> ansible_services;
    //    @JsonIgnore
    private List<String> midware_tags;


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

    public List<String> getSub_domains() {
        return sub_domains;
    }

    public void setSub_domains(List<String> sub_domains) {
        this.sub_domains = sub_domains;
    }

    public String getCheck_sub_domain() {
        return check_sub_domain;
    }

    public void setCheck_sub_domain(String check_sub_domain) {
        this.check_sub_domain = check_sub_domain;
    }
}
