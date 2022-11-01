package cn.iocoder.yudao.module.pdeploy.controller.admin.codebase;

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

import cn.iocoder.yudao.module.pdeploy.controller.admin.codebase.vo.*;
import cn.iocoder.yudao.module.pdeploy.dal.dataobject.codebase.CodebaseDO;
import cn.iocoder.yudao.module.pdeploy.convert.codebase.CodebaseConvert;
import cn.iocoder.yudao.module.pdeploy.service.codebase.CodebaseService;

@Api(tags = "管理后台 - 代码库")
@RestController
@RequestMapping("/pdeploy/codebase")
@Validated
public class CodebaseController {

    @Resource
    private CodebaseService codebaseService;

    @PostMapping("/create")
    @ApiOperation("创建代码库")
    @PreAuthorize("@ss.hasPermission('pdeploy:codebase:create')")
    public CommonResult<Long> createCodebase(@Valid @RequestBody CodebaseCreateReqVO createReqVO) {
        return success(codebaseService.createCodebase(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新代码库")
    @PreAuthorize("@ss.hasPermission('pdeploy:codebase:update')")
    public CommonResult<Boolean> updateCodebase(@Valid @RequestBody CodebaseUpdateReqVO updateReqVO) {
        codebaseService.updateCodebase(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除代码库")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:codebase:delete')")
    public CommonResult<Boolean> deleteCodebase(@RequestParam("id") Long id) {
        codebaseService.deleteCodebase(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得代码库")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:codebase:query')")
    public CommonResult<CodebaseRespVO> getCodebase(@RequestParam("id") Long id) {
        CodebaseDO codebase = codebaseService.getCodebase(id);
        return success(CodebaseConvert.INSTANCE.convert(codebase));
    }

    @GetMapping("/list")
    @ApiOperation("获得代码库列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('pdeploy:codebase:query')")
    public CommonResult<List<CodebaseRespVO>> getCodebaseList(@RequestParam("ids") Collection<Long> ids) {
        List<CodebaseDO> list = codebaseService.getCodebaseList(ids);
        return success(CodebaseConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得代码库分页")
    @PreAuthorize("@ss.hasPermission('pdeploy:codebase:query')")
    public CommonResult<PageResult<CodebaseRespVO>> getCodebasePage(@Valid CodebasePageReqVO pageVO) {
        PageResult<CodebaseDO> pageResult = codebaseService.getCodebasePage(pageVO);
        return success(CodebaseConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出代码库 Excel")
    @PreAuthorize("@ss.hasPermission('pdeploy:codebase:export')")
    @OperateLog(type = EXPORT)
    public void exportCodebaseExcel(@Valid CodebaseExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CodebaseDO> list = codebaseService.getCodebaseList(exportReqVO);
        // 导出 Excel
        List<CodebaseExcelVO> datas = CodebaseConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "代码库.xls", "数据", CodebaseExcelVO.class, datas);
    }

}
