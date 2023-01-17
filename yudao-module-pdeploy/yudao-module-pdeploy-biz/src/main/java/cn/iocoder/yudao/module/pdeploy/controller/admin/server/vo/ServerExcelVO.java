package cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 服务器 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ServerExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty(value = "基线版本", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Long baselineId;

    @ExcelProperty("项目")
    private Long projectId;

    @ExcelProperty("服务器名称")
    private String name;

    @ExcelProperty("服务器IP")
    private String ip;

    @ExcelProperty("CPU")
    private Integer cpu;

    @ExcelProperty("内存(GB)")
    private Integer memory;

    @ExcelProperty(value = "所属环境")
    private Integer envType;

    @ExcelProperty(value = "标签")
    private String tag;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("备注")
    private String remark;

}
