package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DynamicVars {

    private List<DnsCheck> dns_check;

    private Map<String, BaseVars> midwares_init;

    private Map<String,BaseVars> midwares_custom;

    private Map<String, BaseVars> env;

    private Map<String, BaseVars> midwares;

    private Map<String, BaseVars> models;

    public List<DnsCheck> getDns_check() {
        return dns_check;
    }

    public void setDns_check(List<DnsCheck> dns_check) {
        this.dns_check = dns_check;
    }

    public Map<String, BaseVars> getMidwares() {
        return midwares;
    }

    public void setMidwares(Map<String, BaseVars> midwares) {
        this.midwares = midwares;
    }

    public Map<String, BaseVars> getModels() {
        return models;
    }

    public void setModels(Map<String, BaseVars> models) {
        this.models = models;
    }

    public Map<String, BaseVars> getMidwares_init() {
        return midwares_init;
    }

    public void setMidwares_init(Map<String, BaseVars> midwares_init) {
        this.midwares_init = midwares_init;
    }

    public Map<String, BaseVars> getEnv() {
        return env;
    }

    public void setEnv(Map<String, BaseVars> env) {
        this.env = env;
    }

    public Map<String, BaseVars> getMidwares_custom() {
        return midwares_custom;
    }

    public void setMidwares_custom(Map<String, BaseVars> midwares_custom) {
        this.midwares_custom = midwares_custom;
    }
}
