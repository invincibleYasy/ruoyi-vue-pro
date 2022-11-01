package cn.iocoder.yudao.module.pdeploy.service.process;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.process.ProcessMapper;
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
* {@link ProcessServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(ProcessServiceImpl.class)
public class ProcessServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProcessServiceImpl processService;

    @Resource
    private ProcessMapper processMapper;

    @Test
    public void testCreateProcess_success() {
        // 准备参数
        ProcessCreateReqVO reqVO = randomPojo(ProcessCreateReqVO.class);

        // 调用
        Long processId = processService.createProcess(reqVO);
        // 断言
        assertNotNull(processId);
        // 校验记录的属性是否正确
        ProcessDO process = processMapper.selectById(processId);
        assertPojoEquals(reqVO, process);
    }

    @Test
    public void testUpdateProcess_success() {
        // mock 数据
        ProcessDO dbProcess = randomPojo(ProcessDO.class);
        processMapper.insert(dbProcess);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProcessUpdateReqVO reqVO = randomPojo(ProcessUpdateReqVO.class, o -> {
            o.setId(dbProcess.getId()); // 设置更新的 ID
        });

        // 调用
        processService.updateProcess(reqVO);
        // 校验是否更新正确
        ProcessDO process = processMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, process);
    }

    @Test
    public void testUpdateProcess_notExists() {
        // 准备参数
        ProcessUpdateReqVO reqVO = randomPojo(ProcessUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> processService.updateProcess(reqVO), PROCESS_NOT_EXISTS);
    }

    @Test
    public void testDeleteProcess_success() {
        // mock 数据
        ProcessDO dbProcess = randomPojo(ProcessDO.class);
        processMapper.insert(dbProcess);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProcess.getId();

        // 调用
        processService.deleteProcess(id);
       // 校验数据不存在了
       assertNull(processMapper.selectById(id));
    }

    @Test
    public void testDeleteProcess_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> processService.deleteProcess(id), PROCESS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProcessPage() {
       // mock 数据
       ProcessDO dbProcess = randomPojo(ProcessDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setCodebaseId(null);
           o.setName(null);
           o.setVersion(null);
           o.setProcessType(null);
           o.setCpu(null);
           o.setMemory(null);
           o.setIsHa(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       processMapper.insert(dbProcess);
       // 测试 baselineId 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setBaselineId(null)));
       // 测试 codebaseId 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setCodebaseId(null)));
       // 测试 name 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setName(null)));
       // 测试 version 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setVersion(null)));
       // 测试 processType 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setProcessType(null)));
       // 测试 cpu 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setCpu(null)));
       // 测试 memory 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setMemory(null)));
       // 测试 isHa 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setIsHa(null)));
       // 测试 createTime 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setRemark(null)));
       // 准备参数
       ProcessPageReqVO reqVO = new ProcessPageReqVO();
       reqVO.setBaselineId(null);
       reqVO.setCodebaseId(null);
       reqVO.setName(null);
       reqVO.setVersion(null);
       reqVO.setProcessType(null);
       reqVO.setCpu(null);
       reqVO.setMemory(null);
       reqVO.setIsHa(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       PageResult<ProcessDO> pageResult = processService.getProcessPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProcess, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProcessList() {
       // mock 数据
       ProcessDO dbProcess = randomPojo(ProcessDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setCodebaseId(null);
           o.setName(null);
           o.setVersion(null);
           o.setProcessType(null);
           o.setCpu(null);
           o.setMemory(null);
           o.setIsHa(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       processMapper.insert(dbProcess);
       // 测试 baselineId 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setBaselineId(null)));
       // 测试 codebaseId 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setCodebaseId(null)));
       // 测试 name 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setName(null)));
       // 测试 version 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setVersion(null)));
       // 测试 processType 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setProcessType(null)));
       // 测试 cpu 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setCpu(null)));
       // 测试 memory 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setMemory(null)));
       // 测试 isHa 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setIsHa(null)));
       // 测试 createTime 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       processMapper.insert(cloneIgnoreId(dbProcess, o -> o.setRemark(null)));
       // 准备参数
       ProcessExportReqVO reqVO = new ProcessExportReqVO();
       reqVO.setBaselineId(null);
       reqVO.setCodebaseId(null);
       reqVO.setName(null);
       reqVO.setVersion(null);
       reqVO.setProcessType(null);
       reqVO.setCpu(null);
       reqVO.setMemory(null);
       reqVO.setIsHa(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       List<ProcessDO> list = processService.getProcessList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbProcess, list.get(0));
    }

}
