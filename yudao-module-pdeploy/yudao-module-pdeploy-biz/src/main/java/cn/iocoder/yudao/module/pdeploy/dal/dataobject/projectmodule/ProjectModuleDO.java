package cn.iocoder.yudao.module.pdeploy.dal.dataobject.projectmodule;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目模块关系 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_project_module")
@KeySequence("pdeploy_project_module_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectModuleDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 项目
     */
    private Long projectId;
    /**
     * 模块
     */
    private Long moduleId;

}
