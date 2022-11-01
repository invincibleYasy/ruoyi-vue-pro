import request from '@/utils/request'

// 创建进程
export function createProcess(data) {
  return request({
    url: '/pdeploy/process/create',
    method: 'post',
    data: data
  })
}

// 更新进程
export function updateProcess(data) {
  return request({
    url: '/pdeploy/process/update',
    method: 'put',
    data: data
  })
}

// 删除进程
export function deleteProcess(id) {
  return request({
    url: '/pdeploy/process/delete?id=' + id,
    method: 'delete'
  })
}

// 获得进程
export function getProcess(id) {
  return request({
    url: '/pdeploy/process/get?id=' + id,
    method: 'get'
  })
}

// 获得进程分页
export function getProcessPage(query) {
  return request({
    url: '/pdeploy/process/page',
    method: 'get',
    params: query
  })
}
export function getAllProcesses() {
  const params = {
    pageNo: 1,
    pageSize: 9999,
    enabled: true
  }
  return request({
    url: '/pdeploy/process/page',
    method: 'get',
    params
  })
}

// 导出进程 Excel
export function exportProcessExcel(query) {
  return request({
    url: '/pdeploy/process/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
