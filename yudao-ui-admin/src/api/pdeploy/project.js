import request from '@/utils/request'

// 创建私有项目
export function createProject(data) {
  return request({
    url: '/pdeploy/project/create',
    method: 'post',
    data: data
  })
}

// 更新私有项目
export function updateProject(data) {
  return request({
    url: '/pdeploy/project/update',
    method: 'put',
    data: data
  })
}


// 更新私有项目
export function updateProjectServer(data) {
  return request({
    url: '/pdeploy/project/updateProjectServer',
    method: 'put',
    data: data
  })
}

// 删除私有项目
export function deleteProject(id) {
  return request({
    url: '/pdeploy/project/delete?id=' + id,
    method: 'delete'
  })
}

// 删除私有项目
export function deleteProjectServer(id) {
  return request({
    url: '/pdeploy/project/deleteProjectServer?serverId=' + id,
    method: 'delete'
  })
}

// 获得私有项目
export function getProject(id) {
  return request({
    url: '/pdeploy/project/get?id=' + id,
    method: 'get'
  })
}

// 获得私有项目
export function getProjectAll(id) {
  return request({
    url: '/pdeploy/project/getAll?id=' + id,
    method: 'get'
  })
}

// 获得私有项目分页
export function getProjectPage(query) {
  return request({
    url: '/pdeploy/project/page',
    method: 'get',
    params: query
  })
}

export function getAllProjects() {
  const params = {
    pageNo: 1,
    pageSize: 9999,
    enabled: true
  }
  return request({
    url: '/pdeploy/project/page',
    method: 'get',
    params
  })
}

export function genServers(data) {
  return request({
    url: "pdeploy/project/genServers",
    method: 'post',
    data: data,
  })
}

export function mergeServers(data) {
  return request({
    url: "pdeploy/project/mergeServer",
    method: 'post',
    data: data,
  })
}

export function getDeployInfo(id) {
  return request({
    url: '/pdeploy/project/get-deploy-info?id=' + id,
    method: 'get'
  })
}

export function syncProjectConf(data) {
  return request({
    url: "pdeploy/project/syncProjectConf",
    method: 'post',
    data: data,
  })
}

export function showProjectConf(data) {
  return request({
    url: "pdeploy/project/showProjectConf",
    method: 'post',
    data: data,
  })
}

// 导出私有项目 Excel
export function exportProjectExcel(query) {
  return request({
    url: '/pdeploy/project/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
