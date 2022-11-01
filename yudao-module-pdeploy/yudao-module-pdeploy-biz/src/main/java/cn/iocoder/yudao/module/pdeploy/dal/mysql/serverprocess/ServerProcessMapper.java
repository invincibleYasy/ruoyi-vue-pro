package cn.iocoder.yudao.module.pdeploy.dal.mysql.serverprocess;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo.*;

/**
 * 服务器进程关系 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerProcessMapper extends BaseMapperX<ServerProcessDO> {

    default PageResult<ServerProcessDO> selectPage(ServerProcessPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ServerProcessDO>()
                .eqIfPresent(ServerProcessDO::getServerId, reqVO.getServerId())
                .eqIfPresent(ServerProcessDO::getProcessId, reqVO.getProcessId())
                .betweenIfPresent(ServerProcessDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .orderByDesc(ServerProcessDO::getId));
    }

    default List<ServerProcessDO> selectList(ServerProcessExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ServerProcessDO>()
                .eqIfPresent(ServerProcessDO::getServerId, reqVO.getServerId())
                .eqIfPresent(ServerProcessDO::getProcessId, reqVO.getProcessId())
                .betweenIfPresent(ServerProcessDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .orderByDesc(ServerProcessDO::getId));
    }

}
