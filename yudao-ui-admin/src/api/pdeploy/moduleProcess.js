import request from '@/utils/request'

// 创建模块进程关系
export function createModuleProcess(data) {
  return request({
    url: '/pdeploy/module-process/create',
    method: 'post',
    data: data
  })
}

// 更新模块进程关系
export function updateModuleProcess(data) {
  return request({
    url: '/pdeploy/module-process/update',
    method: 'put',
    data: data
  })
}

// 删除模块进程关系
export function deleteModuleProcess(id) {
  return request({
    url: '/pdeploy/module-process/delete?id=' + id,
    method: 'delete'
  })
}

// 获得模块进程关系
export function getModuleProcess(id) {
  return request({
    url: '/pdeploy/module-process/get?id=' + id,
    method: 'get'
  })
}

// 获得模块进程关系分页
export function getModuleProcessPage(query) {
  return request({
    url: '/pdeploy/module-process/page',
    method: 'get',
    params: query
  })
}

// 导出模块进程关系 Excel
export function exportModuleProcessExcel(query) {
  return request({
    url: '/pdeploy/module-process/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
