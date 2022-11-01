package cn.iocoder.yudao.module.pdeploy.service.baseline;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.baseline.BaselineMapper;
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
* {@link BaselineServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(BaselineServiceImpl.class)
public class BaselineServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BaselineServiceImpl baselineService;

    @Resource
    private BaselineMapper baselineMapper;

    @Test
    public void testCreateBaseline_success() {
        // 准备参数
        BaselineCreateReqVO reqVO = randomPojo(BaselineCreateReqVO.class);

        // 调用
        Long baselineId = baselineService.createBaseline(reqVO);
        // 断言
        assertNotNull(baselineId);
        // 校验记录的属性是否正确
        BaselineDO baseline = baselineMapper.selectById(baselineId);
        assertPojoEquals(reqVO, baseline);
    }

    @Test
    public void testUpdateBaseline_success() {
        // mock 数据
        BaselineDO dbBaseline = randomPojo(BaselineDO.class);
        baselineMapper.insert(dbBaseline);// @Sql: 先插入出一条存在的数据
        // 准备参数
        BaselineUpdateReqVO reqVO = randomPojo(BaselineUpdateReqVO.class, o -> {
            o.setId(dbBaseline.getId()); // 设置更新的 ID
        });

        // 调用
        baselineService.updateBaseline(reqVO);
        // 校验是否更新正确
        BaselineDO baseline = baselineMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, baseline);
    }

    @Test
    public void testUpdateBaseline_notExists() {
        // 准备参数
        BaselineUpdateReqVO reqVO = randomPojo(BaselineUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> baselineService.updateBaseline(reqVO), BASELINE_NOT_EXISTS);
    }

    @Test
    public void testDeleteBaseline_success() {
        // mock 数据
        BaselineDO dbBaseline = randomPojo(BaselineDO.class);
        baselineMapper.insert(dbBaseline);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbBaseline.getId();

        // 调用
        baselineService.deleteBaseline(id);
       // 校验数据不存在了
       assertNull(baselineMapper.selectById(id));
    }

    @Test
    public void testDeleteBaseline_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> baselineService.deleteBaseline(id), BASELINE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBaselinePage() {
       // mock 数据
       BaselineDO dbBaseline = randomPojo(BaselineDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setVersion(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       baselineMapper.insert(dbBaseline);
       // 测试 name 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setName(null)));
       // 测试 version 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setVersion(null)));
       // 测试 createTime 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setRemark(null)));
       // 准备参数
       BaselinePageReqVO reqVO = new BaselinePageReqVO();
       reqVO.setName(null);
       reqVO.setVersion(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       PageResult<BaselineDO> pageResult = baselineService.getBaselinePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbBaseline, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBaselineList() {
       // mock 数据
       BaselineDO dbBaseline = randomPojo(BaselineDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setVersion(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       baselineMapper.insert(dbBaseline);
       // 测试 name 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setName(null)));
       // 测试 version 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setVersion(null)));
       // 测试 createTime 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       baselineMapper.insert(cloneIgnoreId(dbBaseline, o -> o.setRemark(null)));
       // 准备参数
       BaselineExportReqVO reqVO = new BaselineExportReqVO();
       reqVO.setName(null);
       reqVO.setVersion(null);
       reqVO.setBeginCreateTime(null);
       reqVO.setEndCreateTime(null);
       reqVO.setRemark(null);

       // 调用
       List<BaselineDO> list = baselineService.getBaselineList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbBaseline, list.get(0));
    }

}
