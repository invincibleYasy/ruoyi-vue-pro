package cn.iocoder.yudao.module.pdeploy.service.codebase;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.codebase.CodebaseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 代码库 Service 接口
 *
 * @author 芋道源码
 */
public interface CodebaseService {

    /**
     * 创建代码库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCodebase(@Valid CodebaseCreateReqVO createReqVO);

    /**
     * 更新代码库
     *
     * @param updateReqVO 更新信息
     */
    void updateCodebase(@Valid CodebaseUpdateReqVO updateReqVO);

    /**
     * 删除代码库
     *
     * @param id 编号
     */
    void deleteCodebase(Long id);

    /**
     * 获得代码库
     *
     * @param id 编号
     * @return 代码库
     */
    CodebaseDO getCodebase(Long id);

    /**
     * 获得代码库列表
     *
     * @param ids 编号
     * @return 代码库列表
     */
    List<CodebaseDO> getCodebaseList(Collection<Long> ids);

    /**
     * 获得代码库分页
     *
     * @param pageReqVO 分页查询
     * @return 代码库分页
     */
    PageResult<CodebaseDO> getCodebasePage(CodebasePageReqVO pageReqVO);

    /**
     * 获得代码库列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 代码库列表
     */
    List<CodebaseDO> getCodebaseList(CodebaseExportReqVO exportReqVO);

}
