<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label=" 所属项目" prop="projectId">
        <el-select v-model="queryParams.projectId" placeholder="请选择 所属项目" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="键" prop="confKey">
        <el-input v-model="queryParams.confKey" placeholder="请输入键" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="值" prop="confValue">
        <el-input v-model="queryParams.confValue" placeholder="请输入值" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
<!--      <el-form-item label="是否需修改" prop="modifyFlag">-->
<!--        <el-select v-model="queryParams.modifyFlag" placeholder="请选择是否需修改" clearable size="small">-->
<!--          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_BOOLEAN_STRING)"-->
<!--                       :key="dict.value" :label="dict.label" :value="dict.value"/>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="配置类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择配置类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.ANSIBLE_CONF_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="键描述" prop="keyDesc">
        <el-input v-model="queryParams.keyDesc" placeholder="请输入键描述" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="配置版本" prop="version">
        <el-input v-model="queryParams.version" placeholder="请输入配置版本" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['pdeploy:project-conf:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['pdeploy:project-conf:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label=" 所属项目" align="center" prop="projectId" />
      <el-table-column label="配置标签" align="center" prop="tag" />
      <el-table-column label="键" align="center" prop="confKey" />
      <el-table-column label="值" align="center" prop="confValue" />
<!--      <el-table-column label="是否需修改" align="center" prop="modifyFlag">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.modifyFlag" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="配置类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.ANSIBLE_CONF_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="键描述" align="center" prop="keyDesc" />
      <el-table-column label="配置版本" align="center" prop="version" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['pdeploy:project-conf:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['pdeploy:project-conf:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label=" 所属项目" prop="projectId">
          <el-select v-model="form.projectId" placeholder="请选择 所属项目">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="键" prop="confKey">
          <el-input v-model="form.confKey" placeholder="请输入键" />
        </el-form-item>
        <el-form-item label="值" prop="confValue">
          <el-input v-model="form.confValue" placeholder="请输入值" />
        </el-form-item>
<!--        <el-form-item label="是否需修改是否需修改是否需修改是否需修改" prop="modifyFlag">-->
<!--          <el-radio-group v-model="form.modifyFlag">-->
<!--            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_BOOLEAN_STRING)"-->
<!--                      :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
        <el-form-item label="配置类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.ANSIBLE_CONF_TYPE)"
                      :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="键描述" prop="keyDesc">
          <el-input v-model="form.keyDesc" placeholder="请输入键描述" />
        </el-form-item>
        <el-form-item label="配置版本" prop="version">
          <el-input v-model="form.version" placeholder="请输入配置版本" />
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
import { createProjectConf, updateProjectConf, deleteProjectConf, getProjectConf, getProjectConfPage, exportProjectConfExcel } from "@/api/pdeploy/projectConf";

export default {
  name: "ProjectConf",
  components: {
  },
  data() {
    return {
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
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        projectId: null,
        confKey: null,
        confValue: null,
        modifyFlag: null,
        type: null,
        keyDesc: null,
        version: null,
        createTime: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getProjectConfPage(this.queryParams).then(response => {
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
    /** 重置按钮操作 */
    resetQuery() {
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
      this.$modal.confirm('是否确认删除项目配置编号为"' + id + '"的数据项?').then(function() {
          return deleteProjectConf(id);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有项目配置数据项?').then(() => {
          this.exportLoading = true;
          return exportProjectConfExcel(params);
        }).then(response => {
          this.$download.excel(response, '项目配置.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
