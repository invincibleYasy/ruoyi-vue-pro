package cn.iocoder.yudao.module.pdeploy.service.baseline;


import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.baseline.BaselineConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.baseline.BaselineMapper;
import org.yaml.snakeyaml.Yaml;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 基线版本 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BaselineServiceImpl implements BaselineService {

    @Resource
    private BaselineMapper baselineMapper;
    @Resource
    private ProjectConfMapper projectConfMapper;
    @Resource
    private ThreadPoolTaskExecutor executor;


    @Override
    public Long createBaseline(BaselineCreateReqVO createReqVO) {
        // 插入
        BaselineDO baseline = BaselineConvert.INSTANCE.convert(createReqVO);
        baselineMapper.insert(baseline);
        //异步更新
        executor.submit(() -> batchSaveBaselineConfs(baseline.getId(), createReqVO.getMainConf(), createReqVO.getMainConfCcpass()));
        // 返回
        return baseline.getId();
    }

    private void batchSaveBaselineConfs(Long id, String mainConf, String mainConfCcpass) {
        Map<String, Object> deleteParams = new HashMap<>();
        deleteParams.put("project_id", -1);
        deleteParams.put("baseline_id",id);
        // 删除版本配置
        projectConfMapper.deleteByMap(deleteParams);
        // 保存主配置
        Yaml yaml = new Yaml();
        List<ProjectConfDO> confDOList = new ArrayList<>();
        LinkedHashMap<String, Object> mainConfMap = yaml.loadAs(mainConf, LinkedHashMap.class);
        iterateAndProcess(id, 1, confDOList, mainConfMap, "");
        // 保存ccpass配置
        LinkedHashMap ccpassConfMap = yaml.loadAs(mainConfCcpass, LinkedHashMap.class);
        iterateAndProcess(id, 2, confDOList, ccpassConfMap, "");
        if (CollectionUtils.isNotEmpty(confDOList)) {
            projectConfMapper.insertBatch(confDOList);
        }
    }

    private void iterateAndProcess(Long baselineId, Integer type, List<ProjectConfDO> mainConfigList, Map<String, Object> ymlEntry, String rootKey) {
        for (String key : ymlEntry.keySet()) {
            Object value = ymlEntry.get(key);
            if (null == value) {
                value = "";
            }
            if (value instanceof LinkedHashMap) {
                iterateAndProcess(baselineId, type, mainConfigList, (LinkedHashMap<String, Object>) value, StringUtils.isEmpty(rootKey) ? key : rootKey
                        + "." + key);
            } else {
                if (value instanceof Collection) {
                    value = JsonUtils.toJsonString(value);
                }
                ProjectConfDO confDO = ProjectConfDO.builder()
                        .projectId(-1l)
                        .baselineId(baselineId)
                        .confKey(StringUtils.isEmpty(rootKey) ? key : rootKey + "." + key)
                        .confValue(value.toString())
                        .type(type)
                        .modifyFlag(false)
                        .build();
                mainConfigList.add(confDO);
            }
        }
    }

    @Override
    public void updateBaseline(BaselineUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateBaselineExists(updateReqVO.getId());
        // 更新
        BaselineDO updateObj = BaselineConvert.INSTANCE.convert(updateReqVO);
        baselineMapper.updateById(updateObj);
        executor.submit(() ->
                batchSaveBaselineConfs(updateReqVO.getId(), updateReqVO.getMainConf(), updateReqVO.getMainConfCcpass())
        );

    }

    @Override
    public void deleteBaseline(Long id) {
        // 校验存在
        this.validateBaselineExists(id);
        // 删除
        baselineMapper.deleteById(id);
    }

    private void validateBaselineExists(Long id) {
        if (baselineMapper.selectById(id) == null) {
            throw exception(BASELINE_NOT_EXISTS);
        }
    }

    @Override
    public BaselineDO getBaseline(Long id) {
        return baselineMapper.selectById(id);
    }

    @Override
    public List<BaselineDO> getBaselineList(Collection<Long> ids) {
        return baselineMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BaselineDO> getBaselinePage(BaselinePageReqVO pageReqVO) {
        return baselineMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BaselineDO> getBaselineList(BaselineExportReqVO exportReqVO) {
        return baselineMapper.selectList(exportReqVO);
    }

}
