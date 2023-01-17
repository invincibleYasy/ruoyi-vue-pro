package cn.iocoder.yudao.module.pdeploy.dal.dataobject.server;

import lombok.*;


import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 服务器 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_server")
@KeySequence("pdeploy_server_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 基线版本
     * <p>
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Long baselineId;
    /**
     * 项目
     */
    private Long projectId;
    /**
     * 服务器名称
     */
    private String name;

    private String tag;
    /**
     * 服务器IP
     */
    private String ip;
    /**
     * CPU
     */
    private Integer cpu;

    private Integer envType;
    /**
     * 内存(GB)
     */
    private Integer memory;
    /**
     * 备注
     */
    private String remark;


}
