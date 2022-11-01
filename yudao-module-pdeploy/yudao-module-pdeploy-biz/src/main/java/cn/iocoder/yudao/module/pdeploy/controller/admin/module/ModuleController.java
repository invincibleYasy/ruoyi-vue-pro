package cn.iocoder.yudao.module.pdeploy.controller.admin.module;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.service.moduleprocess.ModuleProcessService;
import cn.iocoder.yudao.module.pdeploy.service.process.ProcessService;
import org.apache.commons.collections4.CollectionUtils;
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

import cn.iocoder.yudao.module.pdeploy.controller.admin.module.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.module.ModuleDO;
import cn.iocoder.yudao.module.pdeploy.convert.module.ModuleConvert;
import cn.iocoder.yudao.module.pdeploy.service.module.ModuleService;

@Api(tags = "管理后台 - 模块")
@RestController
@RequestMapping("/pdeploy/module")
@Validated
public class ModuleController {

    @Resource
    private ModuleService moduleService;
    @Resource
    private ModuleProcessService moduleProcessService;
    @Resource
    private ProcessService processService;

    @PostMapping("/create")
    @ApiOperation("创建模块")
    @PreAuthorize("@ss.hasPermission('pdeploy:module:create')")
    public CommonResult<Long> createModule(@Valid @RequestBody ModuleCreateReqVO createReqVO) {
        return success(moduleService.createModule(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新模块")
    @PreAuthorize("@ss.hasPermission('pdeploy:module:update')")
    public CommonResult<Boolean> updateModule(@Valid @RequestBody ModuleUpdateReqVO updateReqVO) {
        moduleService.updateModule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除模块")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:module:delete')")
    public CommonResult<Boolean> deleteModule(@RequestParam("id") Long id) {
        moduleService.deleteModule(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得模块")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:module:query')")
    public CommonResult<ModuleRespVO> getModule(@RequestParam("id") Long id) {
        ModuleDO module = moduleService.getModule(id);
        ModuleRespVO moduleRespVO = ModuleConvert.INSTANCE.convert(module);
        Set<Long> processIds = moduleProcessService.getProcessIdsByModuleId(id);
        moduleRespVO.setProcessIds(processIds);
        return success(moduleRespVO);
    }

    @GetMapping("/list")
    @ApiOperation("获得模块列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:module:query')")
    public CommonResult<List<ModuleRespVO>> getModuleList(@RequestParam("ids") Collection<Long> ids) {
        List<ModuleDO> list = moduleService.getModuleList(ids);
        List<ModuleRespVO> moduleRespVOS = ModuleConvert.INSTANCE.convertList(list);
        // 获取模块下的进程信息
        if (CollectionUtils.isNotEmpty(moduleRespVOS)) {
            moduleRespVOS.forEach(moduleRespVO -> {
                List<ProcessDO> processes = moduleProcessService.getProcessesByModuleId(moduleRespVO.getId());
                moduleRespVO.setProcesses(processes);
            });
        }
        return success(moduleRespVOS);
    }

    @GetMapping("/page")
    @ApiOperation("获得模块分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:module:query')")
    public CommonResult<PageResult<ModuleRespVO>> getModulePage(@Valid ModulePageReqVO pageVO) {
        PageResult<ModuleDO> pageResult = moduleService.getModulePage(pageVO);
        return success(ModuleConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出模块 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:module:export')")
    @OperateLog(type = EXPORT)
    public void exportModuleExcel(@Valid ModuleExportReqVO exportReqVO,
                                  HttpServletResponse response) throws IOException {
        List<ModuleDO> list = moduleService.getModuleList(exportReqVO);
        // 导出 Excel
        List<ModuleExcelVO> datas = ModuleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "模块.xls", "数据", ModuleExcelVO.class, datas);
    }

}
