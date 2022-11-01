package cn.iocoder.yudao.module.pdeploy.dal.mysql.server;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.*;

/**
 * 服务器 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerMapper extends BaseMapperX<ServerDO> {

    default PageResult<ServerDO> selectPage(ServerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ServerDO>()
                .eqIfPresent(ServerDO::getBaselineId, reqVO.getBaselineId())
                .eqIfPresent(ServerDO::getProjectId, reqVO.getProjectId())
                .likeIfPresent(ServerDO::getName, reqVO.getName())
                .eqIfPresent(ServerDO::getIp, reqVO.getIp())
                .eqIfPresent(ServerDO::getCpu, reqVO.getCpu())
                .eqIfPresent(ServerDO::getMemory, reqVO.getMemory())
                .betweenIfPresent(ServerDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ServerDO::getRemark, reqVO.getRemark())
                .orderByDesc(ServerDO::getId));
    }

    default List<ServerDO> selectList(ServerExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ServerDO>()
                .eqIfPresent(ServerDO::getBaselineId, reqVO.getBaselineId())
                .eqIfPresent(ServerDO::getProjectId, reqVO.getProjectId())
                .likeIfPresent(ServerDO::getName, reqVO.getName())
                .eqIfPresent(ServerDO::getIp, reqVO.getIp())
                .eqIfPresent(ServerDO::getCpu, reqVO.getCpu())
                .eqIfPresent(ServerDO::getMemory, reqVO.getMemory())
                .betweenIfPresent(ServerDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ServerDO::getRemark, reqVO.getRemark())
                .orderByDesc(ServerDO::getId));
    }

}
