package cn.iocoder.yudao.module.pdeploy.service.baseline;


import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.module.ModuleMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.moduleprocess.ModuleProcessMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectmodule.ProjectModuleMapper;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.baseline.BaselineConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.baseline.BaselineMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 基线版本 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BaselineServiceImpl implements BaselineService {

    @Resource
    private BaselineMapper baselineMapper;
    @Resource
    private ProjectConfMapper projectConfMapper;
    @Resource
    private ThreadPoolTaskExecutor executor;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private ProcessMapper processMapper;
    @Resource
    private ModuleProcessMapper moduleProcessMapper;
    @Resource
    private ProjectModuleMapper projectModuleMapper;

    @Override
    public Long createBaseline(BaselineCreateReqVO createReqVO) {
        // 插入
        BaselineDO baseline = BaselineConvert.INSTANCE.convert(createReqVO);
        Yaml yaml = new Yaml();
        DynamicVars confYaml = yaml.loadAs(createReqVO.getBaselineConfYaml(), DynamicVars.class);
        String baselineConfJson = JsonUtils.toJsonPrettyString(confYaml);
        baseline.setBaselineConfJson(baselineConfJson);
        baselineMapper.insert(baseline);
        //异步更新
        executor.submit(() -> parseDynamicVars(baseline));
        // 返回
        return baseline.getId();
    }

    @Override
    public void updateBaseline(BaselineUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateBaselineExists(updateReqVO.getId());
        // 更新
        BaselineDO updateObj = BaselineConvert.INSTANCE.convert(updateReqVO);
        Yaml yaml = new Yaml();
        DynamicVars confYaml = yaml.loadAs(updateReqVO.getBaselineConfYaml(), DynamicVars.class);
        String baselineConfJson = JsonUtils.toJsonPrettyString(confYaml);
        updateObj.setBaselineConfJson(baselineConfJson);
        baselineMapper.updateById(updateObj);
        //异步更新
        Future<?> submit = executor.submit(() -> parseDynamicVars(updateObj));
        try {
            submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void parseDynamicVars(BaselineDO baselineDO) {
        Long id = baselineDO.getId();
        String baselineConfJson = baselineDO.getBaselineConfJson();
        DynamicVars dynamicVars = JsonUtils.parseObject(baselineConfJson, DynamicVars.class);
        if (null != dynamicVars) {
            // 1. 删除当前基线下的模块信息和进程信息
            List<ModuleDO> moduleDOS = moduleMapper.selectList("baseline_id", id);
            if (CollectionUtils.isNotEmpty(moduleDOS)) {
                moduleDOS.forEach(moduleDO -> {
                    List<ModuleProcessDO> moduleProcessDOS = moduleProcessMapper.selectList("module_id", moduleDO.getId());
                    List<Long> moduleProcessIds = moduleProcessDOS.stream().map(ModuleProcessDO::getId).collect(Collectors.toList());
                    List<Long> processIds = moduleProcessDOS.stream().map(ModuleProcessDO::getProcessId).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(moduleProcessIds)) {
                        moduleProcessMapper.deleteBatchIds(moduleProcessIds);
                        processMapper.deleteBatchIds(processIds);
                    }
                });
            }
            Map<String, Object> moduleDelParams = new HashMap<>();
            moduleDelParams.put("baseline_id", id);
            moduleMapper.deleteByMap(moduleDelParams);
            // 2.删除当前基线的变量配置
            Map<String, Object> confDelParams = new HashMap<>();
            confDelParams.put("baseline_id", id);
            confDelParams.put("project_id", -1);
            projectConfMapper.deleteByMap(confDelParams);

            // 解析基础配置
            parseBaseConf(id, dynamicVars);
            // 解析初始化信息
            parseAnsibleTasks(id, dynamicVars);
            // 解析中间件信息
            parseMidWares(id, dynamicVars);
            // 解析模块信息
            parseModels(id, dynamicVars);
            // 更新占位符
            DumperOptions options = new DumperOptions();
            options.setIndentWithIndicator(true);
            options.setIndicatorIndent(2);
            Yaml yaml = new Yaml(new RepresenterNull2Empty(), options);
            String dump = yaml.dumpAsMap(dynamicVars);
            BaselineDO build = BaselineDO.builder().id(id).baselineConfYaml(dump).build();
            baselineMapper.updateById(build);
        }
    }


    private void parseBaseConf(Long id, DynamicVars dynamicVars) {
        BaseVars basic_info = dynamicVars.getBasic_info();
        if (null != basic_info) {
            Map<String, Object> vars = basic_info.getVars();
            if (MapUtils.isNotEmpty(vars)) {
                List<ProjectConfDO> projectConfDOS = new ArrayList<>();
                vars.forEach((confKey, confValue) -> {
                    if (null == confValue) {
                        confValue = "";
                    }
                    if (confValue instanceof Collection) {
                        confValue = JsonUtils.toJsonString(confValue);
                    }
                    String confValuePlaceholder = "basic_" + confKey;
                    ProjectConfDO confDO = ProjectConfDO.builder()
                            .projectId(-1l)
                            .baselineId(id)
                            .tag("basic")
                            .confKey(confKey)
                            .tagFilter(genTagFilter(confKey))
                            .confValue(confValue.toString())
                            .confValuePlaceholder(confValuePlaceholder)
                            .type(1)
                            .modifyFlag(true)
                            .build();
                    projectConfDOS.add(confDO);
                    vars.put(confKey, confValuePlaceholder);
                });
                projectConfMapper.insertBatch(projectConfDOS);
            }
        }

    }

    private void parseAnsibleTasks(Long id, DynamicVars dynamicVars) {
        List<AnsibleTask> elasticsearch_init_tasks = dynamicVars.getElasticsearch_init_tasks();
        List<AnsibleTask> minio_init_tasks = dynamicVars.getMinio_init_tasks();
        List<AnsibleTask> mysql_init_tasks = dynamicVars.getMysql_init_tasks();
        List<AnsibleTask> postgresql_init_tasks = dynamicVars.getPostgresql_init_tasks();
        List<AnsibleTask> rabbitmq_init_tasks = dynamicVars.getRabbitmq_init_tasks();
        List<AnsibleTask> rocketmq_init_tasks = dynamicVars.getRocketmq_init_tasks();
        List<AnsibleTask> post_check_tasks = dynamicVars.getPost_check_tasks();

    }

    private void parseMidWares(Long id, DynamicVars dynamicVars) {
        Map<String, BaseVars> midwares = dynamicVars.getMidwares();
        if (MapUtils.isNotEmpty(midwares)) {
            parse(id, dynamicVars.getMidwares(), 2);
        }
    }

    private void parseModels(Long id, DynamicVars dynamicVars) {
        Map<String, BaseVars> models = dynamicVars.getModels();
        if (MapUtils.isNotEmpty(models)) {
            parse(id, dynamicVars.getModels(), 1);
        }
    }

    private void parse(Long id, Map<String, BaseVars> maps, Integer type) {
        maps.forEach((key, value) -> {
            ModuleDO moduleDO = new ModuleDO();
            moduleDO.setBaselineId(id);
            moduleDO.setTag(key);
            moduleDO.setName(StringUtils.isEmpty(value.getName()) ? key : value.getName());
            moduleDO.setModuleType(type);
            // 解析中间件依赖
            List<String> midwareTags = value.getMidware_tags();
            if (CollectionUtils.isNotEmpty(midwareTags)) {
                moduleDO.setMidwareTags(String.join(",", midwareTags));
            }
            // 解析镜像
            Map<String, String> image_tags = value.getImage_tags();
            if (MapUtils.isNotEmpty(image_tags)) {
                moduleDO.setImageTags(String.join(",", image_tags.values()));
            }
            moduleMapper.insert(moduleDO);
            // 更新历史的项目模块关系
            projectModuleMapper.update(ProjectModuleDO.builder().moduleId(moduleDO.getId()).build(),
                    new LambdaQueryWrapperX<ProjectModuleDO>().eqIfPresent(ProjectModuleDO::getModuleTag, moduleDO.getTag()));
            // 解析进程信息
            List<AnsibleService> ansibleServices = value.getAnsible_services();
            if (CollectionUtils.isNotEmpty(ansibleServices)) {
                List<ProcessDO> processDOS = new ArrayList<>();
                ansibleServices.forEach(ansibleService -> {
                    ProcessDO processDO = new ProcessDO();
                    processDO.setBaselineId(id);
                    processDO.setName(ansibleService.getName());
                    processDO.setProcessType(type);
                    processDO.setTag(key);
                    processDOS.add(processDO);
                });
                processMapper.insertBatch(processDOS);
                List<ModuleProcessDO> moduleProcessDOS = new ArrayList<>();
                processDOS.forEach(processDO -> {
                    ModuleProcessDO moduleProcessDO = new ModuleProcessDO();
                    moduleProcessDO.setModuleId(moduleDO.getId());
                    moduleProcessDO.setProcessId(processDO.getId());
                    moduleProcessDOS.add(moduleProcessDO);
                });
                moduleProcessMapper.insertBatch(moduleProcessDOS);
            }
            // 解析变量
            Map<String, Object> vars = value.getVars();
            if (MapUtils.isNotEmpty(vars)) {
                List<ProjectConfDO> projectConfDOS = new ArrayList<>();
                vars.forEach((confKey, confValue) -> {
                    if (null == confValue) {
                        confValue = "";
                    }
                    if (confValue instanceof Collection) {
                        confValue = JsonUtils.toJsonString(confValue);
                    }
                    ProjectConfDO confDO = ProjectConfDO.builder()
                            .projectId(-1l)
                            .baselineId(id)
                            .tag(key)
                            .tagFilter(genTagFilter(confKey))
                            .confKey(confKey)
                            .confValue(confValue.toString())
                            .confValuePlaceholder(key + "_" + confKey)
                            .type(1)
                            .modifyFlag(true)
                            .build();
                    projectConfDOS.add(confDO);
                });
                projectConfMapper.insertBatch(projectConfDOS);
            }
        });
    }

    private String genTagFilter(String confKey) {
        if (StringUtils.isNotEmpty(confKey)) {
            String[] splits = confKey.split("__");
            if (splits.length > 0) {
                String split = splits[0];
                String[] arrTagFilters = split.split("_");
                List<String> collect = new ArrayList<>(Arrays.asList(arrTagFilters));
                return String.join(",", collect);
            }
        }
        return Strings.EMPTY;
    }

    private void iterateAndProcess(Long baselineId, Integer type, List<ProjectConfDO> mainConfigList, Map<String, Object> ymlEntry, String rootKey) {
        for (String key : ymlEntry.keySet()) {
            Object value = ymlEntry.get(key);
            if (null == value) {
                value = "";
            }
            if (value instanceof LinkedHashMap) {
                iterateAndProcess(baselineId, type, mainConfigList, (LinkedHashMap<String, Object>) value, StringUtils.isEmpty(rootKey) ? key : rootKey
                        + "." + key);
            } else {
                if (value instanceof Collection) {
                    value = JsonUtils.toJsonString(value);
                }
                ProjectConfDO confDO = ProjectConfDO.builder()
                        .projectId(-1l)
                        .baselineId(baselineId)
                        .confKey(StringUtils.isEmpty(rootKey) ? key : rootKey + "." + key)
                        .confValue(value.toString())
                        .type(type)
                        .modifyFlag(false)
                        .build();
                mainConfigList.add(confDO);
            }
        }
    }


    @Override
    public void deleteBaseline(Long id) {
        // 校验存在
        this.validateBaselineExists(id);
        // 删除
        baselineMapper.deleteById(id);
    }

    private void validateBaselineExists(Long id) {
        if (baselineMapper.selectById(id) == null) {
            throw exception(BASELINE_NOT_EXISTS);
        }
    }

    @Override
    public BaselineDO getBaseline(Long id) {
        return baselineMapper.selectById(id);
    }

    @Override
    public List<BaselineDO> getBaselineList(Collection<Long> ids) {
        return baselineMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BaselineDO> getBaselinePage(BaselinePageReqVO pageReqVO) {
        return baselineMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BaselineDO> getBaselineList(BaselineExportReqVO exportReqVO) {
        return baselineMapper.selectList(exportReqVO);
    }

}
