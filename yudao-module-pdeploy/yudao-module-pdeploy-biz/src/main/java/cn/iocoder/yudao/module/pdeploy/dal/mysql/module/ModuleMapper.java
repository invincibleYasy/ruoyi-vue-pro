package cn.iocoder.yudao.module.pdeploy.dal.mysql.module;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.*;

/**
 * 模块 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ModuleMapper extends BaseMapperX<ModuleDO> {

    default PageResult<ModuleDO> selectPage(ModulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ModuleDO>()
                .eqIfPresent(ModuleDO::getBaselineId, reqVO.getBaselineId())
                .likeIfPresent(ModuleDO::getName, reqVO.getName())
                .eqIfPresent(ModuleDO::getModuleType, reqVO.getModuleType())
                .betweenIfPresent(ModuleDO::getEstimatedTime, reqVO.getBeginEstimatedTime(), reqVO.getEndEstimatedTime())
                .betweenIfPresent(ModuleDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ModuleDO::getRemark, reqVO.getRemark())
                .orderByDesc(ModuleDO::getId));
    }

    default List<ModuleDO> selectList(ModuleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ModuleDO>()
                .eqIfPresent(ModuleDO::getBaselineId, reqVO.getBaselineId())
                .likeIfPresent(ModuleDO::getName, reqVO.getName())
                .eqIfPresent(ModuleDO::getModuleType, reqVO.getModuleType())
                .betweenIfPresent(ModuleDO::getEstimatedTime, reqVO.getBeginEstimatedTime(), reqVO.getEndEstimatedTime())
                .betweenIfPresent(ModuleDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ModuleDO::getRemark, reqVO.getRemark())
                .orderByDesc(ModuleDO::getId));
    }

}
