package cn.iocoder.yudao.module.pdeploy.dal.mysql.codebase;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.codebase.CodebaseDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo.*;

/**
 * 代码库 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CodebaseMapper extends BaseMapperX<CodebaseDO> {

    default PageResult<CodebaseDO> selectPage(CodebasePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CodebaseDO>()
                .likeIfPresent(CodebaseDO::getName, reqVO.getName())
                .eqIfPresent(CodebaseDO::getGitUrl, reqVO.getGitUrl())
                .betweenIfPresent(CodebaseDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(CodebaseDO::getRemark, reqVO.getRemark())
                .orderByDesc(CodebaseDO::getId));
    }

    default List<CodebaseDO> selectList(CodebaseExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CodebaseDO>()
                .likeIfPresent(CodebaseDO::getName, reqVO.getName())
                .eqIfPresent(CodebaseDO::getGitUrl, reqVO.getGitUrl())
                .betweenIfPresent(CodebaseDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(CodebaseDO::getRemark, reqVO.getRemark())
                .orderByDesc(CodebaseDO::getId));
    }

}
