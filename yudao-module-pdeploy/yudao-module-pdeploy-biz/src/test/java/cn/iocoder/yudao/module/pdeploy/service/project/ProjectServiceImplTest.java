package cn.iocoder.yudao.module.pdeploy.service.project;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.project.ProjectMapper;
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
* {@link ProjectServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(ProjectServiceImpl.class)
public class ProjectServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProjectServiceImpl projectService;

    @Resource
    private ProjectMapper projectMapper;

    @Test
    public void testCreateProject_success() {
        // 准备参数
        ProjectCreateReqVO reqVO = randomPojo(ProjectCreateReqVO.class);

        // 调用
        Long projectId = projectService.createProject(reqVO);
        // 断言
        assertNotNull(projectId);
        // 校验记录的属性是否正确
        ProjectDO project = projectMapper.selectById(projectId);
        assertPojoEquals(reqVO, project);
    }

    @Test
    public void testUpdateProject_success() {
        // mock 数据
        ProjectDO dbProject = randomPojo(ProjectDO.class);
        projectMapper.insert(dbProject);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProjectUpdateReqVO reqVO = randomPojo(ProjectUpdateReqVO.class, o -> {
            o.setId(dbProject.getId()); // 设置更新的 ID
        });

        // 调用
        projectService.updateProject(reqVO);
        // 校验是否更新正确
        ProjectDO project = projectMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, project);
    }

    @Test
    public void testUpdateProject_notExists() {
        // 准备参数
        ProjectUpdateReqVO reqVO = randomPojo(ProjectUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> projectService.updateProject(reqVO), PROJECT_NOT_EXISTS);
    }

    @Test
    public void testDeleteProject_success() {
        // mock 数据
        ProjectDO dbProject = randomPojo(ProjectDO.class);
        projectMapper.insert(dbProject);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProject.getId();

        // 调用
        projectService.deleteProject(id);
       // 校验数据不存在了
       assertNull(projectMapper.selectById(id));
    }

    @Test
    public void testDeleteProject_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> projectService.deleteProject(id), PROJECT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectPage() {
       // mock 数据
       ProjectDO dbProject = randomPojo(ProjectDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setName(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       projectMapper.insert(dbProject);
       // 测试 baselineId 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setBaselineId(null)));
       // 测试 name 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setName(null)));
       // 测试 createTime 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setRemark(null)));
       // 准备参数
       ProjectPageReqVO reqVO = new ProjectPageReqVO();
       reqVO.setBaselineId(null);
       reqVO.setName(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       PageResult<ProjectDO> pageResult = projectService.getProjectPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProject, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectList() {
       // mock 数据
       ProjectDO dbProject = randomPojo(ProjectDO.class, o -> { // 等会查询到
           o.setBaselineId(null);
           o.setName(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       projectMapper.insert(dbProject);
       // 测试 baselineId 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setBaselineId(null)));
       // 测试 name 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setName(null)));
       // 测试 createTime 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       projectMapper.insert(cloneIgnoreId(dbProject, o -> o.setRemark(null)));
       // 准备参数
       ProjectExportReqVO reqVO = new ProjectExportReqVO();
       reqVO.setBaselineId(null);
       reqVO.setName(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       List<ProjectDO> list = projectService.getProjectList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbProject, list.get(0));
    }

}
