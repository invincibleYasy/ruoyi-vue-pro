package cn.iocoder.yudao.module.pdeploy.service.serverprocess;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.serverprocess.ServerProcessMapper;
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
* {@link ServerProcessServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(ServerProcessServiceImpl.class)
public class ServerProcessServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ServerProcessServiceImpl serverProcessService;

    @Resource
    private ServerProcessMapper serverProcessMapper;

    @Test
    public void testCreateServerProcess_success() {
        // 准备参数
        ServerProcessCreateReqVO reqVO = randomPojo(ServerProcessCreateReqVO.class);

        // 调用
        Long serverProcessId = serverProcessService.createServerProcess(reqVO);
        // 断言
        assertNotNull(serverProcessId);
        // 校验记录的属性是否正确
        ServerProcessDO serverProcess = serverProcessMapper.selectById(serverProcessId);
        assertPojoEquals(reqVO, serverProcess);
    }

    @Test
    public void testUpdateServerProcess_success() {
        // mock 数据
        ServerProcessDO dbServerProcess = randomPojo(ServerProcessDO.class);
        serverProcessMapper.insert(dbServerProcess);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ServerProcessUpdateReqVO reqVO = randomPojo(ServerProcessUpdateReqVO.class, o -> {
            o.setId(dbServerProcess.getId()); // 设置更新的 ID
        });

        // 调用
        serverProcessService.updateServerProcess(reqVO);
        // 校验是否更新正确
        ServerProcessDO serverProcess = serverProcessMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, serverProcess);
    }

    @Test
    public void testUpdateServerProcess_notExists() {
        // 准备参数
        ServerProcessUpdateReqVO reqVO = randomPojo(ServerProcessUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> serverProcessService.updateServerProcess(reqVO), SERVER_PROCESS_NOT_EXISTS);
    }

    @Test
    public void testDeleteServerProcess_success() {
        // mock 数据
        ServerProcessDO dbServerProcess = randomPojo(ServerProcessDO.class);
        serverProcessMapper.insert(dbServerProcess);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbServerProcess.getId();

        // 调用
        serverProcessService.deleteServerProcess(id);
       // 校验数据不存在了
       assertNull(serverProcessMapper.selectById(id));
    }

    @Test
    public void testDeleteServerProcess_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> serverProcessService.deleteServerProcess(id), SERVER_PROCESS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetServerProcessPage() {
       // mock 数据
       ServerProcessDO dbServerProcess = randomPojo(ServerProcessDO.class, o -> { // 等会查询到
           o.setServerId(null);
           o.setProcessId(null);
           o.setCreateTime(null);
       });
       serverProcessMapper.insert(dbServerProcess);
       // 测试 serverId 不匹配
       serverProcessMapper.insert(cloneIgnoreId(dbServerProcess, o -> o.setServerId(null)));
       // 测试 processId 不匹配
       serverProcessMapper.insert(cloneIgnoreId(dbServerProcess, o -> o.setProcessId(null)));
       // 测试 createTime 不匹配
       serverProcessMapper.insert(cloneIgnoreId(dbServerProcess, o -> o.setCreateTime(null)));
       // 准备参数
       ServerProcessPageReqVO reqVO = new ServerProcessPageReqVO();
       reqVO.setServerId(null);
       reqVO.setProcessId(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);

       // 调用
       PageResult<ServerProcessDO> pageResult = serverProcessService.getServerProcessPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbServerProcess, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetServerProcessList() {
       // mock 数据
       ServerProcessDO dbServerProcess = randomPojo(ServerProcessDO.class, o -> { // 等会查询到
           o.setServerId(null);
           o.setProcessId(null);
           o.setCreateTime(null);
       });
       serverProcessMapper.insert(dbServerProcess);
       // 测试 serverId 不匹配
       serverProcessMapper.insert(cloneIgnoreId(dbServerProcess, o -> o.setServerId(null)));
       // 测试 processId 不匹配
       serverProcessMapper.insert(cloneIgnoreId(dbServerProcess, o -> o.setProcessId(null)));
       // 测试 createTime 不匹配
       serverProcessMapper.insert(cloneIgnoreId(dbServerProcess, o -> o.setCreateTime(null)));
       // 准备参数
       ServerProcessExportReqVO reqVO = new ServerProcessExportReqVO();
       reqVO.setServerId(null);
       reqVO.setProcessId(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);

       // 调用
       List<ServerProcessDO> list = serverProcessService.getServerProcessList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbServerProcess, list.get(0));
    }

}
