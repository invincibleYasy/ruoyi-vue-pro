package cn.iocoder.yudao.module.pdeploy.convert.baseline;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;

/**
 * 基线版本 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BaselineConvert {

    BaselineConvert INSTANCE = Mappers.getMapper(BaselineConvert.class);

    BaselineDO convert(BaselineCreateReqVO bean);

    BaselineDO convert(BaselineUpdateReqVO bean);

    BaselineRespVO convert(BaselineDO bean);

    List<BaselineRespVO> convertList(List<BaselineDO> list);

    PageResult<BaselineRespVO> convertPage(PageResult<BaselineDO> page);

    List<BaselineExcelVO> convertList02(List<BaselineDO> list);

}
