export type ProjectConfVO = {
    id: number
    projectId: number
    confKey: string
    confValue: string
    modifyFlag: boolean
    type: number
    keyDesc: string
    version: string
}

export type ProjectConfPageReqVO = {
    projectId: number
    confKey: string
    confValue: string
    modifyFlag: boolean
    type: number
    keyDesc: string
    version: string
    createTime: date
}

export type ProjectConfExcelReqVO = {
    projectId: number
    confKey: string
    confValue: string
    modifyFlag: boolean
    type: number
    keyDesc: string
    version: string
    createTime: date
}