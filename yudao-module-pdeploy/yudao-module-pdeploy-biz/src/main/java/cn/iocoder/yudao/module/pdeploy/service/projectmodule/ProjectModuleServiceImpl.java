package cn.iocoder.yudao.module.pdeploy.service.projectmodule;

import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.ModuleRespVO;
import cn.iocoder.yudao.module.pdeploy.convert.module.ModuleConvert;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.module.ModuleMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.moduleprocess.ModuleProcessMapper;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;
import cn.iocoder.yudao.module.pdeploy.service.moduleprocess.ModuleProcessService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.projectmodule.ProjectModuleConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectmodule.ProjectModuleMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 项目模块关系 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProjectModuleServiceImpl implements ProjectModuleService {

    @Resource
    private ProjectModuleMapper projectModuleMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private ProcessMapper processMapper;
    @Resource
    private ModuleProcessService moduleProcessService;

    @Override
    public Long createProjectModule(ProjectModuleCreateReqVO createReqVO) {
        // 插入
        ProjectModuleDO projectModule = ProjectModuleConvert.INSTANCE.convert(createReqVO);
        projectModuleMapper.insert(projectModule);
        // 返回
        return projectModule.getId();
    }

    @Override
    public void updateProjectModule(ProjectModuleUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateProjectModuleExists(updateReqVO.getId());
        // 更新
        ProjectModuleDO updateObj = ProjectModuleConvert.INSTANCE.convert(updateReqVO);
        projectModuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteProjectModule(Long id) {
        // 校验存在
        this.validateProjectModuleExists(id);
        // 删除
        projectModuleMapper.deleteById(id);
    }

    private void validateProjectModuleExists(Long id) {
        if (projectModuleMapper.selectById(id) == null) {
            throw exception(PROJECT_MODULE_NOT_EXISTS);
        }
    }

    @Override
    public ProjectModuleDO getProjectModule(Long id) {
        return projectModuleMapper.selectById(id);
    }

    @Override
    public List<ProjectModuleDO> getProjectModuleList(Collection<Long> ids) {
        return projectModuleMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectModuleDO> getProjectModulePage(ProjectModulePageReqVO pageReqVO) {
        return projectModuleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProjectModuleDO> getProjectModuleList(ProjectModuleExportReqVO exportReqVO) {
        return projectModuleMapper.selectList(exportReqVO);
    }

    @Override
    public void createProjectModule(Long projectId, Set<Long> moduleIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("project_id", projectId);
        projectModuleMapper.deleteByMap(params);
        if (CollectionUtils.isNotEmpty(moduleIds)) {
            List<ProjectModuleDO> serverProcessDOS = new ArrayList<>();
            moduleIds.forEach(moduleId -> {
                ProjectModuleDO projectModuleDO = new ProjectModuleDO();
                projectModuleDO.setProjectId(projectId);
                projectModuleDO.setModuleId(moduleId);
                serverProcessDOS.add(projectModuleDO);
            });
            projectModuleMapper.insertBatch(serverProcessDOS);
        }
    }

    @Override
    public Set<Long> getModuleIdsByProjectId(Long projectId) {
        List<ProjectModuleDO> projectModuleDOS = projectModuleMapper.selectList("project_id", projectId);
        if (CollectionUtils.isNotEmpty(projectModuleDOS)) {
            return projectModuleDOS.stream().map(ProjectModuleDO::getModuleId).collect(Collectors.toSet());
        }
        return Sets.newHashSet();
    }

    @Override
    public List<ModuleRespVO> getModulesByProjectId(Long projectId) {
        List<ProjectModuleDO> projectModuleDOS = projectModuleMapper.selectList("project_id", projectId);
        if (CollectionUtils.isNotEmpty(projectModuleDOS)) {
            Set<Long> moduleIds = projectModuleDOS.stream().map(ProjectModuleDO::getModuleId).collect(Collectors.toSet());
            List<ModuleDO> moduleDOS = moduleMapper.selectList("id", moduleIds);
            List<ModuleRespVO> moduleRespVOS = ModuleConvert.INSTANCE.convertList(moduleDOS);
            moduleRespVOS.forEach(moduleRespVO -> {
                List<ProcessDO> processes = moduleProcessService.getProcessesByModuleId(moduleRespVO.getId());
                moduleRespVO.setProcesses(processes);
            });
            return moduleRespVOS;
        }
        return Lists.newArrayList();
    }

}
