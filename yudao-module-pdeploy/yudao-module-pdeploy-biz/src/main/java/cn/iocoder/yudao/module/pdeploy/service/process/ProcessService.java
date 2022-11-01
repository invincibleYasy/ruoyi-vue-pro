package cn.iocoder.yudao.module.pdeploy.service.process;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 进程 Service 接口
 *
 * @author 芋道源码
 */
public interface ProcessService {

    /**
     * 创建进程
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProcess(@Valid ProcessCreateReqVO createReqVO);

    /**
     * 更新进程
     *
     * @param updateReqVO 更新信息
     */
    void updateProcess(@Valid ProcessUpdateReqVO updateReqVO);

    /**
     * 删除进程
     *
     * @param id 编号
     */
    void deleteProcess(Long id);

    /**
     * 获得进程
     *
     * @param id 编号
     * @return 进程
     */
    ProcessDO getProcess(Long id);

    /**
     * 获得进程列表
     *
     * @param ids 编号
     * @return 进程列表
     */
    List<ProcessDO> getProcessList(Collection<Long> ids);

    /**
     * 获得进程分页
     *
     * @param pageReqVO 分页查询
     * @return 进程分页
     */
    PageResult<ProcessDO> getProcessPage(ProcessPageReqVO pageReqVO);

    /**
     * 获得进程列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 进程列表
     */
    List<ProcessDO> getProcessList(ProcessExportReqVO exportReqVO);

}
