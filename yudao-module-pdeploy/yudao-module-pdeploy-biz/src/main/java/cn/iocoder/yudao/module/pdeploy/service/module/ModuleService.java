package cn.iocoder.yudao.module.pdeploy.service.module;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 模块 Service 接口
 *
 * @author 芋道源码
 */
public interface ModuleService {

    /**
     * 创建模块
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createModule(@Valid ModuleCreateReqVO createReqVO);

    /**
     * 更新模块
     *
     * @param updateReqVO 更新信息
     */
    void updateModule(@Valid ModuleUpdateReqVO updateReqVO);

    /**
     * 删除模块
     *
     * @param id 编号
     */
    void deleteModule(Long id);

    /**
     * 获得模块
     *
     * @param id 编号
     * @return 模块
     */
    ModuleDO getModule(Long id);

    /**
     * 获得模块列表
     *
     * @param ids 编号
     * @return 模块列表
     */
    List<ModuleDO> getModuleList(Collection<Long> ids);

    /**
     * 获得模块分页
     *
     * @param pageReqVO 分页查询
     * @return 模块分页
     */
    PageResult<ModuleDO> getModulePage(ModulePageReqVO pageReqVO);

    /**
     * 获得模块列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 模块列表
     */
    List<ModuleDO> getModuleList(ModuleExportReqVO exportReqVO);

}
