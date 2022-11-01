package cn.iocoder.yudao.module.pdeploy.service.module;

import cn.iocoder.yudao.module.pdeploy.service.moduleprocess.ModuleProcessService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.module.ModuleConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.module.ModuleMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 模块 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private ModuleProcessService moduleProcessService;

    @Override
    public Long createModule(ModuleCreateReqVO createReqVO) {
        // 插入
        ModuleDO module = ModuleConvert.INSTANCE.convert(createReqVO);
        moduleMapper.insert(module);
        // 返回
        // 处理模块进程关系
        moduleProcessService.createModuleProcess(module.getId(),createReqVO.getProcessIds());
        return module.getId();
    }

    @Override
    public void updateModule(ModuleUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateModuleExists(updateReqVO.getId());
        // 更新
        ModuleDO updateObj = ModuleConvert.INSTANCE.convert(updateReqVO);
        // 处理模块进程关系
        moduleProcessService.createModuleProcess(updateReqVO.getId(),updateReqVO.getProcessIds());
        moduleMapper.updateById(updateObj);
    }

    @Override
    public void deleteModule(Long id) {
        // 校验存在
        this.validateModuleExists(id);
        // 删除
        moduleMapper.deleteById(id);
    }

    private void validateModuleExists(Long id) {
        if (moduleMapper.selectById(id) == null) {
            throw exception(MODULE_NOT_EXISTS);
        }
    }

    @Override
    public ModuleDO getModule(Long id) {
        return moduleMapper.selectById(id);
    }

    @Override
    public List<ModuleDO> getModuleList(Collection<Long> ids) {
        return moduleMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ModuleDO> getModulePage(ModulePageReqVO pageReqVO) {
        return moduleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ModuleDO> getModuleList(ModuleExportReqVO exportReqVO) {
        return moduleMapper.selectList(exportReqVO);
    }

}
