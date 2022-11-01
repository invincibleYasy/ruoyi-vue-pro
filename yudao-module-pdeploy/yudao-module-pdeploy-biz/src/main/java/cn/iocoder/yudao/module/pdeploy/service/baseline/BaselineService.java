package cn.iocoder.yudao.module.pdeploy.service.baseline;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 基线版本 Service 接口
 *
 * @author 芋道源码
 */
public interface BaselineService {

    /**
     * 创建基线版本
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBaseline(@Valid BaselineCreateReqVO createReqVO);

    /**
     * 更新基线版本
     *
     * @param updateReqVO 更新信息
     */
    void updateBaseline(@Valid BaselineUpdateReqVO updateReqVO);

    /**
     * 删除基线版本
     *
     * @param id 编号
     */
    void deleteBaseline(Long id);

    /**
     * 获得基线版本
     *
     * @param id 编号
     * @return 基线版本
     */
    BaselineDO getBaseline(Long id);

    /**
     * 获得基线版本列表
     *
     * @param ids 编号
     * @return 基线版本列表
     */
    List<BaselineDO> getBaselineList(Collection<Long> ids);

    /**
     * 获得基线版本分页
     *
     * @param pageReqVO 分页查询
     * @return 基线版本分页
     */
    PageResult<BaselineDO> getBaselinePage(BaselinePageReqVO pageReqVO);

    /**
     * 获得基线版本列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 基线版本列表
     */
    List<BaselineDO> getBaselineList(BaselineExportReqVO exportReqVO);

}
