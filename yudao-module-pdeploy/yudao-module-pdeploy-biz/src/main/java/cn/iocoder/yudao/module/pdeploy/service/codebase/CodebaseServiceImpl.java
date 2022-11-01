package cn.iocoder.yudao.module.pdeploy.service.codebase;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.codebase.CodebaseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.codebase.CodebaseConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.codebase.CodebaseMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 代码库 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CodebaseServiceImpl implements CodebaseService {

    @Resource
    private CodebaseMapper codebaseMapper;

    @Override
    public Long createCodebase(CodebaseCreateReqVO createReqVO) {
        // 插入
        CodebaseDO codebase = CodebaseConvert.INSTANCE.convert(createReqVO);
        codebaseMapper.insert(codebase);
        // 返回
        return codebase.getId();
    }

    @Override
    public void updateCodebase(CodebaseUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateCodebaseExists(updateReqVO.getId());
        // 更新
        CodebaseDO updateObj = CodebaseConvert.INSTANCE.convert(updateReqVO);
        codebaseMapper.updateById(updateObj);
    }

    @Override
    public void deleteCodebase(Long id) {
        // 校验存在
        this.validateCodebaseExists(id);
        // 删除
        codebaseMapper.deleteById(id);
    }

    private void validateCodebaseExists(Long id) {
        if (codebaseMapper.selectById(id) == null) {
            throw exception(CODEBASE_NOT_EXISTS);
        }
    }

    @Override
    public CodebaseDO getCodebase(Long id) {
        return codebaseMapper.selectById(id);
    }

    @Override
    public List<CodebaseDO> getCodebaseList(Collection<Long> ids) {
        return codebaseMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CodebaseDO> getCodebasePage(CodebasePageReqVO pageReqVO) {
        return codebaseMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CodebaseDO> getCodebaseList(CodebaseExportReqVO exportReqVO) {
        return codebaseMapper.selectList(exportReqVO);
    }

}
