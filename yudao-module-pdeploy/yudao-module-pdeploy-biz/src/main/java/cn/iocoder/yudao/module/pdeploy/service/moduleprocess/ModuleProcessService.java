package cn.iocoder.yudao.module.pdeploy.service.moduleprocess;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;

/**
 * 模块进程关系 Service 接口
 *
 * @author 芋道源码
 */
public interface ModuleProcessService {

    /**
     * 创建模块进程关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createModuleProcess(@Valid ModuleProcessCreateReqVO createReqVO);

    /**
     * 更新模块进程关系
     *
     * @param updateReqVO 更新信息
     */
    void updateModuleProcess(@Valid ModuleProcessUpdateReqVO updateReqVO);

    /**
     * 删除模块进程关系
     *
     * @param id 编号
     */
    void deleteModuleProcess(Long id);

    /**
     * 获得模块进程关系
     *
     * @param id 编号
     * @return 模块进程关系
     */
    ModuleProcessDO getModuleProcess(Long id);

    /**
     * 获得模块进程关系列表
     *
     * @param ids 编号
     * @return 模块进程关系列表
     */
    List<ModuleProcessDO> getModuleProcessList(Collection<Long> ids);

    /**
     * 获得模块进程关系分页
     *
     * @param pageReqVO 分页查询
     * @return 模块进程关系分页
     */
    PageResult<ModuleProcessDO> getModuleProcessPage(ModuleProcessPageReqVO pageReqVO);

    /**
     * 获得模块进程关系列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 模块进程关系列表
     */
    List<ModuleProcessDO> getModuleProcessList(ModuleProcessExportReqVO exportReqVO);

    void createModuleProcess(Long moduleId, Set<Long> processIds);

    Set<Long> getProcessIdsByModuleId(Long id);

    List<ProcessDO> getProcessesByModuleId(Long id);
}
