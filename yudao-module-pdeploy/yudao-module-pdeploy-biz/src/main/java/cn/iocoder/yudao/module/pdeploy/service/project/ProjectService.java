package cn.iocoder.yudao.module.pdeploy.service.project;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 私有项目 Service 接口
 *
 * @author 芋道源码
 */
public interface ProjectService {

    /**
     * 创建私有项目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProject(@Valid ProjectCreateReqVO createReqVO);

    /**
     * 更新私有项目
     *
     * @param updateReqVO 更新信息
     */
    void updateProject(@Valid ProjectUpdateReqVO updateReqVO);

    /**
     * 删除私有项目
     *
     * @param id 编号
     */
    void deleteProject(Long id);

    /**
     * 获得私有项目
     *
     * @param id 编号
     * @return 私有项目
     */
    ProjectDO getProject(Long id);

    /**
     * 获得私有项目列表
     *
     * @param ids 编号
     * @return 私有项目列表
     */
    List<ProjectDO> getProjectList(Collection<Long> ids);

    /**
     * 获得私有项目分页
     *
     * @param pageReqVO 分页查询
     * @return 私有项目分页
     */
    PageResult<ProjectDO> getProjectPage(ProjectPageReqVO pageReqVO);

    /**
     * 获得私有项目列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 私有项目列表
     */
    List<ProjectDO> getProjectList(ProjectExportReqVO exportReqVO);

    ProjectExtendRespVO extendProject(ProjectExtendReqVO extendReqVO);

    void mergerServer(MergeServerReqVO mergeServerReqVO);

    void syncBaselineConf(ProjectUpdateReqVO updateReqVO);

    ProjectConfRespVO showBaselineConf(ProjectUpdateReqVO updateReqVO);

    void updateProjectServer(ProjectServerUpdateReqVO updateReqVO);

    void deleteProjectServer(Long serverId);
}
