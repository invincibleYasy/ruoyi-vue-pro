package cn.iocoder.yudao.module.pdeploy.service.codebase;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.codebase.CodebaseDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.codebase.CodebaseMapper;
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
* {@link CodebaseServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(CodebaseServiceImpl.class)
public class CodebaseServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CodebaseServiceImpl codebaseService;

    @Resource
    private CodebaseMapper codebaseMapper;

    @Test
    public void testCreateCodebase_success() {
        // 准备参数
        CodebaseCreateReqVO reqVO = randomPojo(CodebaseCreateReqVO.class);

        // 调用
        Long codebaseId = codebaseService.createCodebase(reqVO);
        // 断言
        assertNotNull(codebaseId);
        // 校验记录的属性是否正确
        CodebaseDO codebase = codebaseMapper.selectById(codebaseId);
        assertPojoEquals(reqVO, codebase);
    }

    @Test
    public void testUpdateCodebase_success() {
        // mock 数据
        CodebaseDO dbCodebase = randomPojo(CodebaseDO.class);
        codebaseMapper.insert(dbCodebase);// @Sql: 先插入出一条存在的数据
        // 准备参数
        CodebaseUpdateReqVO reqVO = randomPojo(CodebaseUpdateReqVO.class, o -> {
            o.setId(dbCodebase.getId()); // 设置更新的 ID
        });

        // 调用
        codebaseService.updateCodebase(reqVO);
        // 校验是否更新正确
        CodebaseDO codebase = codebaseMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, codebase);
    }

    @Test
    public void testUpdateCodebase_notExists() {
        // 准备参数
        CodebaseUpdateReqVO reqVO = randomPojo(CodebaseUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> codebaseService.updateCodebase(reqVO), CODEBASE_NOT_EXISTS);
    }

    @Test
    public void testDeleteCodebase_success() {
        // mock 数据
        CodebaseDO dbCodebase = randomPojo(CodebaseDO.class);
        codebaseMapper.insert(dbCodebase);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCodebase.getId();

        // 调用
        codebaseService.deleteCodebase(id);
       // 校验数据不存在了
       assertNull(codebaseMapper.selectById(id));
    }

    @Test
    public void testDeleteCodebase_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> codebaseService.deleteCodebase(id), CODEBASE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCodebasePage() {
       // mock 数据
       CodebaseDO dbCodebase = randomPojo(CodebaseDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setGitUrl(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       codebaseMapper.insert(dbCodebase);
       // 测试 name 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setName(null)));
       // 测试 gitUrl 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setGitUrl(null)));
       // 测试 createTime 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setRemark(null)));
       // 准备参数
       CodebasePageReqVO reqVO = new CodebasePageReqVO();
       reqVO.setName(null);
       reqVO.setGitUrl(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       PageResult<CodebaseDO> pageResult = codebaseService.getCodebasePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCodebase, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCodebaseList() {
       // mock 数据
       CodebaseDO dbCodebase = randomPojo(CodebaseDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setGitUrl(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       codebaseMapper.insert(dbCodebase);
       // 测试 name 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setName(null)));
       // 测试 gitUrl 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setGitUrl(null)));
       // 测试 createTime 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       codebaseMapper.insert(cloneIgnoreId(dbCodebase, o -> o.setRemark(null)));
       // 准备参数
       CodebaseExportReqVO reqVO = new CodebaseExportReqVO();
       reqVO.setName(null);
       reqVO.setGitUrl(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       List<CodebaseDO> list = codebaseService.getCodebaseList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbCodebase, list.get(0));
    }

}
