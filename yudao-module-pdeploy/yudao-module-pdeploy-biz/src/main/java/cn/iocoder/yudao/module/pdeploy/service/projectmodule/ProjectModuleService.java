package cn.iocoder.yudao.module.pdeploy.service.projectmodule;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.ModuleRespVO;
import cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 项目模块关系 Service 接口
 *
 * @author 芋道源码
 */
public interface ProjectModuleService {

    /**
     * 创建项目模块关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProjectModule(@Valid ProjectModuleCreateReqVO createReqVO);

    /**
     * 更新项目模块关系
     *
     * @param updateReqVO 更新信息
     */
    void updateProjectModule(@Valid ProjectModuleUpdateReqVO updateReqVO);

    /**
     * 删除项目模块关系
     *
     * @param id 编号
     */
    void deleteProjectModule(Long id);

    /**
     * 获得项目模块关系
     *
     * @param id 编号
     * @return 项目模块关系
     */
    ProjectModuleDO getProjectModule(Long id);

    /**
     * 获得项目模块关系列表
     *
     * @param ids 编号
     * @return 项目模块关系列表
     */
    List<ProjectModuleDO> getProjectModuleList(Collection<Long> ids);

    /**
     * 获得项目模块关系分页
     *
     * @param pageReqVO 分页查询
     * @return 项目模块关系分页
     */
    PageResult<ProjectModuleDO> getProjectModulePage(ProjectModulePageReqVO pageReqVO);

    /**
     * 获得项目模块关系列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 项目模块关系列表
     */
    List<ProjectModuleDO> getProjectModuleList(ProjectModuleExportReqVO exportReqVO);

    void createProjectModule(Long projectId, List<ModuleDO> moduleDOS);

    Set<Long> getModuleIdsByProjectId(Long projectId);

    List<ModuleRespVO> getModulesByProjectId(Long projectId);
}
