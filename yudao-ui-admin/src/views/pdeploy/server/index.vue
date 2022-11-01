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
      <el-form-item label="所属项目" prop="projectId">
        <el-select v-model="queryParams.projectId" placeholder="请选择所属项目" clearable size="small">
          <el-option v-for="dict in projects"
                     :key="dict.id" :label="dict.name" :value="dict.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入服务器名称" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['pdeploy:server:create']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :loading="exportLoading"
                   v-hasPermi="['pdeploy:server:export']">导出
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
      <el-table-column label="项目" align="center" prop="projectId">
        <template slot-scope="scope">
          <dynamic-dict-tag :options="projects" :value="scope.row.projectId"/>
        </template>
      </el-table-column>
      <el-table-column label="服务器名称" align="center" prop="name"/>
      <el-table-column label="服务器IP" align="center" prop="ip"/>
      <el-table-column label="CPU" align="center" prop="cpu"/>
      <el-table-column label="内存(GB)" align="center" prop="memory"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['pdeploy:server:update']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['pdeploy:server:delete']">删除
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入服务器名称"/>
        </el-form-item>
        <el-form-item label="服务器IP" prop="ip">
          <el-input v-model="form.ip" placeholder="请输入服务器IP"/>
        </el-form-item>
        <el-form-item label="基线版本" prop="baselineId">
          <el-select v-model="form.baselineId" placeholder="请选择基线版本">
            <el-option v-for="dict in baselines"
                       :key="dict.id" :label="dict.name" :value="parseInt(dict.id)"/>
          </el-select>
        </el-form-item>
        <el-form-item label="所属项目" prop="projectId">
          <el-select v-model="form.projectId" placeholder="请选择所属项目" filterable>
            <el-option v-for="dict in projects"
                       :key="dict.id" :label="dict.name" :value="dict.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="关联进程" prop="processIds">
          <el-select v-model="form.processIds" placeholder="请选择关联进程" filterable multiple collapse-tags>
            <el-option v-for="dict in processes"
                       :key="dict.id" :label="dict.name" :value="dict.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="CPU" prop="cpu">
          <el-input-number v-model="form.cpu" placeholder="请输入CPU"/>
        </el-form-item>
        <el-form-item label="内存(GB)" prop="memory">
          <el-input-number v-model="form.memory" placeholder="请输入内存(GB)"/>
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
  createServer,
  updateServer,
  deleteServer,
  getServer,
  getServerPage,
  exportServerExcel
} from "@/api/pdeploy/server";
import {getAllBaselines} from "../../../api/pdeploy/baseline";
import {getAllProcesses} from "../../../api/pdeploy/process";
import {getAllProjects} from "../../../api/pdeploy/project";

export default {
  name: "Server",
  components: {},
  data() {
    return {
      processes: [],
      projects: [],
      baselines: [],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 服务器列表
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
        projectId: null,
        name: null,
        ip: null,
        cpu: null,
        memory: null,
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
      getAllProjects().then(res => {
        this.projects = res.data.list;
        this.getList();
      });
    });
  },
  methods: {
    listAllProcess() {
      getAllProcesses().then(res => {
        this.processes = res.data.list;
      });
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 处理查询参数
      let params = {...this.queryParams};
      // this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      // 执行查询
      getServerPage(params).then(response => {
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
        processIds: [],
        baselineId: undefined,
        projectId: undefined,
        name: undefined,
        ip: undefined,
        cpu: undefined,
        memory: undefined,
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
      this.listAllProcess();
      this.open = true;
      this.title = "添加服务器";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.listAllProcess();
      const id = row.id;
      getServer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改服务器";
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
          updateServer(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createServer(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除服务器编号为"' + id + '"的数据项?').then(function () {
        return deleteServer(id);
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
      this.$modal.confirm('是否确认导出所有服务器数据项?').then(() => {
        this.exportLoading = true;
        return exportServerExcel(params);
      }).then(response => {
        this.$download.excel(response, '服务器.xls');
        this.exportLoading = false;
      }).catch(() => {
      });
    }
  }
};
</script>
