import { useAxios } from '@/hooks/web/useAxios'
import { ProjectConfVO,ProjectConfPageReqVO,ProjectConfExcelReqVO } from './types'

const request = useAxios()

// 查询项目配置列表
export const getPostPageApi = async (params: ProjectConfPageReqVO) => {
    return await request.get({ url: '/pdeploy/project-conf/page', params })
}

// 查询项目配置详情
export const getPostApi = async (id: number) => {
    return await request.get({ url: '/pdeploy/project-conf/get?id=' + id })
}

// 新增项目配置
export const createPostApi = async (data: ProjectConfVO) => {
    return await request.post({ url: '/pdeploy/project-conf/create', data })
}

// 修改项目配置
export const updatePostApi = async (data: ProjectConfVO) => {
    return await request.put({ url: '/pdeploy/project-conf/update', data })
}

// 删除项目配置
export const deletePostApi = async (id: number) => {
    return await request.delete({ url: '/pdeploy/project-conf/delete?id=' + id })
}

// 导出项目配置 Excel
export const exportPostApi = async (params: ProjectConfExcelReqVO) => {
    return await request.download({ url: '/pdeploy/project-conf/export-excel', params })
}
