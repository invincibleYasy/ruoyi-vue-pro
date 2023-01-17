package cn.iocoder.yudao.module.pdeploy.service.project;

import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.ModuleRespVO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.BaseVars;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.DynamicVars;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.GenServerStrategy;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.baseline.BaselineMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.module.ModuleMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectmodule.ProjectModuleMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.server.ServerMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.serverprocess.ServerProcessMapper;
import cn.iocoder.yudao.module.pdeploy.enums.EnvType;
import cn.iocoder.yudao.module.pdeploy.enums.DeployStep;
import cn.iocoder.yudao.module.pdeploy.service.baseline.RepresenterNull2Empty;
import cn.iocoder.yudao.module.pdeploy.service.projectmodule.ProjectModuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.project.ProjectConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.project.ProjectMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 私有项目 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ProjectModuleService projectModuleService;
    @Resource
    private ServerMapper serverMapper;
    @Resource
    private ServerProcessMapper serverProcessMapper;
    @Resource
    private ProjectConfMapper projectConfMapper;
    @Resource
    private ProjectModuleMapper projectModuleMapper;
    @Resource
    private BaselineMapper baselineMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private ProcessMapper processMapper;


    @Override
    @Transactional
    public Long createProject(ProjectCreateReqVO createReqVO) {
        // 插入
        ProjectDO project = ProjectConvert.INSTANCE.convert(createReqVO);
        List<ModuleDO> moduleDOS = moduleMapper.selectBatchIds(createReqVO.getModuleIds());
        wrapperProject(project, moduleDOS);
        projectMapper.insert(project);
        //处理关联模块信息
        projectModuleService.createProjectModule(project.getId(), moduleDOS);
        //生成项目配置信息
//        batchSaveProjectConf(project.getId(),project.getBaselineId());
        return project.getId();
    }

    private void wrapperProject(ProjectDO project, List<ModuleDO> moduleDOS) {
        Long baselineId = project.getBaselineId();
        BaselineDO baselineDO = baselineMapper.selectById(baselineId);
        project.setProjConfYaml(baselineDO.getBaselineConfYaml());
        Yaml yaml = new Yaml();
        DynamicVars dynamicVars = yaml.loadAs(baselineDO.getBaselineConfYaml(), DynamicVars.class);
        project.setProjConfJson(JsonUtils.toJsonPrettyString(dynamicVars));
        Set<String> allTags = moduleDOS.stream().map(ModuleDO::getTag).collect(Collectors.toSet());
        allTags.add("init");
        allTags.add("precheck");
        allTags.add("postcheck");
        allTags.add("all");
        Set<String> midwareTags = moduleDOS.stream().map(moduleDO -> {
            if (StringUtils.isNotEmpty(moduleDO.getMidwareTags())) {
                return Arrays.asList(moduleDO.getMidwareTags().split(","));
            }
            return null;
        }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet());
        allTags.addAll(midwareTags);
        project.setAllProjTags(String.join(",", allTags));

    }

    public void batchSaveProjectConf(Long projectId, Long baselineId, List<Integer> types) {
        ProjectDO projectDO = projectMapper.selectById(projectId);
        if (StringUtils.isEmpty(projectDO.getAllProjTags())) {
            return;
        }

        List<String> allTags = new ArrayList<>(Arrays.asList(projectDO.getAllProjTags().split(",")));
        //根据基线生成项目配置
        List<ProjectConfDO> baselineConfDOS = projectConfMapper.selectList(new LambdaQueryWrapperX<ProjectConfDO>()
                .eqIfPresent(ProjectConfDO::getProjectId, -1)
                .eqIfPresent(ProjectConfDO::getBaselineId, baselineId)
                .inIfPresent(ProjectConfDO::getType, types)
                .inIfPresent(ProjectConfDO::getTag, allTags)
        );

        LambdaQueryWrapperX<ProjectConfDO> queryWrapperX = new LambdaQueryWrapperX<ProjectConfDO>()
                .in(ProjectConfDO::getType, types)
                .eq(ProjectConfDO::getProjectId, projectId);
        List<ProjectConfDO> projectConfDOS = projectConfMapper.selectList(queryWrapperX);

        Map<String, String> existConfMap = new HashMap<>();
        projectConfDOS.forEach(projectConfDO -> {
            String tag = projectConfDO.getTag();
            String confKey = projectConfDO.getConfKey();
            String confValue = projectConfDO.getConfValue();
            existConfMap.put(tag + ":" + confKey, confValue);
        });
        if (CollectionUtils.isNotEmpty(baselineConfDOS)) {
            List<ProjectConfDO> collect = baselineConfDOS.stream()
                    .filter(projectConfDO -> {
                        if (projectConfDO.getModifyFlag()) {
                            List<String> customMidwareTags = new ArrayList<>();
                            if (null != projectDO.getMidwareCustomTags()) {
                                customMidwareTags.addAll(Arrays.asList(projectDO.getMidwareCustomTags().split(",")));
                            }
                            List<String> confTagFilters = Arrays.asList(projectConfDO.getTagFilter().split(","));
                            // 有自定义的中间件，需要过滤中间件配置
                            if (customMidwareTags.contains(projectConfDO.getTag())) {
                                List<String> newTagFilters = new ArrayList<>();
                                newTagFilters.add("custom");
                                newTagFilters.addAll(allTags);
                                return newTagFilters.containsAll(confTagFilters);
                            }
                            return allTags.containsAll(confTagFilters);
                        }
                        return false;
                    })
                    .map(baselineConfDO -> {
                                String confValue = existConfMap.get(baselineConfDO.getTag() + ":" + baselineConfDO.getConfKey());
                                ProjectConfDO build = ProjectConfDO.builder()
                                        .projectId(projectId)
                                        .baselineId(baselineId)
                                        .tag(baselineConfDO.getTag())
                                        .tagFilter(baselineConfDO.getTagFilter())
                                        .modifyFlag(baselineConfDO.getModifyFlag())
                                        .confKey(baselineConfDO.getConfKey())
                                        .keyDesc(baselineConfDO.getKeyDesc())
                                        .confValue(confValue == null ? baselineConfDO.getConfValue() : confValue)
                                        .type(baselineConfDO.getType())
                                        .version(baselineConfDO.getVersion())
                                        .build();
                                return build;
                            }
                    ).collect(Collectors.toList());
            projectConfMapper.delete(queryWrapperX);
            projectConfMapper.insertBatch(collect);
        }
    }

    private void genServers(Long projectId) {
        ProjectDO projectDO = projectMapper.selectById(projectId);
        if (null != projectDO) {
            String allProjTags = projectDO.getAllProjTags();
            if (StringUtils.isNotEmpty(allProjTags)) {
                List<String> tags = Arrays.asList(allProjTags.split(","));
                List<ModuleDO> moduleDOS = moduleMapper.selectList(new LambdaQueryWrapperX<ModuleDO>()
                        .inIfPresent(ModuleDO::getTag, tags)
                        .inIfPresent(ModuleDO::getModuleType, Arrays.asList(DeployStep.DEPLOY_MID.getValue(), DeployStep.DEPLOY_APP.getValue())));
                Map<String, GenServerStrategy> strategyMap = GenServerStrategy.serverStrategyMap();
                List<ServerDO> servers = new ArrayList<>();
                AtomicInteger ipStart = new AtomicInteger(1);
                moduleDOS.forEach(moduleDO -> {
                    //环境服务器
                    GenServerStrategy strategy = strategyMap.get(moduleDO.getTag());
                    Integer minNum = strategy.getMinNum();
                    Integer clusterNum = strategy.getClusterNum();
                    for (int i = 0; i < minNum; i++) {
                        String serverSuffix = i + 1 + "";
                        if (i == 0) {
                            serverSuffix += "__主要";
                        }
                        servers.add(ServerDO.builder()
                                .baselineId(projectDO.getBaselineId())
                                .projectId(projectId)
                                .tag(moduleDO.getTag())
                                .name("测试环境_" + moduleDO.getName() + "_" + serverSuffix)
                                .ip("10.88.88." + ipStart.getAndIncrement())
                                .cpu(strategy.getMinCpu())
                                .memory(strategy.getMinMemory())
                                .envType(EnvType.STAGING.getValue())
                                .build());
                    }
                    for (int i = 0; i < clusterNum; i++) {
                        String serverSuffix = i + 1 + "";
                        if (i == 0) {
                            serverSuffix += "__主要";
                        }
                        servers.add(ServerDO.builder()
                                .baselineId(projectDO.getBaselineId())
                                .projectId(projectId)
                                .tag(moduleDO.getTag())
                                .name("生产环境_" + moduleDO.getName() + "_" + serverSuffix)
                                .ip("10.88.88." + ipStart.getAndIncrement())
                                .cpu(strategy.getMinCpu())
                                .memory(strategy.getMinMemory())
                                .envType(EnvType.PROD.getValue())
                                .build());
                    }
                });
                serverMapper.insertBatch(servers);
                // 生产测试环境服务进程关系
                servers.forEach(serverDO -> {
                    String tag = serverDO.getTag();
                    List<ProcessDO> processDOS = processMapper.selectList(new LambdaQueryWrapperX<ProcessDO>()
                            .inIfPresent(ProcessDO::getProcessType, Arrays.asList(DeployStep.DEPLOY_MID.getValue(), DeployStep.DEPLOY_APP.getValue()))
                            .eqIfPresent(ProcessDO::getTag, tag)
                    );
                    List<ProcessDO> filterProcess = processDOS.stream().filter(ProcessDO::getIsHa).collect(Collectors.toList());

                    List<ServerProcessDO> serverProcessDOS = new ArrayList<>();
                    if (serverDO.getName().contains("__主要")) {
                        processDOS.forEach(processDO -> {
                            serverProcessDOS.add(ServerProcessDO.builder()
                                    .serverId(serverDO.getId())
                                    .processId(processDO.getId())
                                    .build());
                        });
                    } else {
                        filterProcess.forEach(processDO -> {
                            serverProcessDOS.add(ServerProcessDO.builder()
                                    .serverId(serverDO.getId())
                                    .processId(processDO.getId())
                                    .build());
                        });
                    }
                    serverProcessMapper.insertBatch(serverProcessDOS);
                });
            }
        }
    }

    @Override
    public ProjectProcessRespVo getProjectProcess(Long projectId) {
        ProjectDO projectDO = projectMapper.selectById(projectId);
        if (null != projectDO) {
            List<String> allProjTags = Arrays.asList(projectDO.getAllProjTags().split(","));
            List<ProcessDO> processDOS = processMapper.selectList(new LambdaQueryWrapperX<ProcessDO>()
                    .inIfPresent(ProcessDO::getTag, allProjTags)
                    .eqIfPresent(ProcessDO::getBaselineId, projectDO.getBaselineId()));
            Map<Integer, List<ProcessDO>> processMap = processDOS.stream().filter(processDO -> {
                List<String> confTagFilters = Arrays.asList(processDO.getTagFilter().split(","));
                return allProjTags.containsAll(confTagFilters);
            }).collect(Collectors.groupingBy(ProcessDO::getProcessType, Collectors.toList()));
            //3,2,4,1
            List<ProcessDO> processDOS1 = processMap.get(DeployStep.CHECK_AND_INIT_ENV.getValue());
            List<ProcessDO> processDOS2 = processMap.get(DeployStep.DEPLOY_MID.getValue());
            List<ProcessDO> processDOS3 = processMap.get(DeployStep.INIT_MID.getValue());
            List<ProcessDO> processDOS4 = processMap.get(DeployStep.DEPLOY_APP.getValue());
            return ProjectProcessRespVo.builder()
                    .initEnv(ProjectProcessRespVo.ProjectProcess.builder().name("检查&初始化环境").processes(processDOS1).build())
                    .deployMidware(ProjectProcessRespVo.ProjectProcess.builder().name("部署中间件").processes(processDOS2).build())
                    .initMidware(ProjectProcessRespVo.ProjectProcess.builder().name("初始化中间件").processes(processDOS3).build())
                    .deployApp(ProjectProcessRespVo.ProjectProcess.builder().name("部署应用").processes(processDOS4).build())
                    .build();
        }
        return ProjectProcessRespVo.builder().build();
    }

    @Override
    public void updateProject(ProjectUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateProjectExists(updateReqVO.getId());
        // 更新
        ProjectDO updateObj = ProjectConvert.INSTANCE.convert(updateReqVO);
        List<ModuleDO> moduleDOS = moduleMapper.selectBatchIds(updateReqVO.getModuleIds());
        wrapperProject(updateObj, moduleDOS);
        projectMapper.updateById(updateObj);
        // 更新项目模块关系
        projectModuleService.createProjectModule(updateReqVO.getId(), moduleDOS);
        // 生成项目配置信息
//        batchSaveProjectConf(updateObj.getId(),updateObj.getBaselineId());
    }

    @Override
    public void deleteProject(Long id) {
        // 校验存在
        this.validateProjectExists(id);
        // 删除
        projectMapper.deleteById(id);
        // 删除项目的关联关系
        // 1. 配置文件
        Map<String, Object> delParams = new HashMap<>();
        delParams.put("project_id", id);
        projectConfMapper.deleteByMap(delParams);
        // 服务器进程关系
        List<ServerDO> serverDOS = serverMapper.selectList("project_id", id);
        if (CollectionUtils.isNotEmpty(serverDOS)) {
            serverDOS.forEach(serverDO -> {
                Map<String, Object> delProcessParams = new HashMap<>();
                delProcessParams.put("server_id", serverDO.getId());
                serverProcessMapper.deleteByMap(delProcessParams);
            });
        }
        // 项目服务器关系
        serverMapper.deleteByMap(delParams);
        // 项目模块关系
        projectModuleMapper.deleteByMap(delParams);
    }

    private void validateProjectExists(Long id) {
        if (projectMapper.selectById(id) == null) {
            throw exception(PROJECT_NOT_EXISTS);
        }
    }

    @Override
    public ProjectDO getProject(Long id) {
        return projectMapper.selectById(id);
    }

    @Override
    public List<ProjectDO> getProjectList(Collection<Long> ids) {
        return projectMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectDO> getProjectPage(ProjectPageReqVO pageReqVO) {
        return projectMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProjectDO> getProjectList(ProjectExportReqVO exportReqVO) {
        return projectMapper.selectList(exportReqVO);
    }

    @Override
    @Transactional
    public void genServers(GenServersReqVO genServersReqVO) {
        Long currentProjectId = genServersReqVO.getProjectId();
        // 获取当前项目下的模块信息
        projectModuleMapper.selectCount(new LambdaQueryWrapperX<ProjectModuleDO>().eqIfPresent(ProjectModuleDO::getProjectId, currentProjectId));
        List<ModuleRespVO> modules = projectModuleService.getModulesByProjectId(currentProjectId);
        if (CollectionUtils.isEmpty(modules)) {
            throw exception(PROJECT_EXTEND_NO_MODULE_ERROR);
        }
        // 获取当前项目下的服务器
        List<ServerDO> currentServerDOs = serverMapper.selectList("project_id", currentProjectId);
        if (CollectionUtils.isNotEmpty(currentServerDOs)) {
            // 删除当前项目下的服务器信息，以及服务器关联的进程信息
            Set<Long> serverIds = currentServerDOs.stream().map(ServerDO::getId).collect(Collectors.toSet());
            serverMapper.deleteBatchIds(serverIds);
            Map<String, Object> deleteParams = new HashMap<>();
            serverIds.forEach(serverId -> {
                deleteParams.put("server_id", serverId);
                serverProcessMapper.deleteByMap(deleteParams);
            });
        }
        genServers(currentProjectId);
    }

    @Override
    @Transactional
    public void mergerServer(MergeServerReqVO mergeServerReqVO) {
        ServerDO build = ServerDO.builder()
                .remark(mergeServerReqVO.getRemark())
                .projectId(mergeServerReqVO.getProjectId())
                .cpu(mergeServerReqVO.getCpu())
                .ip(mergeServerReqVO.getIp())
                .memory(mergeServerReqVO.getMemory())
                .name(mergeServerReqVO.getName())
                .build();
        int insert = serverMapper.insert(build);
        if (insert > 0) {
            List<Long> mergeServers = mergeServerReqVO.getMergeServers();
            List<ServerProcessDO> serverProcessDOS = serverProcessMapper.selectList("server_id", mergeServers);
            if (CollectionUtils.isNotEmpty(serverProcessDOS)) {
                List<ServerProcessDO> collect = serverProcessDOS.stream()
                        .distinct()
                        .map(serverProcessDO -> ServerProcessDO.builder()
                                .serverId(build.getId())
                                .processId(serverProcessDO.getProcessId())
                                .build())
                        .collect(Collectors.toList());
                serverProcessMapper.insertBatch(collect);
                // 删除服务器以及其关联进程
                serverMapper.deleteBatchIds(mergeServers);
                mergeServers.forEach(me -> {
                    Map<String, Object> delParams = new HashMap<>();
                    delParams.put("server_id", me);
                    serverProcessMapper.deleteByMap(delParams);
                });
            }
        }
    }

    @Override
    public void syncBaselineConf(ProjectUpdateReqVO updateReqVO) {
        batchSaveProjectConf(updateReqVO.getId(), updateReqVO.getBaselineId(), updateReqVO.getTypes());
    }

    private Map<String, BaseVars> assignConf(List<ProjectConfDO> projConfList, String tag) {
        Map<String, BaseVars> baseVarsMap = new HashMap<>();
        BaseVars baseVars = new BaseVars();
        Map<String, Object> vars = new HashMap<>();
        Map<String, String> imageTags = new HashMap<>();
        projConfList.forEach(projConf -> {
            if (DeployStep.SYNC_MID_IMAGES.getValue().equals(projConf.getType())
                    || DeployStep.SYNC_MOD_IMAGES.getValue().equals(projConf.getType())) {
                imageTags.put(projConf.getConfKey(), projConf.getConfValue());
                return;
            }
            Object confValue = projConf.getConfValue();
            if (JSONUtil.isTypeJSONArray(confValue.toString())) {
                confValue = JsonUtils.parseObject(confValue.toString(), Object.class);
            }
            if ("true".equals(confValue) || "false".equals(confValue)) {
                confValue = Boolean.valueOf(confValue.toString());
            }
            vars.put(projConf.getConfKey(), confValue);
        });
        if (MapUtils.isNotEmpty(vars)) {
            baseVars.setVars(vars);
        }
        if (MapUtils.isNotEmpty(imageTags)) {
            baseVars.setImage_tags(imageTags);
        }
        if (MapUtils.isNotEmpty(vars) || MapUtils.isNotEmpty(imageTags)) {
            baseVarsMap.put(tag, baseVars);
            return baseVarsMap;
        }
        return null;
    }

    @Override
    public ProjectConfRespVO showBaselineConf(ProjectUpdateReqVO updateReqVO) {
        // 更新项目配置
        List<ProjectConfDO> projectConfDOS = projectConfMapper.selectList(new LambdaQueryWrapperX<ProjectConfDO>()
                .eqIfPresent(ProjectConfDO::getProjectId, updateReqVO.getId()));
        if (CollectionUtils.isNotEmpty(projectConfDOS)) {
            Map<String, List<ProjectConfDO>> projConfMap = projectConfDOS.stream().collect(Collectors.groupingBy(projectConfDO -> {
                Integer confType = projectConfDO.getType();
                if (DeployStep.SYNC_MID_IMAGES.getValue().equals(projectConfDO.getType())) {
                    confType = DeployStep.DEPLOY_MID.getValue();
                } else if (DeployStep.SYNC_MOD_IMAGES.getValue().equals(projectConfDO.getType())) {
                    confType = DeployStep.DEPLOY_APP.getValue();
                }
                return confType + "_" + projectConfDO.getTag();
            }));
            DynamicVars dynamicVars = new DynamicVars();
            projConfMap.forEach((type_tag, projConfList) -> {
                String[] arr = type_tag.split("_");
                DeployStep deployStep = DeployStep.getByValue(Integer.valueOf(arr[0]));
                Map<String, BaseVars> baseVarsMap = assignConf(projConfList, arr[1]);
                if (null == baseVarsMap) {
                    return;
                }
                switch (deployStep) {
                    case CHECK_AND_INIT_ENV:
                        if (MapUtils.isEmpty(dynamicVars.getEnv())) {
                            dynamicVars.setEnv(baseVarsMap);
                        } else {
                            dynamicVars.getEnv().putAll(baseVarsMap);
                        }
                        break;
                    case PLAN_MID:
                        if (MapUtils.isEmpty(dynamicVars.getMidwares_plan())) {
                            dynamicVars.setMidwares_plan(baseVarsMap);
                        } else {
                            dynamicVars.getMidwares_plan().putAll(baseVarsMap);
                        }
                        break;
                    case DEPLOY_APP:
                        if (MapUtils.isEmpty(dynamicVars.getModels())) {
                            dynamicVars.setModels(baseVarsMap);
                        } else {
                            dynamicVars.getModels().putAll(baseVarsMap);
                        }
                        break;
                    case DEPLOY_MID:
                        if (MapUtils.isEmpty(dynamicVars.getMidwares())) {
                            dynamicVars.setMidwares(baseVarsMap);
                        } else {
                            dynamicVars.getMidwares().putAll(baseVarsMap);
                        }
                        break;
                    case INIT_MID:
                        if (MapUtils.isEmpty(dynamicVars.getMidwares_init())) {
                            dynamicVars.setMidwares_init(baseVarsMap);
                        } else {
                            dynamicVars.getMidwares_init().putAll(baseVarsMap);
                        }
                        break;
                    case INIT_APP:
                    case CHECK_APP:
                    default:
                        break;

                }
            });
            ProjectConfRespVO confRespVO = new ProjectConfRespVO();
            String projConfJson = JsonUtils.toJsonPrettyString(dynamicVars);
            Object projConfYamlObj = JsonUtils.parseObject(projConfJson, Object.class);
            DumperOptions options = new DumperOptions();
            options.setIndentWithIndicator(true);
            options.setIndicatorIndent(2);
            Yaml yaml = new Yaml(options);
            String projConfYaml = yaml.dumpAsMap(projConfYamlObj);
            ProjectDO build = ProjectDO.builder()
                    .projConfJson(projConfJson)
                    .id(updateReqVO.getId())
                    .projConfYaml(projConfYaml)
                    .build();
            projectMapper.updateById(build);
            confRespVO.setProjConfJson(projConfJson);
            confRespVO.setProjConfYaml(projConfYaml);
            confRespVO.setAnsibleHosts(genAnsibleHosts(updateReqVO));
            return confRespVO;
        }

        return null;
    }

    private String genAnsibleHosts(ProjectUpdateReqVO updateReqVO) {
        Long projectId = updateReqVO.getId();
        List<ServerDO> serverDOS = serverMapper.selectList(new LambdaQueryWrapperX<ServerDO>()
                .eqIfPresent(ServerDO::getProjectId, projectId)
        );
        Set<Long> serverIds = serverDOS.stream().map(ServerDO::getId).collect(Collectors.toSet());
        Set<String> allHosts = serverDOS.stream().map(ServerDO::getIp).collect(Collectors.toSet());
        List<ServerProcessDO> serverProcessDOS = serverProcessMapper.selectList(new LambdaQueryWrapperX<ServerProcessDO>()
                .inIfPresent(ServerProcessDO::getServerId, serverIds)
        );

        Set<Long> processIds = serverProcessDOS.stream().map(ServerProcessDO::getProcessId).collect(Collectors.toSet());

        List<ProcessDO> processDOS = processMapper.selectList(new LambdaQueryWrapperX<ProcessDO>()
                .inIfPresent(ProcessDO::getId, processIds)
        );

        Map<Long, Set<Long>> processServerMap = serverProcessDOS.stream().collect(Collectors.groupingBy(ServerProcessDO::getProcessId, Collectors.mapping(ServerProcessDO::getServerId, Collectors.toSet())));

        Map<String, Object> ansibleHosts = new LinkedHashMap<>();
        Map<String, Object> allVal = new LinkedHashMap<>();
        Map<String, Object> varsVal = new LinkedHashMap<>();
        Map<String, Object> childrenVal = new LinkedHashMap<>();
        Map<String, Object> allNodesHosts = new LinkedHashMap<>();
        Map<String, Object> allNodesHostsVal = new LinkedHashMap<>();
        allNodesHosts.put("hosts", allNodesHostsVal);
        childrenVal.put("all_nodes", allNodesHosts);
        allVal.put("children", childrenVal);
        allVal.put("vars", varsVal);
        ansibleHosts.put("all", allVal);
        allHosts.forEach(host -> {
            allNodesHostsVal.put(host, null);
        });
        varsVal.put("ansible_ssh_user", "root");
        processServerMap.forEach((processId, servers) -> {
            ProcessDO processDO = processDOS.stream().filter(s -> s.getId().equals(processId)).findFirst().orElse(null);
            if (null != processDO) {
                String name = processDO.getName(); // shared_mysql
                if (StringUtils.isEmpty(name)) {
                    return;
                }
                name = name.replace("-", "_");
                Map<String, Object> nameHosts = new LinkedHashMap<>();
                Map<String, Object> nameHostsVal = new LinkedHashMap<>();
                List<String> ips = serverDOS.stream().filter(s -> servers.contains(s.getId())).map(ServerDO::getIp).collect(Collectors.toList());// shared_mysql00x
                for (int i = 0; i < ips.size(); i++) {
                    Map<String, Object> nameHostsNVal = new LinkedHashMap<>();
                    nameHostsNVal.put("ansible_ssh_host", ips.get(i));
                    nameHostsNVal.put("ansible_public_host", ips.get(i));
                    nameHostsVal.put(name + "00" + (i + 1), nameHostsNVal);
                }
                nameHosts.put("hosts", nameHostsVal);
                childrenVal.put(name, nameHosts);
            }
        });

        DumperOptions options = new DumperOptions();
        options.setIndentWithIndicator(true);
        options.setIndicatorIndent(2);
        Representer representer = new RepresenterNull2Empty();
        Yaml yaml = new Yaml(representer, options);
        return yaml.dumpAsMap(ansibleHosts);
    }

    @Override
    public void updateProjectServer(ProjectServerUpdateReqVO updateReqVO) {
        Long serverId = updateReqVO.getServerId();
        Set<Long> processIds = updateReqVO.getProcessIds();
        if (null == serverId || CollectionUtils.isEmpty(processIds)) {
            throw exception(PROJECT_SERVER_UPDATE_ERROR);
        }
        Map<String, Object> delParams = new HashMap<>();
        delParams.put("server_id", serverId);
        // 删除进程关系
        serverProcessMapper.deleteByMap(delParams);
        List<ServerProcessDO> serverProcessDOS = new ArrayList<>();
        processIds.forEach(processId -> {
            ServerProcessDO build = ServerProcessDO.builder().processId(processId).serverId(serverId).build();
            serverProcessDOS.add(build);
        });
        serverProcessMapper.insertBatch(serverProcessDOS);
    }

    @Override
    public void deleteProjectServer(Long serverId) {
        if (null == serverId) {
            throw exception(PROJECT_SERVER_UPDATE_ERROR);
        }
        // 删除服务器
        serverMapper.deleteById(serverId);
        // 删除服务器进程关系
        Map<String, Object> delParams = new HashMap<>();
        delParams.put("server_id", serverId);
        serverProcessMapper.deleteByMap(delParams);
    }

    private void assignConfValue(ProjectConfDO projectConfDO, LinkedHashMap ymlEntry) {
        String separator = ".";
        String confKey = projectConfDO.getConfKey();
        String confValue = projectConfDO.getConfValue();
        if (StringUtils.isNotEmpty(confKey)) {
            if (!confKey.contains(separator)) {
                Object o = ymlEntry.get(confKey);
                if (o instanceof Collection) {
                    List<String> values = JsonUtils.parseArray(confValue, String.class);
                    ymlEntry.put(confKey, values);
                } else {
                    ymlEntry.put(confKey, confValue);
                }
            } else {
                String[] keys = confKey.split("\\.");
                LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(keys));
                String finalKey = linkedList.removeLast();
                Iterator<String> iterator = linkedList.iterator();
                LinkedHashMap map = ymlEntry;
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    map = (LinkedHashMap) map.get(key);
                }
                if (null != map) {
                    Object finalValue = map.get(finalKey);
                    if (finalValue instanceof Collection) {
                        List<Object> values = JsonUtils.parseArray(confValue, Object.class);
                        map.put(finalKey, values);
                    } else {
                        map.put(finalKey, confValue);
                    }
                }
            }
        }
    }

}
