package cn.iocoder.yudao.module.pdeploy.dal.dataobject;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnsibleService {
    private String name;
    private String check_sub_domain;
    private String tag_filter = "all";
    private Boolean allow_repeat = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getAllow_repeat() {
        return allow_repeat;
    }

    public void setAllow_repeat(Boolean allow_repeat) {
        this.allow_repeat = allow_repeat;
    }
}
