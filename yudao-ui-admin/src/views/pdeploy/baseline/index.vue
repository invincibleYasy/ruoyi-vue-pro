<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="基线名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入基线名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="基线版本" prop="version">
        <el-input v-model="queryParams.version" placeholder="请输入基线版本" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <!--      <el-form-item label="创建时间">-->
      <!--        <el-date-picker v-model="dateRangeCreateTime" style="width: 240px" value-format="yyyy-MM-dd"-->
      <!--                        type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" />-->
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
                   v-hasPermi="['pdeploy:baseline:create']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :loading="exportLoading"
                   v-hasPermi="['pdeploy:baseline:export']">导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="基线名称" align="center" prop="name"/>
<!--      <el-table-column label="基线版本" align="center" prop="version"/>-->
      <el-table-column label="主配置" align="center">
        <template slot-scope="scope">
          <el-button type="text" @click="confShow(scope.row.mainConf)">主配置<i class="el-icon-view el-icon--right"></i>
          </el-button>
        </template>
      </el-table-column>
<!--      <el-table-column label="ccpass配置" align="center">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="text" @click="confShow(scope.row.mainConfCcpass)">ccpass配置<i-->
<!--            class="el-icon-view el-icon&#45;&#45;right"></i></el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['pdeploy:baseline:update']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['pdeploy:baseline:delete']">删除
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
        <el-form-item label="基线名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入基线名称"/>
        </el-form-item>
<!--        <el-form-item label="基线版本" prop="version">-->
<!--          <el-input-number v-model="form.version" placeholder="请输入基线版本"/>-->
<!--        </el-form-item>-->
        <el-form-item label="主配置" prop="mainConf">
          <el-input v-model="form.mainConf" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
<!--        <el-form-item label="ccpass配置" prop="mainConfCcpass">-->
<!--          <el-input v-model="form.mainConfCcpass" type="textarea" placeholder="请输入内容"/>-->
<!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="配置信息" :visible.sync="showConf">
        <Yaml :value="yamlData" height="500px"/>
    </el-dialog>
  </div>
</template>

<script>
import {
  createBaseline,
  updateBaseline,
  deleteBaseline,
  getBaseline,
  getBaselinePage,
  exportBaselineExcel
} from "@/api/pdeploy/baseline";
import Yaml from '@/components/YamlEdit/index';
export default {
  name: "Baseline",
  components: {
    Yaml,
  },
  data() {
    return {
      // 遮罩层
      showConf: false,
      yamlData: "",
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 基线版本列表
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
        name: null,
        version: null,
        remark: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    confShow(data) {
      this.yamlData = data;
      if(this.yamlData){
        this.showConf = true;
      }else {
        this.$modal.msgError("当前基线没有ansible配置！")
      }
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 处理查询参数
      let params = {...this.queryParams};
      // this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      // 执行查询
      getBaselinePage(params).then(response => {
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
        name: undefined,
        version: undefined,
        mainConf: undefined,
        mainConfCcpass: undefined,
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
      this.title = "添加基线版本";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getBaseline(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改基线版本";
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
          updateBaseline(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createBaseline(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除基线版本编号为"' + id + '"的数据项?').then(function () {
        return deleteBaseline(id);
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
      this.$modal.confirm('是否确认导出所有基线版本数据项?').then(() => {
        this.exportLoading = true;
        return exportBaselineExcel(params);
      }).then(response => {
        this.$download.excel(response, '基线版本.xls');
        this.exportLoading = false;
      }).catch(() => {
      });
    }
  }
};
</script>
