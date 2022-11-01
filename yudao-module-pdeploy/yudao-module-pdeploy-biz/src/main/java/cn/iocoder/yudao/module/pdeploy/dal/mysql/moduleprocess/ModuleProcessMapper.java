package cn.iocoder.yudao.module.pdeploy.dal.mysql.moduleprocess;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess.vo.*;

/**
 * 模块进程关系 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ModuleProcessMapper extends BaseMapperX<ModuleProcessDO> {

    default PageResult<ModuleProcessDO> selectPage(ModuleProcessPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ModuleProcessDO>()
                .eqIfPresent(ModuleProcessDO::getModuleId, reqVO.getModuleId())
                .eqIfPresent(ModuleProcessDO::getProcessId, reqVO.getProcessId())
                .betweenIfPresent(ModuleProcessDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ModuleProcessDO::getRemark, reqVO.getRemark())
                .orderByDesc(ModuleProcessDO::getId));
    }

    default List<ModuleProcessDO> selectList(ModuleProcessExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ModuleProcessDO>()
                .eqIfPresent(ModuleProcessDO::getModuleId, reqVO.getModuleId())
                .eqIfPresent(ModuleProcessDO::getProcessId, reqVO.getProcessId())
                .betweenIfPresent(ModuleProcessDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(ModuleProcessDO::getRemark, reqVO.getRemark())
                .orderByDesc(ModuleProcessDO::getId));
    }

}
