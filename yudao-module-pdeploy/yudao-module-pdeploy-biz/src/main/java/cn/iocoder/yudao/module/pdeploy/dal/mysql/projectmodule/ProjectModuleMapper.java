package cn.iocoder.yudao.module.pdeploy.dal.mysql.projectmodule;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo.*;

/**
 * 项目模块关系 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProjectModuleMapper extends BaseMapperX<ProjectModuleDO> {

    default PageResult<ProjectModuleDO> selectPage(ProjectModulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectModuleDO>()
                .eqIfPresent(ProjectModuleDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ProjectModuleDO::getModuleId, reqVO.getModuleId())
                .betweenIfPresent(ProjectModuleDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .orderByDesc(ProjectModuleDO::getId));
    }

    default List<ProjectModuleDO> selectList(ProjectModuleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectModuleDO>()
                .eqIfPresent(ProjectModuleDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ProjectModuleDO::getModuleId, reqVO.getModuleId())
                .betweenIfPresent(ProjectModuleDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .orderByDesc(ProjectModuleDO::getId));
    }

}
