package cn.iocoder.yudao.module.pdeploy.convert.server;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;

/**
 * 服务器 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerConvert {

    ServerConvert INSTANCE = Mappers.getMapper(ServerConvert.class);

    ServerDO convert(ServerCreateReqVO bean);

    ServerDO convert(ServerUpdateReqVO bean);

    ServerRespVO convert(ServerDO bean);

    List<ServerRespVO> convertList(List<ServerDO> list);

    PageResult<ServerRespVO> convertPage(PageResult<ServerDO> page);

    List<ServerExcelVO> convertList02(List<ServerDO> list);

}
