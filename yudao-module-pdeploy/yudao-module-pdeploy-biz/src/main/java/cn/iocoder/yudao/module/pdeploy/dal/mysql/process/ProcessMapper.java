package cn.iocoder.yudao.module.pdeploy.dal.mysql.process;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo.*;

/**
 * 进程 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProcessMapper extends BaseMapperX<ProcessDO> {

    default PageResult<ProcessDO> selectPage(ProcessPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProcessDO>()
                .eqIfPresent(ProcessDO::getBaselineId, reqVO.getBaselineId())
                .eqIfPresent(ProcessDO::getCodebaseId, reqVO.getCodebaseId())
                .likeIfPresent(ProcessDO::getName, reqVO.getName())
                .eqIfPresent(ProcessDO::getVersion, reqVO.getVersion())
                .eqIfPresent(ProcessDO::getProcessType, reqVO.getProcessType())
                .eqIfPresent(ProcessDO::getCpu, reqVO.getCpu())
                .eqIfPresent(ProcessDO::getMemory, reqVO.getMemory())
                .eqIfPresent(ProcessDO::getIsHa, reqVO.getIsHa())
                .betweenIfPresent(ProcessDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ProcessDO::getRemark, reqVO.getRemark())
                .orderByDesc(ProcessDO::getId));
    }

    default List<ProcessDO> selectList(ProcessExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProcessDO>()
                .eqIfPresent(ProcessDO::getBaselineId, reqVO.getBaselineId())
                .eqIfPresent(ProcessDO::getCodebaseId, reqVO.getCodebaseId())
                .likeIfPresent(ProcessDO::getName, reqVO.getName())
                .eqIfPresent(ProcessDO::getVersion, reqVO.getVersion())
                .eqIfPresent(ProcessDO::getProcessType, reqVO.getProcessType())
                .eqIfPresent(ProcessDO::getCpu, reqVO.getCpu())
                .eqIfPresent(ProcessDO::getMemory, reqVO.getMemory())
                .eqIfPresent(ProcessDO::getIsHa, reqVO.getIsHa())
                .betweenIfPresent(ProcessDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ProcessDO::getRemark, reqVO.getRemark())
                .orderByDesc(ProcessDO::getId));
    }

}
