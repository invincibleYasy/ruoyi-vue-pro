package cn.iocoder.yudao.module.pdeploy.service.serverprocess;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 服务器进程关系 Service 接口
 *
 * @author 芋道源码
 */
public interface ServerProcessService {

    /**
     * 创建服务器进程关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createServerProcess(@Valid ServerProcessCreateReqVO createReqVO);

    /**
     * 更新服务器进程关系
     *
     * @param updateReqVO 更新信息
     */
    void updateServerProcess(@Valid ServerProcessUpdateReqVO updateReqVO);

    /**
     * 删除服务器进程关系
     *
     * @param id 编号
     */
    void deleteServerProcess(Long id);

    /**
     * 获得服务器进程关系
     *
     * @param id 编号
     * @return 服务器进程关系
     */
    ServerProcessDO getServerProcess(Long id);

    /**
     * 获得服务器进程关系列表
     *
     * @param ids 编号
     * @return 服务器进程关系列表
     */
    List<ServerProcessDO> getServerProcessList(Collection<Long> ids);

    /**
     * 获得服务器进程关系分页
     *
     * @param pageReqVO 分页查询
     * @return 服务器进程关系分页
     */
    PageResult<ServerProcessDO> getServerProcessPage(ServerProcessPageReqVO pageReqVO);

    /**
     * 获得服务器进程关系列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 服务器进程关系列表
     */
    List<ServerProcessDO> getServerProcessList(ServerProcessExportReqVO exportReqVO);

    void createServerProcess(Long serverId, Set<Long> processIds);

    Set<Long> getProcessIdsByServerId(Long serverId);

    List<ProcessDO> getProcessesByServerId(Long serverId);

}
