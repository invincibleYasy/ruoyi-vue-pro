import request from '@/utils/request'

// 创建基线版本
export function createBaseline(data) {
  return request({
    url: '/pdeploy/baseline/create',
    method: 'post',
    data: data
  })
}

// 更新基线版本
export function updateBaseline(data) {
  return request({
    url: '/pdeploy/baseline/update',
    method: 'put',
    data: data
  })
}

// 删除基线版本
export function deleteBaseline(id) {
  return request({
    url: '/pdeploy/baseline/delete?id=' + id,
    method: 'delete'
  })
}

// 获得基线版本
export function getBaseline(id) {
  return request({
    url: '/pdeploy/baseline/get?id=' + id,
    method: 'get'
  })
}

// 获得基线版本分页
export function getBaselinePage(query) {
  return request({
    url: '/pdeploy/baseline/page',
    method: 'get',
    params: query
  })
}
export function getAllBaselines() {
  const params = {
    pageNo: 1,
    pageSize: 9999,
    enabled: true
  }
  return request({
    url: '/pdeploy/baseline/page',
    method: 'get',
    params
  })
}


// 导出基线版本 Excel
export function exportBaselineExcel(query) {
  return request({
    url: '/pdeploy/baseline/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
