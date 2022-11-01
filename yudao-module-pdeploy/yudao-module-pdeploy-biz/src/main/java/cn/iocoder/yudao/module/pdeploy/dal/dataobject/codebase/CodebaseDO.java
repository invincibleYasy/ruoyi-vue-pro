package cn.iocoder.yudao.module.pdeploy.dal.dataobject.codebase;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 代码库 DO
 *
 * @author 芋道源码
 */
@TableName("pdeploy_codebase")
@KeySequence("pdeploy_codebase_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodebaseDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String gitUrl;
    /**
     * 备注
     */
    private String remark;

}
