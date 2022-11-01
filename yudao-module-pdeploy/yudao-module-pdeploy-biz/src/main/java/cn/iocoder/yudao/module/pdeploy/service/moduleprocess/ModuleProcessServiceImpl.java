package cn.iocoder.yudao.module.pdeploy.service.moduleprocess;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.moduleprocess.ModuleProcessConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.moduleprocess.ModuleProcessMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 模块进程关系 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ModuleProcessServiceImpl implements ModuleProcessService {

    @Resource
    private ModuleProcessMapper moduleProcessMapper;
    @Resource
    private ProcessMapper processMapper;

    @Override
    public Long createModuleProcess(ModuleProcessCreateReqVO createReqVO) {
        // 插入
        ModuleProcessDO moduleProcess = ModuleProcessConvert.INSTANCE.convert(createReqVO);
        moduleProcessMapper.insert(moduleProcess);
        // 返回
        return moduleProcess.getId();
    }

    @Override
    public void updateModuleProcess(ModuleProcessUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateModuleProcessExists(updateReqVO.getId());
        // 更新
        ModuleProcessDO updateObj = ModuleProcessConvert.INSTANCE.convert(updateReqVO);
        moduleProcessMapper.updateById(updateObj);
    }

    @Override
    public void deleteModuleProcess(Long id) {
        // 校验存在
        this.validateModuleProcessExists(id);
        // 删除
        moduleProcessMapper.deleteById(id);
    }

    private void validateModuleProcessExists(Long id) {
        if (moduleProcessMapper.selectById(id) == null) {
            throw exception(MODULE_PROCESS_NOT_EXISTS);
        }
    }

    @Override
    public ModuleProcessDO getModuleProcess(Long id) {
        return moduleProcessMapper.selectById(id);
    }

    @Override
    public List<ModuleProcessDO> getModuleProcessList(Collection<Long> ids) {
        return moduleProcessMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ModuleProcessDO> getModuleProcessPage(ModuleProcessPageReqVO pageReqVO) {
        return moduleProcessMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ModuleProcessDO> getModuleProcessList(ModuleProcessExportReqVO exportReqVO) {
        return moduleProcessMapper.selectList(exportReqVO);
    }

    @Override
    public void createModuleProcess(Long moduleId, Set<Long> processIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("module_id", moduleId);
        moduleProcessMapper.deleteByMap(params);
        if (CollectionUtils.isNotEmpty(processIds)) {
            List<ModuleProcessDO> moduleProcessDOS = new ArrayList<>();
            processIds.forEach(processId -> {
                ModuleProcessDO moduleProcessDO = ModuleProcessDO.builder().processId(processId).moduleId(moduleId).build();
                moduleProcessDOS.add(moduleProcessDO);
            });
            moduleProcessMapper.insertBatch(moduleProcessDOS);
        }
    }

    @Override
    public Set<Long> getProcessIdsByModuleId(Long id) {
        List<ModuleProcessDO> moduleProcessDOS = moduleProcessMapper.selectList("module_id", id);
        if (CollectionUtils.isNotEmpty(moduleProcessDOS)) {
            return moduleProcessDOS.stream().map(ModuleProcessDO::getProcessId).collect(Collectors.toSet());
        }
        return Sets.newHashSet();
    }

    @Override
    public List<ProcessDO> getProcessesByModuleId(Long id) {
        List<ModuleProcessDO> moduleProcessDOS = moduleProcessMapper.selectList("module_id", id);
        if (CollectionUtils.isNotEmpty(moduleProcessDOS)) {
            Set<Long> processIds = moduleProcessDOS.stream().map(ModuleProcessDO::getProcessId).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(processIds)){
                return processMapper.selectList("id",processIds);
            }
        }
        return Lists.newArrayList();
    }

}
