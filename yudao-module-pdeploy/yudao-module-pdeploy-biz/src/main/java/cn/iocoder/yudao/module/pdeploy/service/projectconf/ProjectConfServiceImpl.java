package cn.iocoder.yudao.module.pdeploy.service.projectconf;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.project.ProjectMapper;
import cn.iocoder.yudao.module.pdeploy.service.project.ProjectService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.projectconf.ProjectConfConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pdeploy.enums.ErrorCodeConstants.*;

/**
 * 项目配置 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class ProjectConfServiceImpl implements ProjectConfService {

    @Resource
    private ProjectConfMapper projectConfMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ProjectService projectService;

    @Override
    public Long createProjectConf(ProjectConfCreateReqVO createReqVO) {
        // 插入
        ProjectConfDO projectConf = ProjectConfConvert.INSTANCE.convert(createReqVO);
        projectConfMapper.insert(projectConf);
        // 返回
        return projectConf.getId();
    }

    @Override
    public void updateProjectConf(ProjectConfUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateProjectConfExists(updateReqVO.getId());
        // 更新
        ProjectConfDO updateObj = ProjectConfConvert.INSTANCE.convert(updateReqVO);
        projectConfMapper.updateById(updateObj);
        // 同步项目配置
        Long projectId = updateObj.getProjectId();
        String confKey = updateObj.getConfKey();
        String confValue = updateObj.getConfValue();
        List<String> tags = Arrays.asList(confKey.split("__"));
        ProjectDO projectDO = projectMapper.selectById(projectId);
        String midwareCustomTags = projectDO.getMidwareCustomTags();
        Set<String> allCustomTags = new HashSet<>();
        if (StringUtils.isNotEmpty(midwareCustomTags)) {
            Set<String> customTags = Sets.newHashSet(midwareCustomTags.split(","));
            allCustomTags.addAll(customTags);
        }
        if ("true".equals(confValue) && tags.contains("custom")) {
            allCustomTags.add(tags.get(0));
            ProjectDO update = ProjectDO.builder()
                    .id(projectId)
                    .midwareCustomTags(String.join(",", allCustomTags))
                    .build();
            projectMapper.updateById(update);
            projectService.batchSaveProjectConf(projectId, projectDO.getBaselineId(), Collections.singletonList(1));
        }

    }

    @Override
    public void deleteProjectConf(Long id) {
        // 校验存在
        this.validateProjectConfExists(id);
        // 删除
        projectConfMapper.deleteById(id);
    }

    private void validateProjectConfExists(Long id) {
        if (projectConfMapper.selectById(id) == null) {
            throw exception(PROJECT_CONF_NOT_EXISTS);
        }
    }

    @Override
    public ProjectConfDO getProjectConf(Long id) {
        return projectConfMapper.selectById(id);
    }

    @Override
    public List<ProjectConfDO> getProjectConfList(Collection<Long> ids) {
        return projectConfMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectConfDO> getProjectConfPage(ProjectConfPageReqVO pageReqVO) {
        return projectConfMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProjectConfDO> getProjectConfList(ProjectConfExportReqVO exportReqVO) {
        return projectConfMapper.selectList(exportReqVO);
    }

}
