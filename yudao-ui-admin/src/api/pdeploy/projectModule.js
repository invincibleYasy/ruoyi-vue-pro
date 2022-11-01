import request from '@/utils/request'

// 创建项目模块关系
export function createProjectModule(data) {
  return request({
    url: '/pdeploy/project-module/create',
    method: 'post',
    data: data
  })
}

// 更新项目模块关系
export function updateProjectModule(data) {
  return request({
    url: '/pdeploy/project-module/update',
    method: 'put',
    data: data
  })
}

// 删除项目模块关系
export function deleteProjectModule(id) {
  return request({
    url: '/pdeploy/project-module/delete?id=' + id,
    method: 'delete'
  })
}

// 获得项目模块关系
export function getProjectModule(id) {
  return request({
    url: '/pdeploy/project-module/get?id=' + id,
    method: 'get'
  })
}

// 获得项目模块关系分页
export function getProjectModulePage(query) {
  return request({
    url: '/pdeploy/project-module/page',
    method: 'get',
    params: query
  })
}

// 导出项目模块关系 Excel
export function exportProjectModuleExcel(query) {
  return request({
    url: '/pdeploy/project-module/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
