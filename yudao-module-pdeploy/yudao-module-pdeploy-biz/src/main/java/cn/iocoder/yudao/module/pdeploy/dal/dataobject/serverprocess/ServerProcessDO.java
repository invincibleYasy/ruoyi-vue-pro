package cn.iocoder.yudao.module.pdeploy.dal.dataobject.serverprocess;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 服务器进程关系 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_server_process")
@KeySequence("pdeploy_server_process_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerProcessDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 服务器
     */
    private Long serverId;
    /**
     * 进程
     */
    private Long processId;

}
