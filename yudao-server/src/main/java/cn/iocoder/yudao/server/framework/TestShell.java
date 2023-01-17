package cn.iocoder.yudao.server.framework;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.AnsibleHosts;
import cn.iocoder.yudao.module.pdeploy.service.baseline.RepresenterNull2Empty;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import javax.crypto.spec.OAEPParameterSpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestShell {
    public static void main(String[] args) throws IOException {
//        AnsibleHosts ansibleHosts = new AnsibleHosts();
//        Map<String, Object> hosts = new HashMap<>();
//        hosts.put("127.0.0.1", null);
//        ansibleHosts.setHosts(hosts);
//
//        Map<String, String> varsMap = new HashMap<>();
//        varsMap.put("ansible_ssh_user", "root");
//        ansibleHosts.setVars(varsMap);
//
//        Map<String, AnsibleHosts> children = new HashMap<>();
//        AnsibleHosts ansibleHostsc = new AnsibleHosts();
//        Map<String, String> hostsMap = new HashMap<>();
//        Map<String, Object> chosts = new HashMap<>();
//        hostsMap.put("ansible_ssh_host", "127.0.0.1");
//        hostsMap.put("ansible_public_host", "127.0.0.1");
//        chosts.put("shared_mysql001", hostsMap);
//        ansibleHostsc.setHosts(chosts);
//        children.put("shared_mysql", ansibleHostsc);
//        ansibleHosts.setChildren(children);
//        Map<String, Object> allMap = new HashMap<>();
//        allMap.put("all", ansibleHosts);
        DumperOptions options = new DumperOptions();
        options.setIndentWithIndicator(true);
        options.setIndicatorIndent(2);
        Representer representer = new RepresenterNull2Empty();
        Yaml yaml = new Yaml(representer, options);
//        String s = yaml.dumpAsMap(allMap);
//        System.out.println(s);


        String yamlstr =
                "all:\n" +
                        "  hosts:\n" +
                        "    127.0.0.1:\n" +
                        "  children:\n" +
                        "    all_nodes:\n" +
                        "      hosts:\n" +
                        "        127.0.0.1:\n" +
                        "    shared_mysql:\n" +
                        "      hosts:\n" +
                        "        shared_mysql001:\n" +
                        "          ansible_ssh_host: 127.0.0.1\n" +
                        "          ansible_public_host: 127.0.0.1  \n" +
                        "        shared_mysql002:\n" +
                        "          ansible_ssh_host: 127.0.0.1\n" +
                        "          ansible_public_host: 127.0.0.1\n" +
                        "  vars:\n" +
                        "    ansible_ssh_user: root";
        Map<String, Object> ansibleHosts = new LinkedHashMap<>();
        Map<String, Object> allVal = new LinkedHashMap<>();
        Map<String, Object> hostsVal = new LinkedHashMap<>();
        hostsVal.put("127.0.0.1", null);

        Map<String, Object> childrenVal = new LinkedHashMap<>();
        Map<String, Object> allNodesVal = new LinkedHashMap<>();
        allNodesVal.put("127.0.0.1", null);
        childrenVal.put("all_nodes", allNodesVal);
        Map<String, Object> sharedMysqlVal = new LinkedHashMap<>();
        Map<String, Object> sharedMysqlHostsVal = new LinkedHashMap<>();

        Map<String, Object> sharedMysqlHosts001Val = new LinkedHashMap<>();
        sharedMysqlHosts001Val.put("ansible_ssh_host", "127.0.0.1");
        sharedMysqlHosts001Val.put("ansible_public_host", "127.0.0.1");
        Map<String, Object> sharedMysqlHosts002Val = new LinkedHashMap<>();
        sharedMysqlHosts002Val.put("ansible_ssh_host", "127.0.0.1");
        sharedMysqlHosts002Val.put("ansible_public_host", "127.0.0.1");

        sharedMysqlHostsVal.put("shared_mysql001", sharedMysqlHosts001Val);
        sharedMysqlHostsVal.put("shared_mysql002", sharedMysqlHosts002Val);

        sharedMysqlVal.put("hosts", sharedMysqlHostsVal);
        childrenVal.put("shared_mysql", sharedMysqlVal);


        Map<String, Object> varsVal = new LinkedHashMap<>();
        varsVal.put("ansible_ssh_user", "root");
        allVal.put("hosts", hostsVal);
        allVal.put("children", childrenVal);
        allVal.put("vars", varsVal);
        ansibleHosts.put("all", allVal);
//        Object load = yaml.load(yamlstr);
//        System.out.println(load);
        String s1 = yaml.dumpAsMap(ansibleHosts);
        System.out.println(s1);

    }

    //    ansible-playbook -i hosts-dev  task-entry-of-role-init-env.yml -t config-hosts
    private static void testShell(String taskId) throws IOException {
        ProcessBuilder process = new ProcessBuilder();
        Process ansiblePlayProcess = process.directory(new File("/Users/zero/Workspace/code/udesk/udesk_local_deploy"))
                .command(
                        "/usr/local/bin/ansible-playbook",
                        "-i", "hosts-dev",
                        "task-entry-of-role-init-env.yml",
                        "-t", "config-hosts"
                )
                .start();
        ArrayList<String> result = new ArrayList<>();
        InputStream in = null;
        try {
            in = ansiblePlayProcess.getInputStream();
            BufferedReader reader = IoUtil.getReader(in, Charset.defaultCharset());
            IoUtil.readLines(reader, (LineHandler) System.out::println);
            IoUtil.readLines(in, Charset.defaultCharset(), result);
        } finally {
            IoUtil.close(in);
            ansiblePlayProcess.destroy();
        }
    }
}
