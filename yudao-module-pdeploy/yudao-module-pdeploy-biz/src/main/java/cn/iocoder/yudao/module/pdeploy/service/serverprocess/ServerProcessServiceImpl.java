package cn.iocoder.yudao.module.pdeploy.service.serverprocess;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.serverprocess.ServerProcessConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.serverprocess.ServerProcessMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 服务器进程关系 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ServerProcessServiceImpl implements ServerProcessService {

    @Resource
    private ServerProcessMapper serverProcessMapper;
    @Resource
    private ProcessMapper processMapper;

    @Override
    public Long createServerProcess(ServerProcessCreateReqVO createReqVO) {
        // 插入
        ServerProcessDO serverProcess = ServerProcessConvert.INSTANCE.convert(createReqVO);
        serverProcessMapper.insert(serverProcess);
        // 返回
        return serverProcess.getId();
    }

    @Override
    public void updateServerProcess(ServerProcessUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateServerProcessExists(updateReqVO.getId());
        // 更新
        ServerProcessDO updateObj = ServerProcessConvert.INSTANCE.convert(updateReqVO);
        serverProcessMapper.updateById(updateObj);
    }

    @Override
    public void deleteServerProcess(Long id) {
        // 校验存在
        this.validateServerProcessExists(id);
        // 删除
        serverProcessMapper.deleteById(id);
    }

    private void validateServerProcessExists(Long id) {
        if (serverProcessMapper.selectById(id) == null) {
            throw exception(SERVER_PROCESS_NOT_EXISTS);
        }
    }

    @Override
    public ServerProcessDO getServerProcess(Long id) {
        return serverProcessMapper.selectById(id);
    }

    @Override
    public List<ServerProcessDO> getServerProcessList(Collection<Long> ids) {
        return serverProcessMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ServerProcessDO> getServerProcessPage(ServerProcessPageReqVO pageReqVO) {
        return serverProcessMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ServerProcessDO> getServerProcessList(ServerProcessExportReqVO exportReqVO) {
        return serverProcessMapper.selectList(exportReqVO);
    }

    @Override
    public void createServerProcess(Long serverId, Set<Long> processIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("server_id", serverId);
        serverProcessMapper.deleteByMap(params);
        if (CollectionUtils.isNotEmpty(processIds)) {
            List<ServerProcessDO> serverProcessDOS = new ArrayList<>();
            processIds.forEach(processId -> {
                ServerProcessDO serverProcessDO = new ServerProcessDO();
                serverProcessDO.setProcessId(processId);
                serverProcessDO.setServerId(serverId);
                serverProcessDOS.add(serverProcessDO);
            });
            serverProcessMapper.insertBatch(serverProcessDOS);
        }
    }

    @Override
    public Set<Long> getProcessIdsByServerId(Long serverId) {
        List<ServerProcessDO> serverProcessDOS = serverProcessMapper.selectList("server_id", serverId);
        if (CollectionUtils.isNotEmpty(serverProcessDOS)) {
            return serverProcessDOS.stream().map(ServerProcessDO::getProcessId).collect(Collectors.toSet());
        }
        return Sets.newHashSet();
    }

    @Override
    public List<ProcessDO> getProcessesByServerId(Long serverId) {
        List<ServerProcessDO> serverProcessDOS = serverProcessMapper.selectList("server_id", serverId);
        if (CollectionUtils.isNotEmpty(serverProcessDOS)) {
            Set<Long> processIds = serverProcessDOS.stream().map(ServerProcessDO::getProcessId).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(processIds)){
                return processMapper.selectList("id", processIds);
            }
        }
        return Lists.newArrayList();
    }

}
