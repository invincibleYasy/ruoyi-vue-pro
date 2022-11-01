package cn.iocoder.yudao.module.pdeploy.controller.admin.serverprocess.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 服务器进程关系 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ServerProcessBaseVO {

    @ApiModelProperty(value = "服务器")
    private Long serverId;

    @ApiModelProperty(value = "进程")
    private Long processId;

}
