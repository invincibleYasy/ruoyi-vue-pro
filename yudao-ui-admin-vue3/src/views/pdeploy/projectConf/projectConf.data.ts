import { reactive } from 'vue'
import { useI18n } from '@/hooks/web/useI18n'
import { CrudSchema, useCrudSchemas } from '@/hooks/web/useCrudSchemas'
import { DICT_TYPE } from '@/utils/dict'
const { t } = useI18n() // 国际化
// 表单校验
export const rules = reactive({
})
// CrudSchema
const crudSchemas = reactive<CrudSchema[]>([
    {
      label: 'ID',
      field: 'id',
      type: 'index',
      form: {
         show: false
      },
      detail: {
         show: false
      }
    },
    {
      label: ' 所属项目',
      field: 'projectId',
      form: {
          show: true,
      },
      search: {
         show: true,
         component: 'Select',
         componentProps: {
             option: [{'','请选择字典生成'}]
         }
      }
    },
    {
      label: '键',
      field: 'confKey',
      form: {
          show: true,
      },
      search: {
         show: true
      }
    },
    {
      label: '值',
      field: 'confValue',
      form: {
          show: true,
      },
      search: {
         show: true
      }
    },
    {
      label: '是否需修改',
      field: 'modifyFlag',
      dictType: DICT_TYPE.INFRA_BOOLEAN_STRING,
      search: {
         show: true
      }
    },
    {
      label: '配置类型',
      field: 'type',
      dictType: DICT_TYPE.ANSIBLE_CONF_TYPE,
      search: {
         show: true
      }
    },
    {
      label: '键描述',
      field: 'keyDesc',
      form: {
          show: true,
      },
      search: {
         show: true
      }
    },
    {
      label: '配置版本',
      field: 'version',
      form: {
          show: true,
      },
      search: {
         show: true
      }
    },
    {
      label: '创建时间',
      field: 'createTime',
      form: {
         false
      },
      search: {
         show: true,
         component: 'DatePicker',
         componentProps: {
             type: 'datetimerange',
             valueFormat: 'YYYY-MM-DD HH:mm:ss'
         }
      }
    },
    {
        label: t('table.action'),
        field: 'action',
        width: '240px',
        form: {
            show: false
        },
        detail: {
            show: false
        }
    }
])

export const { allSchemas } = useCrudSchemas(crudSchemas)