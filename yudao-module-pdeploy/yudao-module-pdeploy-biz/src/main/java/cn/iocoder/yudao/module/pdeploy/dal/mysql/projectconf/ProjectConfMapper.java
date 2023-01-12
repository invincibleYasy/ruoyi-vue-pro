package cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo.*;

/**
 * 项目配置 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ProjectConfMapper extends BaseMapperX<ProjectConfDO> {

    default PageResult<ProjectConfDO> selectPage(ProjectConfPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectConfDO>()
                .eqIfPresent(ProjectConfDO::getBaselineId, reqVO.getBaselineId())
                .eqIfPresent(ProjectConfDO::getProjectId, reqVO.getProjectId())
                .likeIfPresent(ProjectConfDO::getConfKey, reqVO.getConfKey())
                .likeIfPresent(ProjectConfDO::getConfValue, reqVO.getConfValue())
                .eqIfPresent(ProjectConfDO::getModifyFlag, reqVO.getModifyFlag())
                .eqIfPresent(ProjectConfDO::getType, reqVO.getType())
                .likeIfPresent(ProjectConfDO::getKeyDesc, reqVO.getKeyDesc())
                .eqIfPresent(ProjectConfDO::getVersion, reqVO.getVersion())
                .inIfPresent(ProjectConfDO::getType, reqVO.getTypes())
                .betweenIfPresent(ProjectConfDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProjectConfDO::getId));
    }

    default List<ProjectConfDO> selectList(ProjectConfExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectConfDO>()
                .eqIfPresent(ProjectConfDO::getBaselineId, reqVO.getBaselineId())
                .eqIfPresent(ProjectConfDO::getProjectId, reqVO.getProjectId())
                .likeIfPresent(ProjectConfDO::getConfKey, reqVO.getConfKey())
                .likeIfPresent(ProjectConfDO::getConfValue, reqVO.getConfValue())
                .eqIfPresent(ProjectConfDO::getModifyFlag, reqVO.getModifyFlag())
                .eqIfPresent(ProjectConfDO::getType, reqVO.getType())
                .likeIfPresent(ProjectConfDO::getKeyDesc, reqVO.getKeyDesc())
                .eqIfPresent(ProjectConfDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(ProjectConfDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProjectConfDO::getId));
    }

}
