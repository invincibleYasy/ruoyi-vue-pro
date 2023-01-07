package cn.iocoder.yudao.module.pdeploy.dal.dataobject.baseline;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 基线版本 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_baseline")
@KeySequence("pdeploy_baseline_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaselineDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 基线名称
     */
    private String name;
    /**
     * 基线版本
     */
    private String version;
    /**
     * 主配置
     */
    private String baselineConfYaml;
    /**
     * ccpass配置
     */
    private String baselineConfJson;
    /**
     * 备注
     */
    private String remark;

}
