package cn.iocoder.yudao.module.pdeploy.dal.dataobject;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class GenServerStrategy {

    private Integer minNum;

    private Integer clusterNum;

    private Integer minCpu;

    private Integer minMemory;

    public static Map<String, GenServerStrategy> serverStrategyMap() {
        Map<String, GenServerStrategy> serverConfigMap = new HashMap<>();
        serverConfigMap.put("ccpsapp",GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("ccpsads",GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("ccpscti",GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("scrm", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(16).minMemory(32).build());
        serverConfigMap.put("umpaas", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("smspaas", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("insight", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(16).minMemory(32).build());
        serverConfigMap.put("cem", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("sgpostfix", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("sgshorturl", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("sgcc", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("sgmain", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("crm", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("qa", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("kmia", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("kmnlp", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("kmwfe", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("kmkcs", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("kmtextbot", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("ccpsbilling", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("ccpsasr", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("ccpstts", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("ccpsfstrunk", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("ccpskamailio", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("ccpsvoicebot", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("yyoss", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("csofficepreview", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("csqc", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("cscbi", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("cssms", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("cswfm", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("cscse", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("cspostfix", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("cscck", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("csim", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("csts", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(16).minMemory(32).build());
        serverConfigMap.put("ejabberd", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("nginx", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("neo4j", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("postgresql", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("mongodb", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("oss", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("kafka", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("xxljob", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("rocketmq", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("rabbitmq", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(2).minMemory(4).build());
        serverConfigMap.put("elasticsearch", GenServerStrategy.builder().minNum(1).clusterNum(3).minCpu(4).minMemory(8).build());
        serverConfigMap.put("redis", GenServerStrategy.builder().minNum(1).clusterNum(3).minCpu(4).minMemory(8).build());
        serverConfigMap.put("etcd", GenServerStrategy.builder().minNum(3).clusterNum(3).minCpu(2).minMemory(4).build());
        serverConfigMap.put("greenplum", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(4).minMemory(8).build());
        serverConfigMap.put("mysql", GenServerStrategy.builder().minNum(1).clusterNum(2).minCpu(8).minMemory(16).build());
        serverConfigMap.put("redis5sentinel", GenServerStrategy.builder().minNum(3).clusterNum(3).minCpu(2).minMemory(4).build());
        serverConfigMap.put("redissentinel", GenServerStrategy.builder().minNum(3).clusterNum(3).minCpu(2).minMemory(4).build());
        return serverConfigMap;
    }


}
