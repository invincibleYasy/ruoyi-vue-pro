package cn.iocoder.yudao.module.pdeploy.controller.admin.baseline;

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

import cn.iocoder.yudao.module.pdeploy.controller.admin.baseline.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline.BaselineDO;
import cn.iocoder.yudao.module.pdeploy.convert.baseline.BaselineConvert;
import cn.iocoder.yudao.module.pdeploy.service.baseline.BaselineService;

@Api(tags = "管理后台 - 基线版本")
@RestController
@RequestMapping("/pdeploy/baseline")
@Validated
public class BaselineController {

    @Resource
    private BaselineService baselineService;

    @PostMapping("/create")
    @ApiOperation("创建基线版本")
    @PreAuthorize("@ss.hasPermission('pdeploy:baseline:create')")
    public CommonResult<Long> createBaseline(@Valid @RequestBody BaselineCreateReqVO createReqVO) {
        return success(baselineService.createBaseline(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新基线版本")
    @PreAuthorize("@ss.hasPermission('pdeploy:baseline:update')")
    public CommonResult<Boolean> updateBaseline(@Valid @RequestBody BaselineUpdateReqVO updateReqVO) {
        baselineService.updateBaseline(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除基线版本")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:baseline:delete')")
    public CommonResult<Boolean> deleteBaseline(@RequestParam("id") Long id) {
        baselineService.deleteBaseline(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得基线版本")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:baseline:query')")
    public CommonResult<BaselineRespVO> getBaseline(@RequestParam("id") Long id) {
        BaselineDO baseline = baselineService.getBaseline(id);
        return success(BaselineConvert.INSTANCE.convert(baseline));
    }

    @GetMapping("/list")
    @ApiOperation("获得基线版本列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:baseline:query')")
    public CommonResult<List<BaselineRespVO>> getBaselineList(@RequestParam("ids") Collection<Long> ids) {
        List<BaselineDO> list = baselineService.getBaselineList(ids);
        return success(BaselineConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得基线版本分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:baseline:query')")
    public CommonResult<PageResult<BaselineRespVO>> getBaselinePage(@Valid BaselinePageReqVO pageVO) {
        PageResult<BaselineDO> pageResult = baselineService.getBaselinePage(pageVO);
        return success(BaselineConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出基线版本 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:baseline:export')")
    @OperateLog(type = EXPORT)
    public void exportBaselineExcel(@Valid BaselineExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BaselineDO> list = baselineService.getBaselineList(exportReqVO);
        // 导出 Excel
        List<BaselineExcelVO> datas = BaselineConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "基线版本.xls", "数据", BaselineExcelVO.class, datas);
    }

}
