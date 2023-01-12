package cn.iocoder.yudao.module.pdeploy.service.project;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.ModuleRespVO;
import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.ServerRespVO;
import cn.iocoder.yudao.module.pdeploy.convert.server.ServerConvert;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.DynamicVars;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.baseline.BaselineMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.module.ModuleMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectmodule.ProjectModuleMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.server.ServerMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.serverprocess.ServerProcessMapper;
import cn.iocoder.yudao.module.pdeploy.service.projectmodule.ProjectModuleService;
import cn.iocoder.yudao.module.pdeploy.service.server.ServerService;
import jdk.nashorn.internal.objects.annotations.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.project.ProjectConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.project.ProjectMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

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
    private ServerService serverService;
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
            List<ProcessDO> processDOS1 = processMap.get(3);
            List<ProcessDO> processDOS2 = processMap.get(2);
            List<ProcessDO> processDOS3 = processMap.get(4);
            List<ProcessDO> processDOS4 = processMap.get(1);
            ProjectProcessRespVo build = ProjectProcessRespVo.builder()
                    .initEnv(ProjectProcessRespVo.ProjectProcess.builder().name("检查&初始化环境").processes(processDOS1).build())
                    .deployMidware(ProjectProcessRespVo.ProjectProcess.builder().name("部署中间件").processes(processDOS2).build())
                    .initMidware(ProjectProcessRespVo.ProjectProcess.builder().name("初始化中间件").processes(processDOS3).build())
                    .deployApp(ProjectProcessRespVo.ProjectProcess.builder().name("部署应用").processes(processDOS4).build())
                    .build();
            return build;
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
    public ProjectExtendRespVO extendProject(ProjectExtendReqVO extendReqVO) {
        Long extendProjectId = extendReqVO.getExtendProjectId();
        Long currentProjectId = extendReqVO.getCurrentProjectId();
        if (null == extendProjectId || null == currentProjectId) {
            throw exception(PROJECT_EXTEND_ERROR);
        }
        // 获取继承项目下的服务器
        List<ServerRespVO> serverRespVOS = serverService.getServersByProjectId(extendProjectId);
        if (CollectionUtils.isEmpty(serverRespVOS)) {
            throw exception(PROJECT_EXTEND_NO_SERVER_ERROR);
        }
        // 获取当前项目下的模块信息
        List<ModuleRespVO> modules = projectModuleService.getModulesByProjectId(currentProjectId);
        if (CollectionUtils.isEmpty(modules)) {
            throw exception(PROJECT_EXTEND_NO_MODULE_ERROR);
        }
        Set<Long> processesIds = modules.stream().map(ModuleRespVO::getProcesses).flatMap(Collection::stream).map(ProcessDO::getId).collect(Collectors.toSet());
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
        //插入继承的服务器和进程信息
        List<ServerRespVO> servers = new ArrayList<>();
        serverRespVOS.forEach(serverRespVO -> {
            List<ProcessDO> processes = serverRespVO.getProcesses();
            if (CollectionUtils.isNotEmpty(processes)) {
                // 进程交集
                List<ProcessDO> collects = processes.stream()
                        .filter(process -> processesIds.contains(process.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collects)) {
                    ServerDO build = ServerDO.builder()
                            .projectId(currentProjectId)
                            .cpu(serverRespVO.getCpu())
                            .memory(serverRespVO.getMemory())
                            .baselineId(serverRespVO.getBaselineId())
                            .name(serverRespVO.getName())
                            .remark(serverRespVO.getRemark())
                            .build();
                    serverMapper.insert(build);
                    List<ServerProcessDO> serverProcessDOS = new ArrayList<>();
                    collects.forEach(collect -> {
                        serverProcessDOS.add(ServerProcessDO.builder().processId(collect.getId()).serverId(build.getId()).build());
                    });
                    serverProcessMapper.insertBatch(serverProcessDOS);
                    ServerRespVO convert = ServerConvert.INSTANCE.convert(build);
                    convert.setProcesses(collects);
                    servers.add(convert);
                }
            }
        });
        ProjectExtendRespVO projectExtendRespVO = new ProjectExtendRespVO();
        projectExtendRespVO.setServers(servers);
        return projectExtendRespVO;

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
        batchSaveProjectConf(updateReqVO.getId(), updateReqVO.getBaselineId(), Collections.singletonList(updateReqVO.getType()));
    }

    @Override
    public ProjectConfRespVO showBaselineConf(ProjectUpdateReqVO updateReqVO) {
        Long baselineId = updateReqVO.getBaselineId();
        BaselineDO baselineDO = baselineMapper.selectById(baselineId);
        ProjectConfRespVO projectConfRespVO = new ProjectConfRespVO();
        if (null == baselineDO) {
            return projectConfRespVO;
        }
        String mainConf = baselineDO.getBaselineConfYaml();
        LinkedHashMap mainConfMap = null;
        Yaml mainConfYaml = new Yaml();
        if (StringUtils.isNotEmpty(mainConf)) {
            mainConfMap = mainConfYaml.loadAs(mainConf, LinkedHashMap.class);
        }
        String mainConfCcpass = baselineDO.getBaselineConfJson();
        LinkedHashMap mainConfCcpassMap = null;
        Yaml mainConfCcpassYaml = new Yaml();
        if (StringUtils.isNotEmpty(mainConfCcpass)) {
            mainConfCcpassMap = mainConfCcpassYaml.loadAs(mainConfCcpass, LinkedHashMap.class);
        }
        // 获取项目可变配置
        List<ProjectConfDO> projectConfDOS = projectConfMapper.selectList(new LambdaQueryWrapperX<ProjectConfDO>()
                .eq(ProjectConfDO::getProjectId, updateReqVO.getId())
                .eq(ProjectConfDO::getBaselineId, baselineId)
                .eq(ProjectConfDO::getModifyFlag, true));

        if (CollectionUtils.isNotEmpty(projectConfDOS)) {
            for (ProjectConfDO projectConfDO : projectConfDOS) {
                Integer type = projectConfDO.getType();
                if (1 == type && null != mainConfMap) {
                    assignConfValue(projectConfDO, mainConfMap);
                    projectConfRespVO.setMainConf(mainConfYaml.dumpAs(mainConfMap, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
                }
                if (2 == type && null != mainConfCcpassMap) {
                    assignConfValue(projectConfDO, mainConfCcpassMap);
                    projectConfRespVO.setCcpassConf(mainConfYaml.dumpAsMap(mainConfCcpassMap));
                }
            }
        }
        return projectConfRespVO;
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
