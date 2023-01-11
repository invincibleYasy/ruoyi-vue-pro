package cn.iocoder.yudao.module.pdeploy.service.project;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DynamicVarsValFilter {

    @Override
    public boolean equals(Object obj) {
        List<Object> delKeys = new ArrayList<>();
        List<Object> showValues = new ArrayList<>();
        if (obj instanceof Map) {
            ((Map<?, ?>) obj).forEach((key, value) -> {
                if (null != value) {
                    if ("basic_all__docker_registry".equals(value.toString())) {
                        showValues.add(value);
                    } else {
                        delKeys.add(key);
                    }
                } else {
                    delKeys.add(key);
                }

            });
            delKeys.forEach(((Map<?, ?>) obj)::remove);
        }
        return !(showValues.size() > 0);
    }
}
