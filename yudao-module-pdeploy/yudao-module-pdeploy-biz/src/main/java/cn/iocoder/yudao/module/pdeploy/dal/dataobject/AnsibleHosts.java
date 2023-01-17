package cn.iocoder.yudao.module.pdeploy.dal.dataobject;

import lombok.Data;

import java.util.Map;

/**
 *
 */
@Data
public class AnsibleHosts {
    private Map<String, Object> hosts;
    private Map<String, AnsibleHosts> children;
    private Map<String, String> vars;
}
