package cn.iocoder.yudao.module.pdeploy.service.project;

import org.springframework.stereotype.Service;

@Service
public class DynamicVarsValFilter {

    @Override
    public boolean equals(Object obj) {
        if (null != obj){
            return !"basic_all__docker_registry".equals(obj.toString());
        }
        return true;
    }
}
