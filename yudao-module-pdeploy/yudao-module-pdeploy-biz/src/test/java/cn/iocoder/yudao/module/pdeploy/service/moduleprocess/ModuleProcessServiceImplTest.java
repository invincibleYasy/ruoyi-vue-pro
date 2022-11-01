package cn.iocoder.yudao.module.pdeploy.service.moduleprocess;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.moduleprocess.ModuleProcessMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link ModuleProcessServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(ModuleProcessServiceImpl.class)
public class ModuleProcessServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ModuleProcessServiceImpl moduleProcessService;

    @Resource
    private ModuleProcessMapper moduleProcessMapper;

    @Test
    public void testCreateModuleProcess_success() {
        // 准备参数
        ModuleProcessCreateReqVO reqVO = randomPojo(ModuleProcessCreateReqVO.class);

        // 调用
        Long moduleProcessId = moduleProcessService.createModuleProcess(reqVO);
        // 断言
        assertNotNull(moduleProcessId);
        // 校验记录的属性是否正确
        ModuleProcessDO moduleProcess = moduleProcessMapper.selectById(moduleProcessId);
        assertPojoEquals(reqVO, moduleProcess);
    }

    @Test
    public void testUpdateModuleProcess_success() {
        // mock 数据
        ModuleProcessDO dbModuleProcess = randomPojo(ModuleProcessDO.class);
        moduleProcessMapper.insert(dbModuleProcess);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ModuleProcessUpdateReqVO reqVO = randomPojo(ModuleProcessUpdateReqVO.class, o -> {
            o.setId(dbModuleProcess.getId()); // 设置更新的 ID
        });

        // 调用
        moduleProcessService.updateModuleProcess(reqVO);
        // 校验是否更新正确
        ModuleProcessDO moduleProcess = moduleProcessMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, moduleProcess);
    }

    @Test
    public void testUpdateModuleProcess_notExists() {
        // 准备参数
        ModuleProcessUpdateReqVO reqVO = randomPojo(ModuleProcessUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> moduleProcessService.updateModuleProcess(reqVO), MODULE_PROCESS_NOT_EXISTS);
    }

    @Test
    public void testDeleteModuleProcess_success() {
        // mock 数据
        ModuleProcessDO dbModuleProcess = randomPojo(ModuleProcessDO.class);
        moduleProcessMapper.insert(dbModuleProcess);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbModuleProcess.getId();

        // 调用
        moduleProcessService.deleteModuleProcess(id);
       // 校验数据不存在了
       assertNull(moduleProcessMapper.selectById(id));
    }

    @Test
    public void testDeleteModuleProcess_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> moduleProcessService.deleteModuleProcess(id), MODULE_PROCESS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetModuleProcessPage() {
       // mock 数据
       ModuleProcessDO dbModuleProcess = randomPojo(ModuleProcessDO.class, o -> { // 等会查询到
           o.setModuleId(null);
           o.setProcessId(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       moduleProcessMapper.insert(dbModuleProcess);
       // 测试 moduleId 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setModuleId(null)));
       // 测试 processId 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setProcessId(null)));
       // 测试 createTime 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setRemark(null)));
       // 准备参数
       ModuleProcessPageReqVO reqVO = new ModuleProcessPageReqVO();
       reqVO.setModuleId(null);
       reqVO.setProcessId(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       PageResult<ModuleProcessDO> pageResult = moduleProcessService.getModuleProcessPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbModuleProcess, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetModuleProcessList() {
       // mock 数据
       ModuleProcessDO dbModuleProcess = randomPojo(ModuleProcessDO.class, o -> { // 等会查询到
           o.setModuleId(null);
           o.setProcessId(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       moduleProcessMapper.insert(dbModuleProcess);
       // 测试 moduleId 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setModuleId(null)));
       // 测试 processId 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setProcessId(null)));
       // 测试 createTime 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       moduleProcessMapper.insert(cloneIgnoreId(dbModuleProcess, o -> o.setRemark(null)));
       // 准备参数
       ModuleProcessExportReqVO reqVO = new ModuleProcessExportReqVO();
       reqVO.setModuleId(null);
       reqVO.setProcessId(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       List<ModuleProcessDO> list = moduleProcessService.getModuleProcessList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbModuleProcess, list.get(0));
    }

}
