package cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess;

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

import cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess.ServerProcessDO;
import cn.iocoder.yudao.module.pdeploy.convert.serverprocess.ServerProcessConvert;
import cn.iocoder.yudao.module.pdeploy.service.serverprocess.ServerProcessService;

@Api(tags = "管理后台 - 服务器进程关系")
@RestController
@RequestMapping("/pdeploy/server-process")
@Validated
public class ServerProcessController {

    @Resource
    private ServerProcessService serverProcessService;

    @PostMapping("/create")
    @ApiOperation("创建服务器进程关系")
    @PreAuthorize("@ss.hasPermission('pdeploy:server-process:create')")
    public CommonResult<Long> createServerProcess(@Valid @RequestBody ServerProcessCreateReqVO createReqVO) {
        return success(serverProcessService.createServerProcess(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新服务器进程关系")
    @PreAuthorize("@ss.hasPermission('pdeploy:server-process:update')")
    public CommonResult<Boolean> updateServerProcess(@Valid @RequestBody ServerProcessUpdateReqVO updateReqVO) {
        serverProcessService.updateServerProcess(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除服务器进程关系")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:server-process:delete')")
    public CommonResult<Boolean> deleteServerProcess(@RequestParam("id") Long id) {
        serverProcessService.deleteServerProcess(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得服务器进程关系")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:server-process:query')")
    public CommonResult<ServerProcessRespVO> getServerProcess(@RequestParam("id") Long id) {
        ServerProcessDO serverProcess = serverProcessService.getServerProcess(id);
        return success(ServerProcessConvert.INSTANCE.convert(serverProcess));
    }

    @GetMapping("/list")
    @ApiOperation("获得服务器进程关系列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:server-process:query')")
    public CommonResult<List<ServerProcessRespVO>> getServerProcessList(@RequestParam("ids") Collection<Long> ids) {
        List<ServerProcessDO> list = serverProcessService.getServerProcessList(ids);
        return success(ServerProcessConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得服务器进程关系分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:server-process:query')")
    public CommonResult<PageResult<ServerProcessRespVO>> getServerProcessPage(@Valid ServerProcessPageReqVO pageVO) {
        PageResult<ServerProcessDO> pageResult = serverProcessService.getServerProcessPage(pageVO);
        return success(ServerProcessConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出服务器进程关系 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:server-process:export')")
    @OperateLog(type = EXPORT)
    public void exportServerProcessExcel(@Valid ServerProcessExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ServerProcessDO> list = serverProcessService.getServerProcessList(exportReqVO);
        // 导出 Excel
        List<ServerProcessExcelVO> datas = ServerProcessConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "服务器进程关系.xls", "数据", ServerProcessExcelVO.class, datas);
    }

}
