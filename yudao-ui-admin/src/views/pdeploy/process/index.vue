<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="基线版本" prop="baselineId">
        <el-select v-model="queryParams.baselineId" placeholder="请选择基线版本" clearable size="small">
          <el-option v-for="dict in baselines"
                     :key="dict.id" :label="dict.name" :value="dict.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="进程名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入进程名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>

      <el-form-item label="进程类型" prop="processType">
        <el-select v-model="queryParams.processType" placeholder="请选择进程类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.PROCESS_TYPE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="高可用" prop="isHa">
        <el-select v-model="queryParams.isHa" placeholder="请选择高可用" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_YES_NO)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="创建时间">-->
<!--        <el-date-picker v-model="dateRangeCreateTime" style="width: 240px" value-format="yyyy-MM-dd"-->
<!--                        type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['pdeploy:process:create']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :loading="exportLoading"
                   v-hasPermi="['pdeploy:process:export']">导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="基线版本" align="center" prop="baselineId">
        <template slot-scope="scope">
          <dynamic-dict-tag :options="baselines" :value="scope.row.baselineId"/>
        </template>
      </el-table-column>
      <el-table-column label="代码库" align="center" prop="codebaseId">
        <template slot-scope="scope">
          <dynamic-dict-tag :options="codebases" :value="scope.row.codebaseId"/>
        </template>
      </el-table-column>
      <el-table-column label="进程名称" align="center" prop="name"/>
      <el-table-column label="进程版本" align="center" prop="version"/>
      <el-table-column label="进程类型" align="center" prop="processType">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.PROCESS_TYPE" :value="scope.row.processType"/>
        </template>
      </el-table-column>
      <el-table-column label="CPU" align="center" prop="cpu"/>
      <el-table-column label="内存(GB)" align="center" prop="memory"/>
      <el-table-column label="高可用" align="center" prop="isHa">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_YES_NO" :value="String(scope.row.isHa)"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['pdeploy:process:update']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['pdeploy:process:delete']">删除
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
        <el-form-item label="进程名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入进程名称"/>
        </el-form-item>
        <el-form-item label="进程版本" prop="version">
          <el-input v-model="form.version" placeholder="请输入进程版本"/>
        </el-form-item>
        <el-form-item label="基线版本" prop="baselineId">
          <el-select v-model="form.baselineId" placeholder="请选择基线版本">
            <el-option v-for="dict in baselines"
                       :key="dict.id" :label="dict.name" :value="parseInt(dict.id)"/>
          </el-select>
        </el-form-item>
        <el-form-item label="进程类型" prop="processType">
          <el-select v-model="form.processType" placeholder="请选择进程类型" @change="processTypeChange">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.PROCESS_TYPE)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.processType === 1 " label="代码库" prop="codebaseId">
          <el-select v-model="form.codebaseId" placeholder="请选择代码库" filterable>
            <el-option v-for="item in codebases"
                       :key="item.id"
                       :label="item.name"
                       :value="parseInt(item.id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="CPU" prop="cpu">
          <el-input-number v-model="form.cpu" :step="0.1" placeholder="请输入CPU"/>
        </el-form-item>
        <el-form-item label="内存(GB)" prop="memory">
          <el-input-number v-model="form.memory" :step="0.1" placeholder="请输入内存(GB)"/>
        </el-form-item>
        <el-form-item label="高可用" prop="isHa">
          <el-switch
            v-model = "form.isHa"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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
import {
  createProcess,
  updateProcess,
  deleteProcess,
  getProcess,
  getProcessPage,
  exportProcessExcel
} from "@/api/pdeploy/process";
import {getAllBaselines} from "../../../api/pdeploy/baseline";
import {getAllCodebases} from "../../../api/pdeploy/codebase";

export default {
  name: "Process",
  components: {},
  data() {
    return {
      codebases: [],
      baselines: [],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 进程列表
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
        baselineId: null,
        codebaseId: null,
        name: null,
        version: null,
        processType: null,
        cpu: null,
        memory: null,
        isHa: null,
        remark: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    getAllBaselines().then(res => {
      this.baselines = res.data.list;
      getAllCodebases().then(res => {
        this.codebases = res.data.list;
        this.getList();
      });
    });
  },
  methods: {
    processTypeChange(){
      this.form.codebaseId = undefined;
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 处理查询参数
      let params = {...this.queryParams};
      // this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      // 执行查询
      getProcessPage(params).then(response => {
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
        baselineId: undefined,
        codebaseId: undefined,
        name: undefined,
        version: undefined,
        processType: undefined,
        cpu: undefined,
        memory: undefined,
        isHa: undefined,
        remark: undefined,
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
      this.dateRangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加进程";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getProcess(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改进程";
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
          updateProcess(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createProcess(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除进程编号为"' + id + '"的数据项?').then(function () {
        return deleteProcess(id);
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
      this.$modal.confirm('是否确认导出所有进程数据项?').then(() => {
        this.exportLoading = true;
        return exportProcessExcel(params);
      }).then(response => {
        this.$download.excel(response, '进程.xls');
        this.exportLoading = false;
      }).catch(() => {
      });
    }
  }
};
</script>
