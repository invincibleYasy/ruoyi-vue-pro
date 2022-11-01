package cn.iocoder.yudao.module.pdeploy.controller.admin.server;

import cn.iocoder.yudao.module.pdeploy.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.pdeploy.service.serverprocess.ServerProcessService;
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
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;

import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.server.ServerDO;
import cn.iocoder.yudao.module.pdeploy.convert.server.ServerConvert;
import cn.iocoder.yudao.module.pdeploy.service.server.ServerService;

@Api(tags = "管理后台 - 服务器")
@RestController
@RequestMapping("/pdeploy/server")
@Validated
public class ServerController {

    @Resource
    private ServerService serverService;
    @Resource
    private ServerProcessService serverProcessService;

    @PostMapping("/create")
    @ApiOperation("创建服务器")
    @PreAuthorize("@ss.hasPermission('pdeploy:server:create')")
    public CommonResult<Long> createServer(@Valid @RequestBody ServerCreateReqVO createReqVO) {
        return success(serverService.createServer(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新服务器")
    @PreAuthorize("@ss.hasPermission('pdeploy:server:update')")
    public CommonResult<Boolean> updateServer(@Valid @RequestBody ServerUpdateReqVO updateReqVO) {
        serverService.updateServer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除服务器")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:server:delete')")
    public CommonResult<Boolean> deleteServer(@RequestParam("id") Long id) {
        serverService.deleteServer(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得服务器")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:server:query')")
    public CommonResult<ServerRespVO> getServer(@RequestParam("id") Long id) {
        ServerDO server = serverService.getServer(id);
        ServerRespVO serverRespVO = ServerConvert.INSTANCE.convert(server);
        List<ProcessDO> processes = serverProcessService.getProcessesByServerId(id);
        serverRespVO.setProcessIds(processes.stream().map(ProcessDO::getId).collect(Collectors.toSet()));
        serverRespVO.setProcesses(processes);
        return success(serverRespVO);
    }

    @GetMapping("/list")
    @ApiOperation("获得服务器列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:server:query')")
    public CommonResult<List<ServerRespVO>> getServerList(@RequestParam("ids") Collection<Long> ids) {
        List<ServerDO> list = serverService.getServerList(ids);
        List<ServerRespVO> serverRespVOS = ServerConvert.INSTANCE.convertList(list);
        if(CollectionUtils.isNotEmpty(serverRespVOS)){
            serverRespVOS.forEach(serverRespVO -> {
                List<ProcessDO> processes = serverProcessService.getProcessesByServerId(serverRespVO.getId());
                serverRespVO.setProcesses(processes);
            });
        }
        return success(serverRespVOS);
    }

    @GetMapping("/page")
    @ApiOperation("获得服务器分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:server:query')")
    public CommonResult<PageResult<ServerRespVO>> getServerPage(@Valid ServerPageReqVO pageVO) {
        PageResult<ServerDO> pageResult = serverService.getServerPage(pageVO);
        return success(ServerConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出服务器 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:server:export')")
    @OperateLog(type = EXPORT)
    public void exportServerExcel(@Valid ServerExportReqVO exportReqVO,
                                  HttpServletResponse response) throws IOException {
        List<ServerDO> list = serverService.getServerList(exportReqVO);
        // 导出 Excel
        List<ServerExcelVO> datas = ServerConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "服务器.xls", "数据", ServerExcelVO.class, datas);
    }

}
