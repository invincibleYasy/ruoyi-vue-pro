package cn.iocoder.yudao.module.pdeploy.convert.projectconf;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;

/**
 * 项目配置 Convert
 *
 * @author 管理员
 */
@Mapper
public interface ProjectConfConvert {

    ProjectConfConvert INSTANCE = Mappers.getMapper(ProjectConfConvert.class);

    ProjectConfDO convert(ProjectConfCreateReqVO bean);

    ProjectConfDO convert(ProjectConfUpdateReqVO bean);

    ProjectConfRespVO convert(ProjectConfDO bean);

    List<ProjectConfRespVO> convertList(List<ProjectConfDO> list);

    PageResult<ProjectConfRespVO> convertPage(PageResult<ProjectConfDO> page);

    List<ProjectConfExcelVO> convertList02(List<ProjectConfDO> list);

}
