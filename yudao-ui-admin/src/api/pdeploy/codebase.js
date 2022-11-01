import request from '@/utils/request'

// 创建代码库
export function createCodebase(data) {
  return request({
    url: '/pdeploy/codebase/create',
    method: 'post',
    data: data
  })
}

// 更新代码库
export function updateCodebase(data) {
  return request({
    url: '/pdeploy/codebase/update',
    method: 'put',
    data: data
  })
}

// 删除代码库
export function deleteCodebase(id) {
  return request({
    url: '/pdeploy/codebase/delete?id=' + id,
    method: 'delete'
  })
}

// 获得代码库
export function getCodebase(id) {
  return request({
    url: '/pdeploy/codebase/get?id=' + id,
    method: 'get'
  })
}

// 获得代码库分页
export function getCodebasePage(query) {
  return request({
    url: '/pdeploy/codebase/page',
    method: 'get',
    params: query
  })
}
export function getAllCodebases() {
  const params = {
    pageNo: 1,
    pageSize: 9999,
    enabled: true
  }
  return request({
    url: '/pdeploy/codebase/page',
    method: 'get',
    params
  })
}
// 导出代码库 Excel
export function exportCodebaseExcel(query) {
  return request({
    url: '/pdeploy/codebase/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
