package cn.iocoder.yudao.module.pdeploy.service.process;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.process.ProcessConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 进程 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProcessServiceImpl implements ProcessService {

    @Resource
    private ProcessMapper processMapper;

    @Override
    public Long createProcess(ProcessCreateReqVO createReqVO) {
        // 插入
        ProcessDO process = ProcessConvert.INSTANCE.convert(createReqVO);
        processMapper.insert(process);
        // 返回
        return process.getId();
    }

    @Override
    public void updateProcess(ProcessUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateProcessExists(updateReqVO.getId());
        // 更新
        if (null == updateReqVO.getCodebaseId()) {
            updateReqVO.setCodebaseId(-1l);
        }
        ProcessDO updateObj = ProcessConvert.INSTANCE.convert(updateReqVO);
        processMapper.updateById(updateObj);
    }

    @Override
    public void deleteProcess(Long id) {
        // 校验存在
        this.validateProcessExists(id);
        // 删除
        processMapper.deleteById(id);
    }

    private void validateProcessExists(Long id) {
        if (processMapper.selectById(id) == null) {
            throw exception(PROCESS_NOT_EXISTS);
        }
    }

    @Override
    public ProcessDO getProcess(Long id) {
        return processMapper.selectById(id);
    }

    @Override
    public List<ProcessDO> getProcessList(Collection<Long> ids) {
        return processMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProcessDO> getProcessPage(ProcessPageReqVO pageReqVO) {
        return processMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProcessDO> getProcessList(ProcessExportReqVO exportReqVO) {
        return processMapper.selectList(exportReqVO);
    }

    @Override
    public List<ProcessDO> getProcessListByTagsAndBaseline(Set<String> tags, Long baselineId) {
        return processMapper.selectList(new LambdaQueryWrapperX<ProcessDO>()
                .inIfPresent(ProcessDO::getTag, tags)
                .eqIfPresent(ProcessDO::getBaselineId, baselineId));
    }

}
