package cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess;

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

import cn.iocoder.yudao.module.pdeploy.controller.admin.moduleprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess.ModuleProcessDO;
import cn.iocoder.yudao.module.pdeploy.convert.moduleprocess.ModuleProcessConvert;
import cn.iocoder.yudao.module.pdeploy.service.moduleprocess.ModuleProcessService;

@Api(tags = "管理后台 - 模块进程关系")
@RestController
@RequestMapping("/pdeploy/module-process")
@Validated
public class ModuleProcessController {

    @Resource
    private ModuleProcessService moduleProcessService;

    @PostMapping("/create")
    @ApiOperation("创建模块进程关系")
    @PreAuthorize("@ss.hasPermission('pdeploy:module-process:create')")
    public CommonResult<Long> createModuleProcess(@Valid @RequestBody ModuleProcessCreateReqVO createReqVO) {
        return success(moduleProcessService.createModuleProcess(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新模块进程关系")
    @PreAuthorize("@ss.hasPermission('pdeploy:module-process:update')")
    public CommonResult<Boolean> updateModuleProcess(@Valid @RequestBody ModuleProcessUpdateReqVO updateReqVO) {
        moduleProcessService.updateModuleProcess(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除模块进程关系")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:module-process:delete')")
    public CommonResult<Boolean> deleteModuleProcess(@RequestParam("id") Long id) {
        moduleProcessService.deleteModuleProcess(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得模块进程关系")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:module-process:query')")
    public CommonResult<ModuleProcessRespVO> getModuleProcess(@RequestParam("id") Long id) {
        ModuleProcessDO moduleProcess = moduleProcessService.getModuleProcess(id);
        return success(ModuleProcessConvert.INSTANCE.convert(moduleProcess));
    }

    @GetMapping("/list")
    @ApiOperation("获得模块进程关系列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:module-process:query')")
    public CommonResult<List<ModuleProcessRespVO>> getModuleProcessList(@RequestParam("ids") Collection<Long> ids) {
        List<ModuleProcessDO> list = moduleProcessService.getModuleProcessList(ids);
        return success(ModuleProcessConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得模块进程关系分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:module-process:query')")
    public CommonResult<PageResult<ModuleProcessRespVO>> getModuleProcessPage(@Valid ModuleProcessPageReqVO pageVO) {
        PageResult<ModuleProcessDO> pageResult = moduleProcessService.getModuleProcessPage(pageVO);
        return success(ModuleProcessConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出模块进程关系 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:module-process:export')")
    @OperateLog(type = EXPORT)
    public void exportModuleProcessExcel(@Valid ModuleProcessExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ModuleProcessDO> list = moduleProcessService.getModuleProcessList(exportReqVO);
        // 导出 Excel
        List<ModuleProcessExcelVO> datas = ModuleProcessConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "模块进程关系.xls", "数据", ModuleProcessExcelVO.class, datas);
    }

}
