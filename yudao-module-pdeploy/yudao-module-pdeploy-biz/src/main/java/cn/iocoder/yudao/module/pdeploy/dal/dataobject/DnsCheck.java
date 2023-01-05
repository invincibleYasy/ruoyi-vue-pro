package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


import java.util.List;
public class DnsCheck {
    private String target_ip;
    private List<String> domain_list;

    public String getTarget_ip() {
        return target_ip;
    }

    public void setTarget_ip(String target_ip) {
        this.target_ip = target_ip;
    }

    public List<String> getDomain_list() {
        return domain_list;
    }

    public void setDomain_list(List<String> domain_list) {
        this.domain_list = domain_list;
    }
}
