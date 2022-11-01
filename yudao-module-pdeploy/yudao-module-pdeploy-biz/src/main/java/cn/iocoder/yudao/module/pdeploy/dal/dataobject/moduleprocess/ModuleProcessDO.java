package cn.iocoder.yudao.module.pdeploy.dal.dataobject.moduleprocess;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 模块进程关系 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_module_process")
@KeySequence("pdeploy_module_process_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleProcessDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 模块
     */
    private Long moduleId;
    /**
     * 进程
     */
    private Long processId;
    /**
     * 备注
     */
    private String remark;

}
