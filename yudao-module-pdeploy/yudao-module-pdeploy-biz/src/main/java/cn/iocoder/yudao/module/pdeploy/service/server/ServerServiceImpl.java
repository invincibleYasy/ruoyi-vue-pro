package cn.iocoder.yudao.module.pdeploy.service.server;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.service.serverprocess.ServerProcessService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.server.ServerConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.server.ServerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 服务器 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ServerServiceImpl implements ServerService {

    @Resource
    private ServerMapper serverMapper;
    @Resource
    private ServerProcessService serverProcessService;


    @Override
    public Long createServer(ServerCreateReqVO createReqVO) {
        // 插入
        ServerDO server = ServerConvert.INSTANCE.convert(createReqVO);
        serverMapper.insert(server);
        // 处理关联进程数据
        Set<Long> processIds = createReqVO.getProcessIds();
        serverProcessService.createServerProcess(server.getId(), processIds);
        // 返回
        return server.getId();
    }

    @Override
    public void updateServer(ServerUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateServerExists(updateReqVO.getId());
        // 更新
        ServerDO updateObj = ServerConvert.INSTANCE.convert(updateReqVO);
        Set<Long> processIds = updateReqVO.getProcessIds();
        serverProcessService.createServerProcess(updateReqVO.getId(), processIds);
        serverMapper.updateById(updateObj);
    }

    @Override
    public void deleteServer(Long id) {
        // 校验存在
        this.validateServerExists(id);
        // 删除
        serverMapper.deleteById(id);
    }

    private void validateServerExists(Long id) {
        if (serverMapper.selectById(id) == null) {
            throw exception(SERVER_NOT_EXISTS);
        }
    }

    @Override
    public ServerDO getServer(Long id) {
        return serverMapper.selectById(id);
    }

    @Override
    public List<ServerDO> getServerList(Collection<Long> ids) {
        return serverMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ServerDO> getServerPage(ServerPageReqVO pageReqVO) {
        return serverMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ServerDO> getServerList(ServerExportReqVO exportReqVO) {
        return serverMapper.selectList(exportReqVO);
    }

    @Override
    public List<ServerRespVO> getServersByProjectId(Long projectId) {
        List<ServerDO> serverDOS = serverMapper.selectList("project_id", projectId);
        List<ServerRespVO> serverRespVOS = ServerConvert.INSTANCE.convertList(serverDOS);
        if (CollectionUtils.isNotEmpty(serverRespVOS)) {
            serverRespVOS.forEach(serverRespVO -> {
                List<ProcessDO> processes = serverProcessService.getProcessesByServerId(serverRespVO.getId());
                serverRespVO.setProcesses(processes);
            });
        }
        return serverRespVOS;
    }

}
