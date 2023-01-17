package cn.iocoder.yudao.module.pdeploy.controller.admin.server.vo;

import lombok.*;

import java.util.*;

import io.swagger.annotations.*;

import javax.validation.constraints.*;

/**
 * 服务器 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ServerBaseVO {

    @ApiModelProperty(value = "基线版本")
    private Long baselineId;

    @ApiModelProperty(value = "项目")
    private Long projectId;

    @ApiModelProperty(value = "服务器名称")
    private String name;

    @ApiModelProperty(value = "服务器IP")
    private String ip;

    @ApiModelProperty(value = "CPU")
    private Integer cpu;

    @ApiModelProperty(value = "内存(GB)")
    private Integer memory;

    @ApiModelProperty(value = "所属环境")
    private Integer envType;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "备注")
    private String remark;

}
