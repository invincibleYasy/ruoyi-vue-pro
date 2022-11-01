package cn.iocoder.yudao.module.pdeploy.service.server;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.ProjectExtendReqVO;
import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.ProjectExtendRespVO;
import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 服务器 Service 接口
 *
 * @author 芋道源码
 */
public interface ServerService {

    /**
     * 创建服务器
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createServer(@Valid ServerCreateReqVO createReqVO);

    /**
     * 更新服务器
     *
     * @param updateReqVO 更新信息
     */
    void updateServer(@Valid ServerUpdateReqVO updateReqVO);

    /**
     * 删除服务器
     *
     * @param id 编号
     */
    void deleteServer(Long id);

    /**
     * 获得服务器
     *
     * @param id 编号
     * @return 服务器
     */
    ServerDO getServer(Long id);

    /**
     * 获得服务器列表
     *
     * @param ids 编号
     * @return 服务器列表
     */
    List<ServerDO> getServerList(Collection<Long> ids);

    /**
     * 获得服务器分页
     *
     * @param pageReqVO 分页查询
     * @return 服务器分页
     */
    PageResult<ServerDO> getServerPage(ServerPageReqVO pageReqVO);

    /**
     * 获得服务器列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 服务器列表
     */
    List<ServerDO> getServerList(ServerExportReqVO exportReqVO);

    List<ServerRespVO> getServersByProjectId(Long projectId);

}
