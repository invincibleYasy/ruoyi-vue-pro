package cn.iocoder.yudao.module.pdeploy.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode BASELINE_NOT_EXISTS = new ErrorCode(1006000001, "基线版本不存在");
    ErrorCode CODEBASE_NOT_EXISTS = new ErrorCode(1006000002, "代码库不存在");
    ErrorCode MODULE_PROCESS_NOT_EXISTS = new ErrorCode(1006000003, "模块进程关系不存在");
    ErrorCode PROCESS_NOT_EXISTS = new ErrorCode(1006000004, "进程不存在");
    ErrorCode PROJECT_NOT_EXISTS = new ErrorCode(1006000005, "私有项目不存在");
    ErrorCode PROJECT_MODULE_NOT_EXISTS = new ErrorCode(1006000006, "项目模块关系不存在");
    ErrorCode SERVER_NOT_EXISTS = new ErrorCode(1006000007, "服务器不存在");
    ErrorCode SERVER_PROCESS_NOT_EXISTS = new ErrorCode(1006000008, "服务器进程关系不存在");
    ErrorCode MODULE_NOT_EXISTS = new ErrorCode(1006000009, "模块不存在");
    ErrorCode PROJECT_CONF_NOT_EXISTS = new ErrorCode(1006000010, "项目配置不存在");
    ErrorCode PROJECT_EXTEND_ERROR = new ErrorCode(1006000011, "继承的projectId和当前的projectId不能为空");
    ErrorCode PROJECT_EXTEND_NO_SERVER_ERROR = new ErrorCode(1006000012, "当前继承的项目服务器列表为空");
    ErrorCode PROJECT_EXTEND_NO_MODULE_ERROR = new ErrorCode(1006000013, "当前项目模块列表为空");
    ErrorCode PROJECT_SERVER_UPDATE_ERROR = new ErrorCode(1006000014, "项目服务器更新失败，服务器或进程为空");


}
