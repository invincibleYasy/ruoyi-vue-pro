import request from '@/utils/request'

// 创建模块
export function createModule(data) {
  return request({
    url: '/pdeploy/module/create',
    method: 'post',
    data: data
  })
}

// 更新模块
export function updateModule(data) {
  return request({
    url: '/pdeploy/module/update',
    method: 'put',
    data: data
  })
}

// 删除模块
export function deleteModule(id) {
  return request({
    url: '/pdeploy/module/delete?id=' + id,
    method: 'delete'
  })
}

// 获得模块
export function getModule(id) {
  return request({
    url: '/pdeploy/module/get?id=' + id,
    method: 'get'
  })
}

// 获得模块分页
export function getModulePage(query) {
  return request({
    url: '/pdeploy/module/page',
    method: 'get',
    params: query
  })
}

export function getAllModules() {
  const params = {
    pageNo: 1,
    pageSize: 9999,
    enabled: true
  }
  return request({
    url: '/pdeploy/module/page',
    method: 'get',
    params
  })
}

export function listModulesByIds(moduleIds){
  let join = moduleIds.join(',');
  const params = {
    ids: join,
  }
  return request({
    url: '/pdeploy/module/list',
    method: 'get',
    params
  })
}

// 导出模块 Excel
export function exportModuleExcel(query) {
  return request({
    url: '/pdeploy/module/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
