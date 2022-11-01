package cn.iocoder.yudao.module.pdeploy.dal.mysql.baseline;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo.*;

/**
 * 基线版本 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BaselineMapper extends BaseMapperX<BaselineDO> {

    default PageResult<BaselineDO> selectPage(BaselinePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BaselineDO>()
                .likeIfPresent(BaselineDO::getName, reqVO.getName())
                .eqIfPresent(BaselineDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(BaselineDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(BaselineDO::getRemark, reqVO.getRemark())
                .orderByDesc(BaselineDO::getId));
    }

    default List<BaselineDO> selectList(BaselineExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BaselineDO>()
                .likeIfPresent(BaselineDO::getName, reqVO.getName())
                .eqIfPresent(BaselineDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(BaselineDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(BaselineDO::getRemark, reqVO.getRemark())
                .orderByDesc(BaselineDO::getId));
    }

}
