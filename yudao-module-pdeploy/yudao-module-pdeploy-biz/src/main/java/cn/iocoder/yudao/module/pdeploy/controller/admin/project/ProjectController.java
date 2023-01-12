package cn.iocoder.yudao.module.pdeploy.controller.admin.project;

import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.ModuleRespVO;
import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.ServerRespVO;
import cn.iocoder.yudao.module.pdeploy.service.projectmodule.ProjectModuleService;
import cn.iocoder.yudao.module.pdeploy.service.server.ServerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.*;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;

import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.project.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.project.ProjectDO;
import cn.iocoder.yudao.module.pdeploy.convert.project.ProjectConvert;
import cn.iocoder.yudao.module.pdeploy.service.project.ProjectService;

@Api(tags = "管理后台 - 私有项目")
@RestController
@RequestMapping("/pdeploy/project")
@Validated
public class ProjectController {

    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectModuleService projectModuleService;
    @Resource
    private ServerService serverService;

    @DeleteMapping("/deleteProjectServer")
    @ApiOperation("更新当前项目项服务器和进程关系")
    public CommonResult<Boolean> deleteProjectServer(@RequestParam("serverId") Long serverId) {
        projectService.deleteProjectServer(serverId);
        return success(true);
    }

    @PutMapping("/updateProjectServer")
    @ApiOperation("更新当前项目项服务器和进程关系")
    public CommonResult<Boolean> updateProjectServer(@Valid @RequestBody ProjectServerUpdateReqVO updateReqVO) {
        projectService.updateProjectServer(updateReqVO);
        return success(true);
    }

    @PostMapping("/showProjectConf")
    @ApiOperation("基线配置展示")
    public CommonResult<ProjectConfRespVO> showBaselineConf(@Valid @RequestBody ProjectUpdateReqVO updateReqVO) {
        ProjectConfRespVO confRespVO = projectService.showBaselineConf(updateReqVO);
        return success(confRespVO);
    }

    @PostMapping("/syncProjectConf")
    @ApiOperation("基线配置同步")
    public CommonResult<Boolean> syncBaselineConf(@Valid @RequestBody ProjectUpdateReqVO updateReqVO) {
        projectService.syncBaselineConf(updateReqVO);
        return success(true);
    }


    @PostMapping("/extend")
    @ApiOperation("项目继承")
    public CommonResult<ProjectExtendRespVO> extendProject(@Valid @RequestBody ProjectExtendReqVO extendReqVO) {
        ProjectExtendRespVO projectExtendRespVO = projectService.extendProject(extendReqVO);
        return success(projectExtendRespVO);
    }

    @PostMapping("/mergeServer")
    @ApiOperation("服务器合并")
    public CommonResult<Boolean> mergeServer(@Valid @RequestBody MergeServerReqVO mergeServerReqVO) {
        projectService.mergerServer(mergeServerReqVO);
        return success(true);
    }

    @PostMapping("/create")
    @ApiOperation("创建私有项目")
    @PreAuthorize("@ss.hasPermission('pdeploy:project:create')")
    public CommonResult<Long> createProject(@Valid @RequestBody ProjectCreateReqVO createReqVO) {
        return success(projectService.createProject(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新私有项目")
    @PreAuthorize("@ss.hasPermission('pdeploy:project:update')")
    public CommonResult<Boolean> updateProject(@Valid @RequestBody ProjectUpdateReqVO updateReqVO) {
        projectService.updateProject(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除私有项目")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project:delete')")
    public CommonResult<Boolean> deleteProject(@RequestParam("id") Long id) {
        projectService.deleteProject(id);
        return success(true);
    }

    /**
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1269, '项目管理', '', 1, 10, 1268, 'project', 'money', NULL, 0, b'1', b'1', '1', '2022-08-12 12:00:24', '1', '2022-08-12 12:08:00', b'0');
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1270, '项目列表', '', 2, 10, 1269, 'list', 'list', 'pdeploy/project/list/index', 0, b'1', b'1', '1', '2022-08-12 12:01:30', '1', '2022-08-12 12:10:21', b'0');
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1271, '项目配置', '', 2, 20, 1269, 'conf', 'pay', 'pdeploy/project/conf/index', 0, b'1', b'1', '1', '2022-08-12 12:08:43', '1', '2022-08-12 12:10:31', b'0');
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1272, '基线管理', '', 2, 20, 1268, 'baseline', 'slider', 'pdeploy/baseline/index', 0, b'1', b'1', '1', '2022-08-12 12:48:18', '1', '2022-08-12 12:48:18', b'0');
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1273, '代码管理', '', 2, 30, 1268, 'codebase', 'code', 'pdeploy/codebase/index', 0, b'1', b'1', '1', '2022-08-12 12:48:59', '1', '2022-08-12 12:48:59', b'0');
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1274, '模块管理', '', 2, 40, 1268, 'module', 'example', 'pdeploy/module/index', 0, b'1', b'1', '1', '2022-08-12 12:49:43', '1', '2022-08-12 12:50:04', b'0');
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1275, '服务器管理', '', 2, 50, 1268, 'server', 'server', 'pdeploy/server/index', 0, b'1', b'1', '1', '2022-08-12 12:50:58', '1', '2022-08-12 12:50:58', b'0');
     * INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1276, '进程管理', '', 2, 60, 1268, 'process', 'swagger', 'pdeploy/process/index', 0, b'1', b'1', '1', '2022-08-12 12:51:44', '1', '2022-08-12 12:51:44', b'0');
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("获得私有项目")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project:query')")
    public CommonResult<ProjectRespVO> getProject(@RequestParam("id") Long id) {
        ProjectDO project = projectService.getProject(id);
        ProjectRespVO projectRespVO = ProjectConvert.INSTANCE.convert(project);
        Set<Long> moduleIds = projectModuleService.getModuleIdsByProjectId(id);
        projectRespVO.setModuleIds(moduleIds);
        return success(projectRespVO);
    }

    @GetMapping("/getAll")
    @ApiOperation("获得私有项目")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project:query')")
    public CommonResult<ProjectRespVO> getProjectAll(@RequestParam("id") Long id) {
        ProjectDO project = projectService.getProject(id);
        ProjectRespVO projectRespVO = ProjectConvert.INSTANCE.convert(project);
        List<ModuleRespVO> modules = projectModuleService.getModulesByProjectId(id);
        projectRespVO.setModules(modules);
        List<ServerRespVO> servers = serverService.getServersByProjectId(id);
        projectRespVO.setServers(servers);
        return success(projectRespVO);
    }

    @GetMapping("/list")
    @ApiOperation("获得私有项目列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project:query')")
    public CommonResult<List<ProjectRespVO>> getProjectList(@RequestParam("ids") Collection<Long> ids) {
        List<ProjectDO> list = projectService.getProjectList(ids);
        return success(ProjectConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得私有项目分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:project:query')")
    public CommonResult<PageResult<ProjectRespVO>> getProjectPage(@Valid ProjectPageReqVO pageVO) {
        PageResult<ProjectDO> pageResult = projectService.getProjectPage(pageVO);
        return success(ProjectConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出私有项目 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:project:export')")
    @OperateLog(type = EXPORT)
    public void exportProjectExcel(@Valid ProjectExportReqVO exportReqVO,
                                   HttpServletResponse response) throws IOException {
        List<ProjectDO> list = projectService.getProjectList(exportReqVO);
        // 导出 Excel
        List<ProjectExcelVO> datas = ProjectConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "私有项目.xls", "数据", ProjectExcelVO.class, datas);
    }

    @GetMapping("/get-deploy-info")
    @ApiOperation("获得私有项目部署列表")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project:query')")
    public CommonResult<ProjectProcessRespVo> getProjectDeployInfo(@RequestParam("id") Long id) {
        return success(projectService.getProjectProcess(id));
    }

}
