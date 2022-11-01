package cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule;

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

import cn.iocoder.yudao.module.pdeploy.controller.admin.projectmodule.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule.ProjectModuleDO;
import cn.iocoder.yudao.module.pdeploy.convert.projectmodule.ProjectModuleConvert;
import cn.iocoder.yudao.module.pdeploy.service.projectmodule.ProjectModuleService;

@Api(tags = "管理后台 - 项目模块关系")
@RestController
@RequestMapping("/pdeploy/project-module")
@Validated
public class ProjectModuleController {

    @Resource
    private ProjectModuleService projectModuleService;

    @PostMapping("/create")
    @ApiOperation("创建项目模块关系")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-module:create')")
    public CommonResult<Long> createProjectModule(@Valid @RequestBody ProjectModuleCreateReqVO createReqVO) {
        return success(projectModuleService.createProjectModule(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新项目模块关系")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-module:update')")
    public CommonResult<Boolean> updateProjectModule(@Valid @RequestBody ProjectModuleUpdateReqVO updateReqVO) {
        projectModuleService.updateProjectModule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除项目模块关系")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project-module:delete')")
    public CommonResult<Boolean> deleteProjectModule(@RequestParam("id") Long id) {
        projectModuleService.deleteProjectModule(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得项目模块关系")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project-module:query')")
    public CommonResult<ProjectModuleRespVO> getProjectModule(@RequestParam("id") Long id) {
        ProjectModuleDO projectModule = projectModuleService.getProjectModule(id);
        return success(ProjectModuleConvert.INSTANCE.convert(projectModule));
    }

    @GetMapping("/list")
    @ApiOperation("获得项目模块关系列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:project-module:query')")
    public CommonResult<List<ProjectModuleRespVO>> getProjectModuleList(@RequestParam("ids") Collection<Long> ids) {
        List<ProjectModuleDO> list = projectModuleService.getProjectModuleList(ids);
        return success(ProjectModuleConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得项目模块关系分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-module:query')")
    public CommonResult<PageResult<ProjectModuleRespVO>> getProjectModulePage(@Valid ProjectModulePageReqVO pageVO) {
        PageResult<ProjectModuleDO> pageResult = projectModuleService.getProjectModulePage(pageVO);
        return success(ProjectModuleConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出项目模块关系 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:project-module:export')")
    @OperateLog(type = EXPORT)
    public void exportProjectModuleExcel(@Valid ProjectModuleExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProjectModuleDO> list = projectModuleService.getProjectModuleList(exportReqVO);
        // 导出 Excel
        List<ProjectModuleExcelVO> datas = ProjectModuleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "项目模块关系.xls", "数据", ProjectModuleExcelVO.class, datas);
    }

}
