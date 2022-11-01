package cn.iocoder.yudao.module.pdeploy.controller.admin.process;

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

import cn.iocoder.yudao.module.pdeploy.controller.admin.process.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.convert.process.ProcessConvert;
import cn.iocoder.yudao.module.pdeploy.service.process.ProcessService;

@Api(tags = "管理后台 - 进程")
@RestController
@RequestMapping("/pdeploy/process")
@Validated
public class ProcessController {

    @Resource
    private ProcessService processService;

    @PostMapping("/create")
    @ApiOperation("创建进程")
    @PreAuthorize("@ss.hasPermission('pdeploy:process:create')")
    public CommonResult<Long> createProcess(@Valid @RequestBody ProcessCreateReqVO createReqVO) {
        return success(processService.createProcess(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新进程")
    @PreAuthorize("@ss.hasPermission('pdeploy:process:update')")
    public CommonResult<Boolean> updateProcess(@Valid @RequestBody ProcessUpdateReqVO updateReqVO) {
        processService.updateProcess(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除进程")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:process:delete')")
    public CommonResult<Boolean> deleteProcess(@RequestParam("id") Long id) {
        processService.deleteProcess(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得进程")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:process:query')")
    public CommonResult<ProcessRespVO> getProcess(@RequestParam("id") Long id) {
        ProcessDO process = processService.getProcess(id);
        return success(ProcessConvert.INSTANCE.convert(process));
    }

    @GetMapping("/list")
    @ApiOperation("获得进程列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:process:query')")
    public CommonResult<List<ProcessRespVO>> getProcessList(@RequestParam("ids") Collection<Long> ids) {
        List<ProcessDO> list = processService.getProcessList(ids);
        return success(ProcessConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得进程分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:process:query')")
    public CommonResult<PageResult<ProcessRespVO>> getProcessPage(@Valid ProcessPageReqVO pageVO) {
        PageResult<ProcessDO> pageResult = processService.getProcessPage(pageVO);
        return success(ProcessConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出进程 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:process:export')")
    @OperateLog(type = EXPORT)
    public void exportProcessExcel(@Valid ProcessExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ProcessDO> list = processService.getProcessList(exportReqVO);
        // 导出 Excel
        List<ProcessExcelVO> datas = ProcessConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "进程.xls", "数据", ProcessExcelVO.class, datas);
    }

}
