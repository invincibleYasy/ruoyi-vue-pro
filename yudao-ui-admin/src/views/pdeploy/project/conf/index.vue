<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label=" 所属基线" prop="baselineId" v-if="source === 1">
        <el-select v-model="queryParams.baselineId" placeholder="请选择所属基线" clearable size="small">
          <el-option v-for="dict in baselines"
                     :key="dict.id" :label="dict.name" :value="dict.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label=" 所属项目" prop="projectId" v-if="source === 1">
        <el-select v-model="queryParams.projectId" placeholder="请选择所属项目" clearable size="small">
          <el-option v-for="dict in projects"
                     :key="dict.id" :label="dict.name" :value="dict.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="配置键" prop="confKey">
        <el-input v-model="queryParams.confKey" placeholder="请输入键" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
<!--      <el-form-item label="Modify" prop="modifyFlag" v-if="source === 1">-->
<!--        <el-select v-model="queryParams.modifyFlag" placeholder="请选择是否需修改" clearable size="small">-->
<!--          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_YES_NO)"-->
<!--                     :key="dict.value" :label="dict.label" :value="dict.value"/>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="配置类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择配置类型" clearable size="small" v-if="source === 1">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.ANSIBLE_CONF_TYPE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="键描述" prop="keyDesc">
        <el-input v-model="queryParams.keyDesc" placeholder="请输入键描述" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="syncConf" v-if="source !==1">同步基线配置</el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="所属基线" align="center" prop="baselineId">
        <template slot-scope="scope">
          <dynamic-dict-tag :options="baselines" :value="scope.row.baselineId"/>
        </template>
      </el-table-column>
      <el-table-column label="所属项目" align="center" prop="projectId">
        <template slot-scope="scope">
          <dynamic-dict-tag :options="projects" :value="scope.row.projectId"/>
        </template>
      </el-table-column>
      <el-table-column label="配置标签" align="center" prop="tag"/>
      <el-table-column label="键" align="center" prop="confKey"/>
      <el-table-column label="值" align="center" prop="confValue"/>
<!--      <el-table-column label="是否需修改" align="center" prop="modifyFlag">-->
<!--        <template slot-scope="scope">-->
<!--          <el-switch-->
<!--            v-model="scope.row.modifyFlag"-->
<!--          />-->
<!--          &lt;!&ndash;          <dict-tag :type="DICT_TYPE.SYSTEM_YES_NO" :value="scope.row.modifyFlag"/>&ndash;&gt;-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="配置类型" align="center" prop="type">
        <template scope="scope">
          <dict-tag :type="DICT_TYPE.ANSIBLE_CONF_TYPE" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="键描述" align="center" prop="keyDesc"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['pdeploy:project-conf:update']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-if="source === 1"
                     v-hasPermi="['pdeploy:project-conf:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="键" prop="confKey">
          <el-input v-model="form.confKey" disabled/>
        </el-form-item>
        <el-form-item label="值" prop="confValue">
          <el-input v-model="form.confValue" placeholder="请输入值"/>
        </el-form-item>
<!--        <el-form-item label="是否需修改" prop="modifyFlag" v-if="source === 1">-->
<!--          <el-switch-->
<!--            v-model="form.modifyFlag"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item label="配置类型" prop="type" v-if="source === 1 ">
          <el-select v-model="form.type" placeholder="请选择配置类型">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.ANSIBLE_CONF_TYPE)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"/>
          </el-select>
        </el-form-item>
        <el-form-item label="关联进程" prop="processId" v-if="source === 1 ">
          <el-select v-model="form.processId" placeholder="请选择关联进程" filterable>
            <el-option v-for="dict in processes"
                       :key="dict.id" :label="dict.name" :value="dict.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="关联模块" prop="moduleId" v-if="source === 1 ">
          <el-select v-model="form.moduleId" placeholder="请选择关联模块" filterable>
            <el-option v-for="dict in modules"
                       :key="dict.id" :label="dict.name" :value="dict.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="键描述" prop="keyDesc">
          <el-input v-model="form.keyDesc" placeholder="请输入键描述"/>
        </el-form-item>
        <el-form-item label="配置版本" prop="version" v-if="source === 1">
          <el-input v-model="form.version" placeholder="请输入配置版本"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {getAllProjects, syncProjectConf} from "@/api/pdeploy/project";
import {
  createProjectConf,
  deleteProjectConf,
  exportProjectConfExcel,
  getProjectConf,
  getProjectConfPage,
  updateProjectConf
} from "@/api/pdeploy/projectConf";
import {getAllBaselines} from "@/api/pdeploy/baseline";
import {getAllProcesses} from "@/api/pdeploy/process";
import {getAllModules} from "@/api/pdeploy/module";

export default {
  name: "BaselineConf",
  props: {
    source: {
      type: Number,
      default: 1,
    },
    superConfType: {
      type: String,
      default: []
    },
    superProjectId: {
      type: Number,
      default: undefined,
    },
    superBaselineId: {
      type: Number,
      default: undefined,
    }
  },
  components: {},
  data() {
    return {
      projects: [{id: -1, name: '基线配置'}],
      baselines: [],
      processes: [],
      modules: [],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 项目配置列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      dateRangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        projectId: null,
        confKey: null,
        confValue: null,
        modifyFlag: 'true',
        type: null,
        types: [],
        keyDesc: null,
        version: null,
        baselineId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    console.log(this.superConfType)
    console.log(this.superProjectId)
    console.log(this.superBaselineId)
    if (this.superProjectId) {
      this.queryParams.projectId = this.superProjectId;
    }
    if (this.superBaselineId) {
      this.queryParams.baselineId = this.superBaselineId;
    }
    if (this.superConfType) {
      this.queryParams.type = this.superConfType;
    }
    getAllProjects().then(res => {
      this.projects = this.projects.concat(res.data.list);
    });
    getAllProcesses().then(res => {
      this.processes = res.data.list;
    })
    getAllModules().then(res => {
      this.modules = res.data.list;
    })
    getAllBaselines().then(res => {
      this.baselines = res.data.list;
    });
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 处理查询参数
      let params = {...this.queryParams};
      // this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      // 执行查询
      getProjectConfPage(params).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        projectId: undefined,
        confKey: undefined,
        confValue: undefined,
        modifyFlag: undefined,
        type: undefined,
        keyDesc: undefined,
        version: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    syncConf() {
      this.loading = true;
      let params = {
        baselineId: this.queryParams.baselineId,
        id: this.queryParams.projectId,
        type: this.queryParams.type
      }
      syncProjectConf(params).then(res => {
        this.loading = false
        this.handleQuery();
      })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加项目配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getProjectConf(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.id != null) {
          updateProjectConf(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createProjectConf(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除项目配置编号为"' + id + '"的数据项?').then(function () {
        return deleteProjectConf(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      // this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      // 执行导出
      this.$modal.confirm('是否确认导出所有项目配置数据项?').then(() => {
        this.exportLoading = true;
        return exportProjectConfExcel(params);
      }).then(response => {
        this.$download.excel(response, '项目配置.xls');
        this.exportLoading = false;
      }).catch(() => {
      });
    }
  }
};
</script>
