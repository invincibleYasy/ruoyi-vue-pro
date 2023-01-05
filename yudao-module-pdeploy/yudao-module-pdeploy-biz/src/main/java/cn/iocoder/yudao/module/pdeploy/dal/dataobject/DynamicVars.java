package cn.iocoder.yudao.module.pdeploy.dal.dataobject;


import java.util.List;
import java.util.Map;

public class DynamicVars {

    private List<DnsCheck> dns_check;

    private List<AnsibleTask> post_check_tasks;

    private List<AnsibleTask> rabbitmq_init_tasks;

    private List<AnsibleTask> rocketmq_init_tasks;

    private List<AnsibleTask> minio_init_tasks;

    private List<AnsibleTask> elasticsearch_init_tasks;

    private List<AnsibleTask> mysql_init_tasks;

    private List<AnsibleTask> postgresql_init_tasks;

    private Map<String, Boolean> midwares_choice;

    private Map<String, Boolean> midwares_cluster_choice;

    private Map<String, Boolean> midwares_custom_choice;

    private BaseVars basic_info;

    private Map<String,BaseVars> midwares;

    private Map<String,BaseVars> models;

    public List<DnsCheck> getDns_check() {
        return dns_check;
    }

    public void setDns_check(List<DnsCheck> dns_check) {
        this.dns_check = dns_check;
    }

    public Map<String, Boolean> getMidwares_choice() {
        return midwares_choice;
    }

    public void setMidwares_choice(Map<String, Boolean> midwares_choice) {
        this.midwares_choice = midwares_choice;
    }

    public Map<String, Boolean> getMidwares_cluster_choice() {
        return midwares_cluster_choice;
    }

    public void setMidwares_cluster_choice(Map<String, Boolean> midwares_cluster_choice) {
        this.midwares_cluster_choice = midwares_cluster_choice;
    }

    public Map<String, Boolean> getMidwares_custom_choice() {
        return midwares_custom_choice;
    }

    public void setMidwares_custom_choice(Map<String, Boolean> midwares_custom_choice) {
        this.midwares_custom_choice = midwares_custom_choice;
    }

    public BaseVars getBasic_info() {
        return basic_info;
    }

    public void setBasic_info(BaseVars basic_info) {
        this.basic_info = basic_info;
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

    public List<AnsibleTask> getRabbitmq_init_tasks() {
        return rabbitmq_init_tasks;
    }

    public void setRabbitmq_init_tasks(List<AnsibleTask> rabbitmq_init_tasks) {
        this.rabbitmq_init_tasks = rabbitmq_init_tasks;
    }

    public List<AnsibleTask> getRocketmq_init_tasks() {
        return rocketmq_init_tasks;
    }

    public void setRocketmq_init_tasks(List<AnsibleTask> rocketmq_init_tasks) {
        this.rocketmq_init_tasks = rocketmq_init_tasks;
    }

    public List<AnsibleTask> getMinio_init_tasks() {
        return minio_init_tasks;
    }

    public void setMinio_init_tasks(List<AnsibleTask> minio_init_tasks) {
        this.minio_init_tasks = minio_init_tasks;
    }

    public List<AnsibleTask> getElasticsearch_init_tasks() {
        return elasticsearch_init_tasks;
    }

    public void setElasticsearch_init_tasks(List<AnsibleTask> elasticsearch_init_tasks) {
        this.elasticsearch_init_tasks = elasticsearch_init_tasks;
    }

    public List<AnsibleTask> getMysql_init_tasks() {
        return mysql_init_tasks;
    }

    public void setMysql_init_tasks(List<AnsibleTask> mysql_init_tasks) {
        this.mysql_init_tasks = mysql_init_tasks;
    }

    public List<AnsibleTask> getPostgresql_init_tasks() {
        return postgresql_init_tasks;
    }

    public void setPostgresql_init_tasks(List<AnsibleTask> postgresql_init_tasks) {
        this.postgresql_init_tasks = postgresql_init_tasks;
    }

    public List<AnsibleTask> getPost_check_tasks() {
        return post_check_tasks;
    }

    public void setPost_check_tasks(List<AnsibleTask> post_check_tasks) {
        this.post_check_tasks = post_check_tasks;
    }
}
