package cn.iocoder.yudao.module.pdeploy.dal.dataobject.process;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 进程 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_process")
@KeySequence("pdeploy_process_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 基线版本
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Long baselineId;
    /**
     * 代码库
     */
    private Long codebaseId;
    /**
     * 进程名称
     */
    private String name;
    /**
     * 进程标签
     */
    private String tag;

    private String tagFilter;
    /**
     * 进程版本
     */
    private String version;
    /**
     * 进程类型
     *
     * 枚举 {@link TODO process_type 对应的类}
     */
    private Integer processType;
    /**
     * CPU
     */
    private Integer cpu;
    /**
     * 内存(GB)
     */
    private Integer memory;
    /**
     * 高可用
     */
    private Boolean isHa;
    /**
     * 备注
     */
    private String remark;

}
