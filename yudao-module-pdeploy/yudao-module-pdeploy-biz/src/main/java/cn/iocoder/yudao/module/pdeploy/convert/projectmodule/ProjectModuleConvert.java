package cn.iocoder.yudao.module.pdeploy.convert.projectmodule;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;

/**
 * 项目模块关系 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ProjectModuleConvert {

    ProjectModuleConvert INSTANCE = Mappers.getMapper(ProjectModuleConvert.class);

    ProjectModuleDO convert(ProjectModuleCreateReqVO bean);

    ProjectModuleDO convert(ProjectModuleUpdateReqVO bean);

    ProjectModuleRespVO convert(ProjectModuleDO bean);

    List<ProjectModuleRespVO> convertList(List<ProjectModuleDO> list);

    PageResult<ProjectModuleRespVO> convertPage(PageResult<ProjectModuleDO> page);

    List<ProjectModuleExcelVO> convertList02(List<ProjectModuleDO> list);

}
