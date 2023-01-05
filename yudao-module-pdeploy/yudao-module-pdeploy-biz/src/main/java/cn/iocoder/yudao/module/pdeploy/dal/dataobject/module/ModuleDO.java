package cn.iocoder.yudao.module.pdeploy.dal.dataobject.module;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 模块 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_module")
@KeySequence("pdeploy_module_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDO extends BaseDO {

    /**
     * 主键
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
     * 模块名称
     */
    private String name;
    /**
     * 模块标签
     */
    private String tag;
    /**
     * 依赖的中间件标签
     */
    private String midwareTags;
    /**
     * 依赖的镜像
     */
    private String imageTags;
    /**
     * 模块类型
     *
     * 枚举 {@link TODO module_type 对应的类}
     */
    private Integer moduleType;
    /**
     * 估时
     */
    private Long estimatedTime;
    /**
     * 备注
     */
    private String remark;


}
