package cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectconf;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目配置 DO
 *
 * @author 管理员
 */
@TableName("pdeploy_project_conf")
@KeySequence("pdeploy_project_conf_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectConfDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 基线版本
     */
    private Long baselineId;
    /**
     *  所属项目
     */
    private Long projectId;

    private String tag;

    private String tagFilter;
    /**
     * 键
     */
    private String confKey;
    /**
     * 值
     */
    private String confValue;

    private String confValuePlaceholder;
    /**
     * 是否需修改
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean modifyFlag;
    /**
     * 配置类型
     *
     * 枚举 {@link TODO ansible_conf_type 对应的类}
     */
    private Integer type;
    /**
     * 键描述
     */
    private String keyDesc;
    /**
     * 配置版本
     */
    private String version;

}
