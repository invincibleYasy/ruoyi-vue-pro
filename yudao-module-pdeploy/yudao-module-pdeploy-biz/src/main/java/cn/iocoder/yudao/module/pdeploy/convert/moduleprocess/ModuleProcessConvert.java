package cn.iocoder.yudao.module.pdeploy.convert.moduleprocess;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;

/**
 * 模块进程关系 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ModuleProcessConvert {

    ModuleProcessConvert INSTANCE = Mappers.getMapper(ModuleProcessConvert.class);

    ModuleProcessDO convert(ModuleProcessCreateReqVO bean);

    ModuleProcessDO convert(ModuleProcessUpdateReqVO bean);

    ModuleProcessRespVO convert(ModuleProcessDO bean);

    List<ModuleProcessRespVO> convertList(List<ModuleProcessDO> list);

    PageResult<ModuleProcessRespVO> convertPage(PageResult<ModuleProcessDO> page);

    List<ModuleProcessExcelVO> convertList02(List<ModuleProcessDO> list);

}
