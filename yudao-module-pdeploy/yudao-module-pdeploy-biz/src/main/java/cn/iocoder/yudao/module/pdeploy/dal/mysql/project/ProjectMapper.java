package cn.iocoder.yudao.module.pdeploy.dal.mysql.project;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.*;

/**
 * 私有项目 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProjectMapper extends BaseMapperX<ProjectDO> {

    default PageResult<ProjectDO> selectPage(ProjectPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectDO>()
                .eqIfPresent(ProjectDO::getBaselineId, reqVO.getBaselineId())
                .likeIfPresent(ProjectDO::getName, reqVO.getName())
                .betweenIfPresent(ProjectDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ProjectDO::getRemark, reqVO.getRemark())
                .orderByDesc(ProjectDO::getId));
    }

    default List<ProjectDO> selectList(ProjectExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectDO>()
                .eqIfPresent(ProjectDO::getBaselineId, reqVO.getBaselineId())
                .likeIfPresent(ProjectDO::getName, reqVO.getName())
                .betweenIfPresent(ProjectDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ProjectDO::getRemark, reqVO.getRemark())
                .orderByDesc(ProjectDO::getId));
    }

}
