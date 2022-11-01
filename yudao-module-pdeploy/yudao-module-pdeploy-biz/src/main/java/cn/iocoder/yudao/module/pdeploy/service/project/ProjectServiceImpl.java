package cn.iocoder.yudao.module.pdeploy.service.project;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.excel.core.convert.JsonConvert;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.ModuleRespVO;
import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.ServerRespVO;
import cn.iocoder.yudao.module.pdeploy.convert.server.ServerConvert;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.baseline.BaselineMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectmodule.ProjectModuleMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.server.ServerMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.serverprocess.ServerProcessMapper;
import cn.iocoder.yudao.module.pdeploy.service.projectmodule.ProjectModuleService;
import cn.iocoder.yudao.module.pdeploy.service.server.ServerService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
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


    @Override
    @Transactional
    public Long createProject(ProjectCreateReqVO createReqVO) {
        // 插入
        ProjectDO project = ProjectConvert.INSTANCE.convert(createReqVO);
        projectMapper.insert(project);
        // 返回
        //处理关联模块信息
        projectModuleService.createProjectModule(project.getId(), createReqVO.getModuleIds());
        return project.getId();
    }

    private void batchSaveProjectConf(Long projectId, Long baselineId) {
        //根据基线生成项目配置
        List<ProjectConfDO> baselineConfDOS = projectConfMapper.selectList(new LambdaQueryWrapperX<ProjectConfDO>()
                .eqIfPresent(ProjectConfDO::getProjectId, -1)
                .eqIfPresent(ProjectConfDO::getBaselineId, baselineId));

        if (CollectionUtils.isNotEmpty(baselineConfDOS)) {
            List<ProjectConfDO> collect = baselineConfDOS.stream()
                    .filter(ProjectConfDO::getModifyFlag)
                    .map(baselineConfDO -> {
                                ProjectConfDO build = ProjectConfDO.builder()
                                        .projectId(projectId)
                                        .baselineId(baselineId)
                                        .modifyFlag(baselineConfDO.getModifyFlag())
                                        .confKey(baselineConfDO.getConfKey())
                                        .keyDesc(baselineConfDO.getKeyDesc())
                                        .confValue(baselineConfDO.getConfValue())
                                        .type(baselineConfDO.getType())
                                        .version(baselineConfDO.getVersion())
                                        .build();
                                String confValue = baselineConfDO.getConfValue();
                                if (JsonUtils.isJson(confValue)) {
                                    List<Object> values = JsonUtils.parseArray(confValue, Object.class);
                                    List<Object> valueList = new ArrayList<>();
                                    values.forEach(value -> {
                                        valueList.add("基线值-" + value);
                                    });
                                    build.setConfValue(JsonUtils.toJsonString(valueList));
                                } else {
                                    build.setConfValue("基线值-" + confValue);
                                }
                                return build;
                            }
                    ).collect(Collectors.toList());
            Map<String, Object> delParams = new HashMap<>();
            delParams.put("project_id", projectId);
            projectConfMapper.deleteByMap(delParams);
            projectConfMapper.insertBatch(collect);
        }
    }

    @Override
    public void updateProject(ProjectUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateProjectExists(updateReqVO.getId());
        // 更新
        ProjectDO updateObj = ProjectConvert.INSTANCE.convert(updateReqVO);
        projectModuleService.createProjectModule(updateReqVO.getId(), updateReqVO.getModuleIds());
        projectMapper.updateById(updateObj);
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
        batchSaveProjectConf(updateReqVO.getId(), updateReqVO.getBaselineId());
    }

    @Override
    public ProjectConfRespVO showBaselineConf(ProjectUpdateReqVO updateReqVO) {
        Long baselineId = updateReqVO.getBaselineId();
        BaselineDO baselineDO = baselineMapper.selectById(baselineId);
        ProjectConfRespVO projectConfRespVO = new ProjectConfRespVO();
        if (null == baselineDO) {
            return projectConfRespVO;
        }
        String mainConf = baselineDO.getMainConf();
        LinkedHashMap mainConfMap = null;
        Yaml mainConfYaml = new Yaml();
        if (StringUtils.isNotEmpty(mainConf)) {
            mainConfMap = mainConfYaml.loadAs(mainConf, LinkedHashMap.class);
        }
        String mainConfCcpass = baselineDO.getMainConfCcpass();
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
