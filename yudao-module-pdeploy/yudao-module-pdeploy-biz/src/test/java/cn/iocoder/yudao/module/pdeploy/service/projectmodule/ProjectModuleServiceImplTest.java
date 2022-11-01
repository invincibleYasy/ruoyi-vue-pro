package cn.iocoder.yudao.module.pdeploy.service.projectmodule;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectmodule.ProjectModuleMapper;
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
* {@link ProjectModuleServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(ProjectModuleServiceImpl.class)
public class ProjectModuleServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProjectModuleServiceImpl projectModuleService;

    @Resource
    private ProjectModuleMapper projectModuleMapper;

    @Test
    public void testCreateProjectModule_success() {
        // 准备参数
        ProjectModuleCreateReqVO reqVO = randomPojo(ProjectModuleCreateReqVO.class);

        // 调用
        Long projectModuleId = projectModuleService.createProjectModule(reqVO);
        // 断言
        assertNotNull(projectModuleId);
        // 校验记录的属性是否正确
        ProjectModuleDO projectModule = projectModuleMapper.selectById(projectModuleId);
        assertPojoEquals(reqVO, projectModule);
    }

    @Test
    public void testUpdateProjectModule_success() {
        // mock 数据
        ProjectModuleDO dbProjectModule = randomPojo(ProjectModuleDO.class);
        projectModuleMapper.insert(dbProjectModule);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProjectModuleUpdateReqVO reqVO = randomPojo(ProjectModuleUpdateReqVO.class, o -> {
            o.setId(dbProjectModule.getId()); // 设置更新的 ID
        });

        // 调用
        projectModuleService.updateProjectModule(reqVO);
        // 校验是否更新正确
        ProjectModuleDO projectModule = projectModuleMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, projectModule);
    }

    @Test
    public void testUpdateProjectModule_notExists() {
        // 准备参数
        ProjectModuleUpdateReqVO reqVO = randomPojo(ProjectModuleUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> projectModuleService.updateProjectModule(reqVO), PROJECT_MODULE_NOT_EXISTS);
    }

    @Test
    public void testDeleteProjectModule_success() {
        // mock 数据
        ProjectModuleDO dbProjectModule = randomPojo(ProjectModuleDO.class);
        projectModuleMapper.insert(dbProjectModule);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProjectModule.getId();

        // 调用
        projectModuleService.deleteProjectModule(id);
       // 校验数据不存在了
       assertNull(projectModuleMapper.selectById(id));
    }

    @Test
    public void testDeleteProjectModule_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> projectModuleService.deleteProjectModule(id), PROJECT_MODULE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectModulePage() {
       // mock 数据
       ProjectModuleDO dbProjectModule = randomPojo(ProjectModuleDO.class, o -> { // 等会查询到
           o.setProjectId(null);
           o.setModuleId(null);
           o.setCreateTime(null);
       });
       projectModuleMapper.insert(dbProjectModule);
       // 测试 projectId 不匹配
       projectModuleMapper.insert(cloneIgnoreId(dbProjectModule, o -> o.setProjectId(null)));
       // 测试 moduleId 不匹配
       projectModuleMapper.insert(cloneIgnoreId(dbProjectModule, o -> o.setModuleId(null)));
       // 测试 createTime 不匹配
       projectModuleMapper.insert(cloneIgnoreId(dbProjectModule, o -> o.setCreateTime(null)));
       // 准备参数
       ProjectModulePageReqVO reqVO = new ProjectModulePageReqVO();
       reqVO.setProjectId(null);
       reqVO.setModuleId(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);

       // 调用
       PageResult<ProjectModuleDO> pageResult = projectModuleService.getProjectModulePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProjectModule, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectModuleList() {
       // mock 数据
       ProjectModuleDO dbProjectModule = randomPojo(ProjectModuleDO.class, o -> { // 等会查询到
           o.setProjectId(null);
           o.setModuleId(null);
           o.setCreateTime(null);
       });
       projectModuleMapper.insert(dbProjectModule);
       // 测试 projectId 不匹配
       projectModuleMapper.insert(cloneIgnoreId(dbProjectModule, o -> o.setProjectId(null)));
       // 测试 moduleId 不匹配
       projectModuleMapper.insert(cloneIgnoreId(dbProjectModule, o -> o.setModuleId(null)));
       // 测试 createTime 不匹配
       projectModuleMapper.insert(cloneIgnoreId(dbProjectModule, o -> o.setCreateTime(null)));
       // 准备参数
       ProjectModuleExportReqVO reqVO = new ProjectModuleExportReqVO();
       reqVO.setProjectId(null);
       reqVO.setModuleId(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);

       // 调用
       List<ProjectModuleDO> list = projectModuleService.getProjectModuleList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbProjectModule, list.get(0));
    }

}
