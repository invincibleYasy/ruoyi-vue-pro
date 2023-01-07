package cn.iocoder.yudao.module.pdeploy.dal.dataobject.project;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 私有项目 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_project")
@KeySequence("pdeploy_project_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDO extends BaseDO {

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

    private String projConfYaml;

    private String projConfJson;

    private String midwareCustomTags;

    private String allProjTags;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;

}
