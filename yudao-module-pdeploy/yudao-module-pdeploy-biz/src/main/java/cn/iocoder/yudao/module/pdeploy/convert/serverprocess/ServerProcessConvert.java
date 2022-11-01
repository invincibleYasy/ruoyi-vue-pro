package cn.iocoder.yudao.module.pdeploy.convert.serverprocess;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;

/**
 * 服务器进程关系 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerProcessConvert {

    ServerProcessConvert INSTANCE = Mappers.getMapper(ServerProcessConvert.class);

    ServerProcessDO convert(ServerProcessCreateReqVO bean);

    ServerProcessDO convert(ServerProcessUpdateReqVO bean);

    ServerProcessRespVO convert(ServerProcessDO bean);

    List<ServerProcessRespVO> convertList(List<ServerProcessDO> list);

    PageResult<ServerProcessRespVO> convertPage(PageResult<ServerProcessDO> page);

    List<ServerProcessExcelVO> convertList02(List<ServerProcessDO> list);

}
