package cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.*;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.projectconf.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf.ProjectConfDO;
import cn.iocoder.yudao.module.pdeploy.convert.projectconf.ProjectConfConvert;
import cn.iocoder.yudao.module.pdeploy.service.projectconf.ProjectConfService;

@Api(tags = "管理后台 - 项目配置")
@RestController
@RequestMapping("/pdeploy/project-conf")
@Validated
public class ProjectConfController {

    @Resource
    private ProjectConfService projectConfService;

    @PostMapping("/create")
    @ApiOperation("创建项目配置")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-conf:create')")
    public CommonResult<Long> createProjectConf(@Valid @RequestBody ProjectConfCreateReqVO createReqVO) {
        return success(projectConfService.createProjectConf(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新项目配置")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-conf:update')")
    public CommonResult<Boolean> updateProjectConf(@Valid @RequestBody ProjectConfUpdateReqVO updateReqVO) {
        projectConfService.updateProjectConf(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除项目配置")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project-conf:delete')")
    public CommonResult<Boolean> deleteProjectConf(@RequestParam("id") Long id) {
        projectConfService.deleteProjectConf(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得项目配置")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project-conf:query')")
    public CommonResult<ProjectConfRespVO> getProjectConf(@RequestParam("id") Long id) {
        ProjectConfDO projectConf = projectConfService.getProjectConf(id);
        return success(ProjectConfConvert.INSTANCE.convert(projectConf));
    }

    @GetMapping("/list")
    @ApiOperation("获得项目配置列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project-conf:query')")
    public CommonResult<List<ProjectConfRespVO>> getProjectConfList(@RequestParam("ids") Collection<Long> ids) {
        List<ProjectConfDO> list = projectConfService.getProjectConfList(ids);
        return success(ProjectConfConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得项目配置分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-conf:query')")
    public CommonResult<PageResult<ProjectConfRespVO>> getProjectConfPage(@Valid ProjectConfPageReqVO pageVO) {
        PageResult<ProjectConfDO> pageResult = projectConfService.getProjectConfPage(pageVO);
        return success(ProjectConfConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出项目配置 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-conf:export')")
    @OperateLog(type = EXPORT)
    public void exportProjectConfExcel(@Valid ProjectConfExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProjectConfDO> list = projectConfService.getProjectConfList(exportReqVO);
        // 导出 Excel
        List<ProjectConfExcelVO> datas = ProjectConfConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "项目配置.xls", "数据", ProjectConfExcelVO.class, datas);
    }

}
