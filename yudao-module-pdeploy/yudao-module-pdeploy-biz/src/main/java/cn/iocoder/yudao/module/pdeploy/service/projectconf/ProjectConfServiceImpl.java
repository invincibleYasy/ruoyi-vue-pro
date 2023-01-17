package cn.iocoder.yudao.module.pdeploy.service.projectconf;

import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.BaseVars;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.DynamicVars;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.project.ProjectMapper;
import cn.iocoder.yudao.module.pdeploy.enums.DeployStep;
import cn.iocoder.yudao.module.pdeploy.service.project.ProjectService;
import com.google.common.collect.Sets;
import jdk.nashorn.internal.runtime.regexp.joni.constants.internal.OPCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.pdeploy.convert.projectconf.ProjectConfConvert;
import cn.iocoder.yudao.module.pdeploy.dal.mysql.projectconf.ProjectConfMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

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
        Integer type = updateObj.getType();
        if (DeployStep.PLAN_MID.getValue().equals(type)) {
            List<ProjectConfDO> projectConfDOS = projectConfMapper.selectList(new LambdaQueryWrapperX<ProjectConfDO>()
                    .eqIfPresent(ProjectConfDO::getProjectId, updateObj.getProjectId())
                    .eqIfPresent(ProjectConfDO::getType, DeployStep.PLAN_MID.getValue())
                    .likeIfPresent(ProjectConfDO::getConfKey, "_custom")
                    .eqIfPresent(ProjectConfDO::getConfValue, "true")
            );
            Set<String> collect = projectConfDOS.stream().map(ProjectConfDO::getTag).collect(Collectors.toSet());
            ProjectDO update = ProjectDO.builder()
                    .id(updateObj.getProjectId())
                    .midwareCustomTags(String.join(",", collect))
                    .build();
            projectMapper.updateById(update);
            projectService.batchSaveProjectConf(updateObj.getProjectId(), updateObj.getBaselineId(), Collections.singletonList(DeployStep.DEPLOY_MID.getValue()));
        }
//        // 更新项目配置
//        List<ProjectConfDO> projectConfDOS = projectConfMapper.selectList(new LambdaQueryWrapperX<ProjectConfDO>().eqIfPresent(ProjectConfDO::getProjectId, updateObj.getProjectId()));
//        if (CollectionUtils.isNotEmpty(projectConfDOS)) {
//            Map<String, List<ProjectConfDO>> projConfMap = projectConfDOS.stream().collect(Collectors.groupingBy(projectConfDO -> {
//                Integer confType = projectConfDO.getType();
//                if (DeployStep.SYNC_MID_IMAGES.getValue().equals(projectConfDO.getType())) {
//                    confType = DeployStep.DEPLOY_MID.getValue();
//                } else if (DeployStep.SYNC_MOD_IMAGES.getValue().equals(projectConfDO.getType())) {
//                    confType = DeployStep.DEPLOY_APP.getValue();
//                }
//                return confType + "_" + projectConfDO.getTag();
//            }));
//            DynamicVars dynamicVars = new DynamicVars();
//            projConfMap.forEach((type_tag, projConfList) -> {
//                String[] arr = type_tag.split("_");
//                DeployStep deployStep = DeployStep.getByValue(Integer.valueOf(arr[0]));
//                Map<String, BaseVars> baseVarsMap = assignConf(projConfList, arr[1]);
//                if (null == baseVarsMap) {
//                    return;
//                }
//                switch (deployStep) {
//                    case CHECK_AND_INIT_ENV:
//                        if (MapUtils.isEmpty(dynamicVars.getEnv())) {
//                            dynamicVars.setEnv(baseVarsMap);
//                        } else {
//                            dynamicVars.getEnv().putAll(baseVarsMap);
//                        }
//                        break;
//                    case PLAN_MID:
//                        if (MapUtils.isEmpty(dynamicVars.getMidwares_plan())) {
//                            dynamicVars.setMidwares_plan(baseVarsMap);
//                        } else {
//                            dynamicVars.getMidwares_plan().putAll(baseVarsMap);
//                        }
//                        break;
//                    case DEPLOY_APP:
//                        if (MapUtils.isEmpty(dynamicVars.getModels())) {
//                            dynamicVars.setModels(baseVarsMap);
//                        } else {
//                            dynamicVars.getModels().putAll(baseVarsMap);
//                        }
//                        break;
//                    case DEPLOY_MID:
//                        if (MapUtils.isEmpty(dynamicVars.getMidwares())) {
//                            dynamicVars.setMidwares(baseVarsMap);
//                        } else {
//                            dynamicVars.getMidwares().putAll(baseVarsMap);
//                        }
//                        break;
//                    case INIT_MID:
//                        if (MapUtils.isEmpty(dynamicVars.getMidwares_init())) {
//                            dynamicVars.setMidwares_init(baseVarsMap);
//                        } else {
//                            dynamicVars.getMidwares_init().putAll(baseVarsMap);
//                        }
//                        break;
//                    case INIT_APP:
//                    case CHECK_APP:
//                    default:
//                        break;
//
//                }
//            });
//            String projConfJson = JsonUtils.toJsonPrettyString(dynamicVars);
//            Object projConfYaml = JsonUtils.parseObject(projConfJson, Object.class);
//            DumperOptions options = new DumperOptions();
//            options.setIndentWithIndicator(true);
//            options.setIndicatorIndent(2);
//            Yaml yaml = new Yaml(options);
//            ProjectDO build = ProjectDO.builder()
//                    .projConfJson(projConfJson)
//                    .id(updateObj.getProjectId())
//                    .projConfYaml(yaml.dumpAsMap(projConfYaml))
//                    .build();
//            projectMapper.updateById(build);
//        }
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
