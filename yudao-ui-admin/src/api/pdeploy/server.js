import request from '@/utils/request'

// 创建服务器
export function createServer(data) {
  return request({
    url: '/pdeploy/server/create',
    method: 'post',
    data: data
  })
}

// 更新服务器
export function updateServer(data) {
  return request({
    url: '/pdeploy/server/update',
    method: 'put',
    data: data
  })
}

// 删除服务器
export function deleteServer(id) {
  return request({
    url: '/pdeploy/server/delete?id=' + id,
    method: 'delete'
  })
}

// 获得服务器
export function getServer(id) {
  return request({
    url: '/pdeploy/server/get?id=' + id,
    method: 'get'
  })
}

// 获得服务器分页
export function getServerPage(query) {
  return request({
    url: '/pdeploy/server/page',
    method: 'get',
    params: query
  })
}

export function getAllServers() {
  const params = {
    pageNo: 1,
    pageSize: 9999,
    enabled: true
  }
  return request({
    url: '/pdeploy/server/page',
    method: 'get',
    params
  })
}

export function listServersByIds(serverIds){
  let join = serverIds.join(',');
  const params = {
    ids: join,
  }
  return request({
    url: '/pdeploy/server/list',
    method: 'get',
    params
  })
}

// 导出服务器 Excel
export function exportServerExcel(query) {
  return request({
    url: '/pdeploy/server/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
