package cn.iocoder.yudao.module.pdeploy.service.module;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.module.ModuleMapper;
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
* {@link ModuleServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(ModuleServiceImpl.class)
public class ModuleServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ModuleServiceImpl moduleService;

    @Resource
    private ModuleMapper moduleMapper;

    @Test
    public void testCreateModule_success() {
        // 准备参数
        ModuleCreateReqVO reqVO = randomPojo(ModuleCreateReqVO.class);

        // 调用
        Long moduleId = moduleService.createModule(reqVO);
        // 断言
        assertNotNull(moduleId);
        // 校验记录的属性是否正确
        ModuleDO module = moduleMapper.selectById(moduleId);
        assertPojoEquals(reqVO, module);
    }

    @Test
    public void testUpdateModule_success() {
        // mock 数据
        ModuleDO dbModule = randomPojo(ModuleDO.class);
        moduleMapper.insert(dbModule);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ModuleUpdateReqVO reqVO = randomPojo(ModuleUpdateReqVO.class, o -> {
            o.setId(dbModule.getId()); // 设置更新的 ID
        });

        // 调用
        moduleService.updateModule(reqVO);
        // 校验是否更新正确
        ModuleDO module = moduleMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, module);
    }

    @Test
    public void testUpdateModule_notExists() {
        // 准备参数
        ModuleUpdateReqVO reqVO = randomPojo(ModuleUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> moduleService.updateModule(reqVO), MODULE_NOT_EXISTS);
    }

    @Test
    public void testDeleteModule_success() {
        // mock 数据
        ModuleDO dbModule = randomPojo(ModuleDO.class);
        moduleMapper.insert(dbModule);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbModule.getId();

        // 调用
        moduleService.deleteModule(id);
       // 校验数据不存在了
       assertNull(moduleMapper.selectById(id));
    }

    @Test
    public void testDeleteModule_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> moduleService.deleteModule(id), MODULE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetModulePage() {
       // mock 数据
       ModuleDO dbModule = randomPojo(ModuleDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setName(null);
           o.setModuleType(null);
           o.setEstimatedTime(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       moduleMapper.insert(dbModule);
       // 测试 baselineId 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setBaselineId(null)));
       // 测试 name 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setName(null)));
       // 测试 moduleType 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setModuleType(null)));
       // 测试 estimatedTime 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setEstimatedTime(null)));
       // 测试 createTime 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setRemark(null)));
       // 准备参数
       ModulePageReqVO reqVO = new ModulePageReqVO();
       reqVO.setBaselineId(null);
       reqVO.setName(null);
       reqVO.setModuleType(null);
       reqVO.setBeginEstimatedTime(null);
       reqVO.setEndEstimatedTime(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       PageResult<ModuleDO> pageResult = moduleService.getModulePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbModule, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetModuleList() {
       // mock 数据
       ModuleDO dbModule = randomPojo(ModuleDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setName(null);
           o.setModuleType(null);
           o.setEstimatedTime(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       moduleMapper.insert(dbModule);
       // 测试 baselineId 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setBaselineId(null)));
       // 测试 name 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setName(null)));
       // 测试 moduleType 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setModuleType(null)));
       // 测试 estimatedTime 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setEstimatedTime(null)));
       // 测试 createTime 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       moduleMapper.insert(cloneIgnoreId(dbModule, o -> o.setRemark(null)));
       // 准备参数
       ModuleExportReqVO reqVO = new ModuleExportReqVO();
       reqVO.setBaselineId(null);
       reqVO.setName(null);
       reqVO.setModuleType(null);
       reqVO.setBeginEstimatedTime(null);
       reqVO.setEndEstimatedTime(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       List<ModuleDO> list = moduleService.getModuleList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbModule, list.get(0));
    }

}
