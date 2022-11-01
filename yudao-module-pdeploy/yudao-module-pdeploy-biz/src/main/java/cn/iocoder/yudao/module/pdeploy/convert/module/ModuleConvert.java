package cn.iocoder.yudao.module.pdeploy.convert.module;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;

/**
 * 模块 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ModuleConvert {

    ModuleConvert INSTANCE = Mappers.getMapper(ModuleConvert.class);

    ModuleDO convert(ModuleCreateReqVO bean);

    ModuleDO convert(ModuleUpdateReqVO bean);

    ModuleRespVO convert(ModuleDO bean);

    List<ModuleRespVO> convertList(List<ModuleDO> list);

    PageResult<ModuleRespVO> convertPage(PageResult<ModuleDO> page);

    List<ModuleExcelVO> convertList02(List<ModuleDO> list);

}
