package cn.iocoder.yudao.module.pdeploy.service.projectconf;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 项目配置 Service 接口
 *
 * @author 管理员
 */
public interface ProjectConfService {

    /**
     * 创建项目配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProjectConf(@Valid ProjectConfCreateReqVO createReqVO);

    /**
     * 更新项目配置
     *
     * @param updateReqVO 更新信息
     */
    void updateProjectConf(@Valid ProjectConfUpdateReqVO updateReqVO);

    /**
     * 删除项目配置
     *
     * @param id 编号
     */
    void deleteProjectConf(Long id);

    /**
     * 获得项目配置
     *
     * @param id 编号
     * @return 项目配置
     */
    ProjectConfDO getProjectConf(Long id);

    /**
     * 获得项目配置列表
     *
     * @param ids 编号
     * @return 项目配置列表
     */
    List<ProjectConfDO> getProjectConfList(Collection<Long> ids);

    /**
     * 获得项目配置分页
     *
     * @param pageReqVO 分页查询
     * @return 项目配置分页
     */
    PageResult<ProjectConfDO> getProjectConfPage(ProjectConfPageReqVO pageReqVO);

    /**
     * 获得项目配置列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 项目配置列表
     */
    List<ProjectConfDO> getProjectConfList(ProjectConfExportReqVO exportReqVO);

}
