package cn.iocoder.yudao.module.pdeploy.service.server;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.server.ServerMapper;
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
* {@link ServerServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(ServerServiceImpl.class)
public class ServerServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ServerServiceImpl serverService;

    @Resource
    private ServerMapper serverMapper;

    @Test
    public void testCreateServer_success() {
        // 准备参数
        ServerCreateReqVO reqVO = randomPojo(ServerCreateReqVO.class);

        // 调用
        Long serverId = serverService.createServer(reqVO);
        // 断言
        assertNotNull(serverId);
        // 校验记录的属性是否正确
        ServerDO server = serverMapper.selectById(serverId);
        assertPojoEquals(reqVO, server);
    }

    @Test
    public void testUpdateServer_success() {
        // mock 数据
        ServerDO dbServer = randomPojo(ServerDO.class);
        serverMapper.insert(dbServer);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ServerUpdateReqVO reqVO = randomPojo(ServerUpdateReqVO.class, o -> {
            o.setId(dbServer.getId()); // 设置更新的 ID
        });

        // 调用
        serverService.updateServer(reqVO);
        // 校验是否更新正确
        ServerDO server = serverMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, server);
    }

    @Test
    public void testUpdateServer_notExists() {
        // 准备参数
        ServerUpdateReqVO reqVO = randomPojo(ServerUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> serverService.updateServer(reqVO), SERVER_NOT_EXISTS);
    }

    @Test
    public void testDeleteServer_success() {
        // mock 数据
        ServerDO dbServer = randomPojo(ServerDO.class);
        serverMapper.insert(dbServer);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbServer.getId();

        // 调用
        serverService.deleteServer(id);
       // 校验数据不存在了
       assertNull(serverMapper.selectById(id));
    }

    @Test
    public void testDeleteServer_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> serverService.deleteServer(id), SERVER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetServerPage() {
       // mock 数据
       ServerDO dbServer = randomPojo(ServerDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setProjectId(null);
           o.setName(null);
           o.setIp(null);
           o.setCpu(null);
           o.setMemory(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       serverMapper.insert(dbServer);
       // 测试 baselineId 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setBaselineId(null)));
       // 测试 projectId 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setProjectId(null)));
       // 测试 name 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setName(null)));
       // 测试 ip 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setIp(null)));
       // 测试 cpu 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setCpu(null)));
       // 测试 memory 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setMemory(null)));
       // 测试 createTime 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setRemark(null)));
       // 准备参数
       ServerPageReqVO reqVO = new ServerPageReqVO();
       reqVO.setBaselineId(null);
       reqVO.setProjectId(null);
       reqVO.setName(null);
       reqVO.setIp(null);
       reqVO.setCpu(null);
       reqVO.setMemory(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       PageResult<ServerDO> pageResult = serverService.getServerPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbServer, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetServerList() {
       // mock 数据
       ServerDO dbServer = randomPojo(ServerDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setProjectId(null);
           o.setName(null);
           o.setIp(null);
           o.setCpu(null);
           o.setMemory(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       serverMapper.insert(dbServer);
       // 测试 baselineId 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setBaselineId(null)));
       // 测试 projectId 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setProjectId(null)));
       // 测试 name 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setName(null)));
       // 测试 ip 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setIp(null)));
       // 测试 cpu 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setCpu(null)));
       // 测试 memory 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setMemory(null)));
       // 测试 createTime 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       serverMapper.insert(cloneIgnoreId(dbServer, o -> o.setRemark(null)));
       // 准备参数
       ServerExportReqVO reqVO = new ServerExportReqVO();
       reqVO.setBaselineId(null);
       reqVO.setProjectId(null);
       reqVO.setName(null);
       reqVO.setIp(null);
       reqVO.setCpu(null);
       reqVO.setMemory(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       List<ServerDO> list = serverService.getServerList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbServer, list.get(0));
    }

}
