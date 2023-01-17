package cn.iocoder.yudao.module.pdeploy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum DeployStep {
    // 10 环境检查&初始化 ，20 中间件规划，30 中间件 ，40 中间件初始化，50 应用 ，60 应用初始化，70 应用自检

    CHECK_AND_INIT_ENV(10),
    PLAN_MID(20),
    SYNC_MID_IMAGES(30),
    SYNC_MOD_IMAGES(40),
    DEPLOY_MID(50),
    INIT_MID(60),
    DEPLOY_APP(70),
    INIT_APP(80),
    CHECK_APP(90);

    @Getter
    private Integer value;

    public static DeployStep getByValue(Integer value) {
        return Arrays.stream(DeployStep.values()).filter(s -> s.getValue().equals(value)).findFirst().orElse(null);
    }

}
