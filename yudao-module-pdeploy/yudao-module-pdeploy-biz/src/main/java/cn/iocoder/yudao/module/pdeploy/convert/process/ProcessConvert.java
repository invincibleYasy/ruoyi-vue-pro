package cn.iocoder.yudao.module.pdeploy.convert.process;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;

/**
 * 进程 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ProcessConvert {

    ProcessConvert INSTANCE = Mappers.getMapper(ProcessConvert.class);

    ProcessDO convert(ProcessCreateReqVO bean);

    ProcessDO convert(ProcessUpdateReqVO bean);

    ProcessRespVO convert(ProcessDO bean);

    List<ProcessRespVO> convertList(List<ProcessDO> list);

    PageResult<ProcessRespVO> convertPage(PageResult<ProcessDO> page);

    List<ProcessExcelVO> convertList02(List<ProcessDO> list);

}
