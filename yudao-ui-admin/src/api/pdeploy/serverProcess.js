import request from '@/utils/request'

// 创建服务器进程关系
export function createServerProcess(data) {
  return request({
    url: '/pdeploy/server-process/create',
    method: 'post',
    data: data
  })
}

// 更新服务器进程关系
export function updateServerProcess(data) {
  return request({
    url: '/pdeploy/server-process/update',
    method: 'put',
    data: data
  })
}

// 删除服务器进程关系
export function deleteServerProcess(id) {
  return request({
    url: '/pdeploy/server-process/delete?id=' + id,
    method: 'delete'
  })
}

// 获得服务器进程关系
export function getServerProcess(id) {
  return request({
    url: '/pdeploy/server-process/get?id=' + id,
    method: 'get'
  })
}

// 获得服务器进程关系分页
export function getServerProcessPage(query) {
  return request({
    url: '/pdeploy/server-process/page',
    method: 'get',
    params: query
  })
}

// 导出服务器进程关系 Excel
export function exportServerProcessExcel(query) {
  return request({
    url: '/pdeploy/server-process/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
