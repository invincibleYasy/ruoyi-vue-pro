package cn.iocoder.yudao.module.pdeploy.service.projectconf;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;
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
* {@link ProjectConfServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(ProjectConfServiceImpl.class)
public class ProjectConfServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProjectConfServiceImpl projectConfService;

    @Resource
    private ProjectConfMapper projectConfMapper;

    @Test
    public void testCreateProjectConf_success() {
        // 准备参数
        ProjectConfCreateReqVO reqVO = randomPojo(ProjectConfCreateReqVO.class);

        // 调用
        Long projectConfId = projectConfService.createProjectConf(reqVO);
        // 断言
        assertNotNull(projectConfId);
        // 校验记录的属性是否正确
        ProjectConfDO projectConf = projectConfMapper.selectById(projectConfId);
        assertPojoEquals(reqVO, projectConf);
    }

    @Test
    public void testUpdateProjectConf_success() {
        // mock 数据
        ProjectConfDO dbProjectConf = randomPojo(ProjectConfDO.class);
        projectConfMapper.insert(dbProjectConf);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProjectConfUpdateReqVO reqVO = randomPojo(ProjectConfUpdateReqVO.class, o -> {
            o.setId(dbProjectConf.getId()); // 设置更新的 ID
        });

        // 调用
        projectConfService.updateProjectConf(reqVO);
        // 校验是否更新正确
        ProjectConfDO projectConf = projectConfMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, projectConf);
    }

    @Test
    public void testUpdateProjectConf_notExists() {
        // 准备参数
        ProjectConfUpdateReqVO reqVO = randomPojo(ProjectConfUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> projectConfService.updateProjectConf(reqVO), PROJECT_CONF_NOT_EXISTS);
    }

    @Test
    public void testDeleteProjectConf_success() {
        // mock 数据
        ProjectConfDO dbProjectConf = randomPojo(ProjectConfDO.class);
        projectConfMapper.insert(dbProjectConf);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProjectConf.getId();

        // 调用
        projectConfService.deleteProjectConf(id);
       // 校验数据不存在了
       assertNull(projectConfMapper.selectById(id));
    }

    @Test
    public void testDeleteProjectConf_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> projectConfService.deleteProjectConf(id), PROJECT_CONF_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectConfPage() {
       // mock 数据
       ProjectConfDO dbProjectConf = randomPojo(ProjectConfDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setProjectId(null);
           o.setConfKey(null);
           o.setConfValue(null);
           o.setModifyFlag(null);
           o.setType(null);
           o.setKeyDesc(null);
           o.setVersion(null);
           o.setCreateTime(null);
       });
       projectConfMapper.insert(dbProjectConf);
       // 测试 baselineId 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setBaselineId(null)));
       // 测试 projectId 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setProjectId(null)));
       // 测试 confKey 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setConfKey(null)));
       // 测试 confValue 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setConfValue(null)));
       // 测试 modifyFlag 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setModifyFlag(null)));
       // 测试 type 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setType(null)));
       // 测试 keyDesc 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setKeyDesc(null)));
       // 测试 version 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setVersion(null)));
       // 测试 createTime 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setCreateTime(null)));
       // 准备参数
       ProjectConfPageReqVO reqVO = new ProjectConfPageReqVO();
       reqVO.setBaselineId(null);
       reqVO.setProjectId(null);
       reqVO.setConfKey(null);
       reqVO.setConfValue(null);
       reqVO.setModifyFlag(null);
       reqVO.setType(null);
       reqVO.setKeyDesc(null);
       reqVO.setVersion(null);
       reqVO.setCreateTime((new Date[]{}));

       // 调用
       PageResult<ProjectConfDO> pageResult = projectConfService.getProjectConfPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProjectConf, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectConfList() {
       // mock 数据
       ProjectConfDO dbProjectConf = randomPojo(ProjectConfDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setProjectId(null);
           o.setConfKey(null);
           o.setConfValue(null);
           o.setModifyFlag(null);
           o.setType(null);
           o.setKeyDesc(null);
           o.setVersion(null);
           o.setCreateTime(null);
       });
       projectConfMapper.insert(dbProjectConf);
       // 测试 baselineId 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setBaselineId(null)));
       // 测试 projectId 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setProjectId(null)));
       // 测试 confKey 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setConfKey(null)));
       // 测试 confValue 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setConfValue(null)));
       // 测试 modifyFlag 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setModifyFlag(null)));
       // 测试 type 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setType(null)));
       // 测试 keyDesc 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setKeyDesc(null)));
       // 测试 version 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setVersion(null)));
       // 测试 createTime 不匹配
       projectConfMapper.insert(cloneIgnoreId(dbProjectConf, o -> o.setCreateTime(null)));
       // 准备参数
       ProjectConfExportReqVO reqVO = new ProjectConfExportReqVO();
       reqVO.setBaselineId(null);
       reqVO.setProjectId(null);
       reqVO.setConfKey(null);
       reqVO.setConfValue(null);
       reqVO.setModifyFlag(null);
       reqVO.setType(null);
       reqVO.setKeyDesc(null);
       reqVO.setVersion(null);
       reqVO.setCreateTime((new Date[]{}));

       // 调用
       List<ProjectConfDO> list = projectConfService.getProjectConfList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbProjectConf, list.get(0));
    }

}
