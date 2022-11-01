import request from '@/utils/request'

// 创建项目配置
export function createProjectConf(data) {
  return request({
    url: '/pdeploy/project-conf/create',
    method: 'post',
    data: data
  })
}

// 更新项目配置
export function updateProjectConf(data) {
  return request({
    url: '/pdeploy/project-conf/update',
    method: 'put',
    data: data
  })
}

// 删除项目配置
export function deleteProjectConf(id) {
  return request({
    url: '/pdeploy/project-conf/delete?id=' + id,
    method: 'delete'
  })
}

// 获得项目配置
export function getProjectConf(id) {
  return request({
    url: '/pdeploy/project-conf/get?id=' + id,
    method: 'get'
  })
}

// 获得项目配置分页
export function getProjectConfPage(query) {
  return request({
    url: '/pdeploy/project-conf/page',
    method: 'get',
    params: query
  })
}

// 导出项目配置 Excel
export function exportProjectConfExcel(query) {
  return request({
    url: '/pdeploy/project-conf/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
