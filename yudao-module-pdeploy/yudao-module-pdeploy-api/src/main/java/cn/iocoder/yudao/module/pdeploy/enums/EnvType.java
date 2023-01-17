package cn.iocoder.yudao.module.pdeploy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EnvType {
    STAGING(1),
    PROD(2);
    @Getter
    private Integer value;
}
