package cn.iocoder.yudao.module.pdeploy.convert.codebase;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.codebase.CodebaseDO;

/**
 * 代码库 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface CodebaseConvert {

    CodebaseConvert INSTANCE = Mappers.getMapper(CodebaseConvert.class);

    CodebaseDO convert(CodebaseCreateReqVO bean);

    CodebaseDO convert(CodebaseUpdateReqVO bean);

    CodebaseRespVO convert(CodebaseDO bean);

    List<CodebaseRespVO> convertList(List<CodebaseDO> list);

    PageResult<CodebaseRespVO> convertPage(PageResult<CodebaseDO> page);

    List<CodebaseExcelVO> convertList02(List<CodebaseDO> list);

}
