<template>
  <div class="app-container">
    <el-row>
      <el-col :span="22">
        <el-steps :active="activeStep" finish-status="success" align-center>
          <el-step title="资源规划"></el-step>
          <el-step title="配置修改"></el-step>
          <el-step title="生成ansible配置文件"></el-step>
          <el-step title="服务部署和验证"></el-step>
        </el-steps>
      </el-col>
      <el-col :span="2">
        <div style="display: flex;justify-content: left">
          <el-button :disabled="activeStep === 0" size="small" type="primary" style="margin-top: 12px;" @click="prev">
            上一步
          </el-button>
          <el-button :disabled="activeStep === 4" size="small" type="primary" style="margin-top: 12px;" @click="next">
            下一步
          </el-button>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <!--第一步-->
      <div class="step-content" v-if="activeStep === 0">
        <el-row>
          <el-tabs :v-model="activeTab" active-name="serverPlan" type="border-card" @tab-click="tabSelect">
            <el-tab-pane name="serverPlan" label="服务器规划">
              <el-col :span="8">
                <div class="left-module1-board">
                  <div class="action-bar1">
                    <!-- 搜索工作栏 -->
                    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
                      <!--          <el-form-item>-->
                      <!--            <el-tag effect="dark" v-if="project">{{ project.name }}:</el-tag>-->
                      <!--          </el-form-item>-->
                      <el-form-item :label="project.name" prop="moduleIds" size="small" v-if="project">
                        <el-select v-model="queryParams.extendProjectId" placeholder="请选择继承项目" clearable filterable
                                   size="small">
                          <el-option v-for="dict in allProjects"
                                     :key="dict.id" :label="dict.name" :value="dict.id"/>
                        </el-select>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" style="left: 5px" size="small" icon="el-icon-arrow-down"
                                   @click="handleExtend">
                          继承
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                  <el-scrollbar class="left-module-scrollbar">
                    <div class="components-list">
                      <div v-for="(module, listIndex) in modifyModules" :key="listIndex">
                        <div class="components-title1">
                          <div>
                            <svg-icon icon-class="redis"/>
                            <el-tag type="warn" size="small">{{ module.name }}</el-tag>
                          </div>
                        </div>
                        <el-scrollbar class="module-scrollbar">
                          <draggable
                            class="components-draggable1"
                            :list="module.processes"
                            :group="{ name: 'process', pull: 'clone', put: false }"
                            :clone="cloneProcess"
                            :sort="false"
                          >
                            <el-collapse class="list-module-item"
                                         v-for="process in module.processes" :key="process.id">
                              <el-collapse-item :name="module.id+'-'+process.id">
                                <template slot="title">
                                  <span>{{ process.name }}</span>
                                </template>
                                <el-button type="text" @click="processClick(process)">
                                  详情
                                </el-button>
                              </el-collapse-item>
                            </el-collapse>
                          </draggable>
                        </el-scrollbar>
                      </div>
                    </div>
                  </el-scrollbar>
                </div>
              </el-col>
              <el-col :span="16">
                <div class="right-server-board">
                  <div class="right-server-board-content">
                    <div class="action-bar1" style="border-right: none">
                      <!-- 搜索工作栏 -->
                      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                               label-width="68px">
                        <el-form-item label="模块" prop="moduleIds">
                          <el-select v-model="queryParams.moduleIds" placeholder="请选择模块" clearable filterable multiple
                                     collapse-tags
                                     size="small">
                            <el-option v-for="dict in fixedModules"
                                       :key="dict.id" :label="dict.name" :value="dict.id"/>
                          </el-select>
                        </el-form-item>
                        <el-form-item label="服务器" prop="serverIds">
                          <el-select v-model="queryParams.serverIds" placeholder="请选择服务器" clearable filterable
                                     multiple
                                     collapse-tags size="small">
                            <el-option v-for="dict in fixedServers"
                                       :key="dict.id" :label="dict.name" :value="dict.id"/>
                          </el-select>
                        </el-form-item>
                        <el-form-item>
                          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleMerge">合并
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                    <el-scrollbar class="center-scrollbar1">
                      <el-row class="center-board-row" :gutter="20">
                        <el-form>
                          <el-col :span="8" v-for="server in modifyServers" :key="server.id">
                            <div class="components-title1">
                              <el-button size="small" type="text" icon="el-icon-success"
                                         @click="updateServer(server)">更新
                              </el-button>
                              <el-button size="small" type="text" icon="el-icon-delete" @click="deleteServer(server)">
                                删除
                              </el-button>
                              <el-button size="small" type="text" icon="el-icon-refresh" @click="resetServer(server)">
                                重置
                              </el-button>
                              <el-button size="small" type="text" icon="el-icon-delete"
                                         @click="deleteAllInServer(server)">清空
                              </el-button>
                            </div>
                            <div style="display: flex; justify-content: space-between">
                              <div>
                                <svg-icon icon-class="server"/>
                                <el-tag type="warn" size="small">{{ server.name }}</el-tag>
                              </div>
                              <el-checkbox :id="'serverCheckbox-'+server.id" @change="serverChecked($event,server)"
                                           :ref="'serverCheckbox-'+server.id"/>
                            </div>
                            <el-scrollbar class="server-scrollbar">
                              <draggable :list="server.processes" :animation="340" group="process">
                                <el-collapse class="list-server-item left"
                                             v-for="(process,index) in server.processes" :key="process.id">
                                  <el-collapse-item :name="server.id+'-'+process.id">
                                    <template slot="title">
                                      <i class="el-icon-circle-close" style="margin-right: 5px"
                                         @click.stop="deleteServerProcess(index,server.processes)"></i>
                                      <span>{{ process.name }}</span>
                                    </template>
                                    <el-button type="text" @click="processClick(process)">
                                      详情
                                    </el-button>
                                  </el-collapse-item>
                                </el-collapse>
                              </draggable>
                            </el-scrollbar>
                          </el-col>
                        </el-form>
                      </el-row>
                    </el-scrollbar>
                  </div>
                </div>
              </el-col>
            </el-tab-pane>
            <el-tab-pane name="domainPlan" label="域名规划">
              <div class="domain-plan">
                <el-table :v-loading="true" v-if="modifyServers[0]" :data="modifyServers[0].processes">
                  <el-table-column label="基线版本" align="center" prop="baselineId">
                    <template slot-scope="scope">
                      <dynamic-dict-tag :options="baselines" :value="scope.row.baselineId"/>
                    </template>
                  </el-table-column>
                  <el-table-column label="进程名称" align="center" prop="name"/>
                  <el-table-column label="进程类型" align="center" prop="processType">
                    <template slot-scope="scope">
                      <dict-tag :type="DICT_TYPE.PROCESS_TYPE" :value="scope.row.processType"/>
                    </template>
                  </el-table-column>
                  <el-table-column label="标签" align="center">Mysql</el-table-column>
                  <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" icon="el-icon-edit" @click="replaceMidware(scope.row)">替换
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
            <el-tab-pane name="midwarePlan" label="中间件选择">
              <div class="midware-plan">
                <el-table :v-loading="true" v-if="modifyServers[0]" :data="modifyServers[0].processes">
                  <el-table-column label="基线版本" align="center" prop="baselineId">
                    <template slot-scope="scope">
                      <dynamic-dict-tag :options="baselines" :value="scope.row.baselineId"/>
                    </template>
                  </el-table-column>
                  <el-table-column label="进程名称" align="center" prop="name"/>
                  <el-table-column label="进程类型" align="center" prop="processType">
                    <template slot-scope="scope">
                      <dict-tag :type="DICT_TYPE.PROCESS_TYPE" :value="scope.row.processType"/>
                    </template>
                  </el-table-column>
                  <el-table-column label="标签" align="center">Mysql</el-table-column>
                  <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-button size="mini" type="text" icon="el-icon-edit" @click="replaceMidware(scope.row)">替换
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </div>
      <!--第二步-->
      <div class="step-content" v-if="activeStep === 1">
        <el-row>
          <el-tabs active-name="confUpdate" type="border-card" @tab-click="tabSelect">
            <el-tab-pane name="confUpdate" label="配置修改">
              <config-list :source="2" :super-project-id="project.id"
                           :super-baseline-id="project.baselineId"/>
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </div>
      <!--第三步-->
      <div class="step-content" v-if="activeStep === 2">
        <el-row :gutter="0">
          <el-tabs active-name="ansibleConf" type="border-card" @tab-click="tabSelect">
            <el-tab-pane name="ansibleConf" label="ansible配置">
              <el-col :span="12">
                <el-tag>主配置</el-tag>
                <Yaml :value="mainConf" height="800px"/>
              </el-col>
              <el-col :span="12">
                <el-tag>CCPASS配置</el-tag>
                <Yaml :value="ccpassConf" height="800px"/>
              </el-col>
            </el-tab-pane>
            <el-tab-pane name="ansibleHosts" label="ansibleHosts">
              <div style="border: 1px solid #f1e8e8;">

              </div>
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </div>
      <!--第四步-->
      <div class="step-content" v-if="activeStep === 3 || activeStep === 4">
        <el-row>
          <el-tabs active-name="serverDeploy" type="border-card" @tab-click="tabSelect">
            <el-tab-pane name="serverDeploy" label="服务部署和验证">
              <el-descriptions style="padding-top: 20px" border :column="1" v-for="item in preCheckList">
                <template slot="title">
                  <el-tag>{{ item.type }}</el-tag>
                </template>
                <el-descriptions-item :label="i.name" v-for="i in item.items">
                  {{ i.content }}
                </el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>
          </el-tabs>
        </el-row>
      </div>
      <!--中间件版本替换-->
      <el-dialog title="中间件版本替换" :visible.sync="showReplaceMidware" width="500px" append-to-body>
        <el-form>
          <el-form-item label="中间件">
            <el-select v-model="form" placeholder="中间件" clearable filterable multiple
                       collapse-tags size="small">
              <el-option
                :key="1" label="shared-mysql8" :value="1"/>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
      <!--进程详情-->
      <el-dialog title="进程详情" :visible.sync="showProcessDetail"
                 width="500px" append-to-body>
        <el-descriptions border size="small" :column="1">
          <el-descriptions-item label="名称">
            {{ processDetail.name }}
          </el-descriptions-item>
          <el-descriptions-item label="备注">
            {{ processDetail.remark }}
          </el-descriptions-item>
          <el-descriptions-item label="部署命令">
            {{ processDetail.remark }}
          </el-descriptions-item>
        </el-descriptions>
      </el-dialog>
      <!--对话框(添加 / 修改) -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="合并服务器" prop="mergeServres">
            <el-tag v-for="item in mergeServers">
              {{ item.name }}
            </el-tag>
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入服务器名称"/>
          </el-form-item>
          <el-form-item label="服务器IP" prop="ip">
            <el-input v-model="form.ip" placeholder="请输入服务器IP"/>
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
    </el-row>
  </div>
</template>

<script>
import {
  deleteProject,
  getProject,
  exportProjectExcel,
  getAllProjects,
  extendProject,
  mergeServers,
  showProjectConf,
  updateProjectServer,
  deleteProjectServer
} from "@/api/pdeploy/project";
import draggable from "vuedraggable";
import {getAllBaselines} from "../../../../api/pdeploy/baseline";
import {getProjectAll} from "../../../../api/pdeploy/project";
import {isEqual} from "element-ui/src/utils/util";
import {deepClone} from "@/utils";
import configList from "../conf/index";
import Yaml from '@/components/YamlEdit/index';

export default {
  name: "Project",
  components: {
    draggable,
    configList,
    Yaml,
  },
  data() {
    return {
      mainConf: '',
      ccpassConf: '',
      preCheckList: [
        {
          type: '环境检测',
          items: [
            {
              name: '服务器时间检测',
              content: 'ansible拉取各台服务器时间，差距不能超过30秒'
            },
            {
              name: 'corn服务检测',
              content: '检测每台服务器的corn服务是否正常启动'
            },
          ],
        },
        {
          type: '网络检测',
          items: [
            {
              name: '镜像拉取检测',
              content: '检测每台服务器的corn服务是否正常启动'
            },
            {
              name: 'apt软件安装',
              content: '检测每台服务器的corn服务是否正常启动'
            },
            {
              name: '内网端口连通性',
              content: '检测每台服务器的corn服务是否正常启动'
            }
          ],
        },
        {
          type: '资源检测',
          items: [
            {
              name: '配置文件填写',
              content: '检测每台服务器的corn服务是否正常启动'
            },
            {
              name: '域名解析',
              content: '检测每台服务器的corn服务是否正常启动'
            },
            {
              name: '中间件版本',
              content: '检测每台服务器的corn服务是否正常启动'
            }
          ],
        }
      ],

      confLoading: false,
      confList: [],
      activeStep: 0,
      activeTab: "serverPlan",
      tagIcon: 'slider',
      project: {},
      mergeServers: [],
      allProjects: [],
      baselines: [],
      fixedModules: [],
      fixedServers: [],
      modifyModules: [],
      modifyServers: [],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 私有项目列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      showProcessDetail: false,
      showDeploySteps: false,
      showReplaceMidware: false,
      processDetail: {},
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        baselineId: null,
        serverIds: [],
        moduleIds: [],
        name: null,
        remark: null,
        extendProjectId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "服务器名称不能为空", trigger: "blur"}
        ],
        ip: [
          {required: true, message: "服务器IP不能为空", trigger: "blur"}
        ],
        cpu: [
          {required: true, message: "服务器CPU不能为空", trigger: "blur"}
        ],
        memory: [
          {required: true, message: "服务器内存不能为空", trigger: "blur"}
        ],
      },
    };
  },
  created() {
    const projectId = this.$route.params && this.$route.params.projectId;
    getProject(projectId).then(res => {
      this.project = res.data;
      this.queryParams.name = this.project.name;
      this.queryParams.baselineId = this.project.baselineId;
    });
    getAllBaselines().then(res => {
      this.baselines = res.data.list;
    });
    getProjectAll(projectId).then(res => {
      this.fixedModules = res.data.modules;
      this.fixedServers = res.data.servers;
      this.modifyModules = deepClone(this.fixedModules)
      this.modifyServers = deepClone(this.fixedServers)
    });
    getAllProjects().then(res => {
      this.allProjects = res.data.list.filter(i => !isEqual(i.id, projectId * 1));
    })
  },
  methods: {
    tabSelect(data) {
      this.activeTab = data.name;
    },
    replaceMidware(item) {
      console.log("replaceMidware:", item)
      this.showReplaceMidware = true;
    },
    prev() {
      this.activeStep--;
      if (this.activeStep === 0){
        return;
      }
      // ansible配置
      if (this.activeStep === 2) {
        this.getAnsibleConf();
      }
    },
    next() {
      this.activeStep++;
      if (this.activeStep === 4){
        return;
      }
      // ansible配置
      if (this.activeStep === 2) {
        this.getAnsibleConf();
      }
    },
    getAnsibleConf() {
      let params = {
        baselineId: this.project.baselineId,
        id: this.project.id,
      }
      showProjectConf(params).then(res => {
        this.mainConf = res.data.mainConf;
        this.ccpassConf = res.data.ccpassConf;
        console.log("showProjectConf:", res.data);
      });
    },
    handleDeploy() {
      this.activeStep = 0;
      this.showDeploySteps = true;
    },
    processClick(process) {
      this.showProcessDetail = true;
      this.processDetail = process;
    },
    handleExtend() {
      let params = {...this.queryParams};
      if (params.extendProjectId) {
        let currentProj = this.allProjects.find(pro => isEqual(pro.id, params.extendProjectId));
        console.log("currentProj:", currentProj);
        this.$confirm('是否要继承【' + currentProj.name + '】, 该操作将重新生成服务器列表，且无法恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let data = {
            extendProjectId: params.extendProjectId,
            currentProjectId: this.project.id,
          }
          console.log("handleExtend:", data);
          extendProject(data).then(res => {
            if (res) {
              this.$message({
                type: 'success',
                message: '操作成功!'
              });
              this.fixedServers = res.data.servers;
              this.modifyServers = deepClone(this.fixedServers);
            }
            // let params = {...this.queryParams};
            // console.log("handleExtend:", params);
          });
        })
      } else {
        this.$message({
          type: 'warning',
          message: '请选择需要继承的项目!'
        });
      }
    },
    serverChecked(checked, server) {
      if (checked) {
        this.mergeServers.push(server)
      } else {
        this.mergeServers.forEach((v, i) => {
          if (server.id === v.id) {
            this.mergeServers.splice(i, 1);
          }
        })
      }
      console.log("serverChecked:", checked, server, this.mergeServers);
    },
    resetServer(server) {
      if (server) {
        this.modifyServers.forEach(modifyServer => {
          if (isEqual(modifyServer.id, server.id)) {
            let find = this.fixedServers.find(fixedServer => isEqual(fixedServer.id, server.id));
            modifyServer.processes = deepClone(find.processes);
          }
        })
        // getServer(server.id).then(res => {
        //   this.modifyServers.forEach(modifyServer => {
        //     if (isEqual(modifyServer.id, server.id)) {
        //       modifyServer.processes = res.data.processes;
        //     }
        //   })
        // });
      }
    },
    deleteServer(server) {
      this.$modal.confirm("确定删除" + server.name + "？此操作不可逆！")
        .then(() => {
          return deleteProjectServer(server.id);
        }).then(() => {
        getProjectAll(this.project.id).then(res => {
          this.fixedModules = res.data.modules;
          this.fixedServers = res.data.servers;
          this.modifyModules = deepClone(this.fixedModules)
          this.modifyServers = deepClone(this.fixedServers)
        });
      })

    },
    updateServer(server) {
      this.$modal.confirm("确定更新" + server.name + "？此操作不可逆！")
        .then(() => {
          let params = {
            serverId: server.id,
            processIds: server.processes.map(process => process.id)
          }
          return updateProjectServer(params);
        }).then(() => {
        getProjectAll(this.project.id).then(res => {
          this.fixedModules = res.data.modules;
          this.fixedServers = res.data.servers;
          this.modifyModules = deepClone(this.fixedModules)
          this.modifyServers = deepClone(this.fixedServers)
        });
      })
    },
    deleteAllInServer(server) {
      if (server) {
        server.processes = [];
      }
    },
    deleteServerProcess(index, processes) {
      console.log("deleteServerProcess:", index, processes);
      console.log("fixedServers:", this.fixedServers)
      console.log("modifyServers:", this.modifyServers)
      if (processes) {
        processes.splice(index, 1);
      }
    },
    cloneProcess(res) {
      console.log("clone process:", res)
      return deepClone(res);
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 处理查询参数
      let params = {...this.queryParams};
      if (params.moduleIds.length > 0) {
        this.modifyModules = deepClone(this.fixedModules.filter(fixedModule => params.moduleIds.includes(fixedModule.id)));
      } else {
        this.modifyModules = deepClone(this.fixedModules);
      }
      if (params.serverIds.length > 0) {
        this.modifyServers = deepClone(this.fixedServers.filter(fixedServer => params.serverIds.includes(fixedServer.id)));
      } else {
        this.modifyServers = deepClone(this.fixedServers);
      }
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
        moduleIds: [],
        baselineId: undefined,
        name: undefined,
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
    handleMerge() {
      if (this.mergeServers.length <= 1) {
        this.$message({
          type: 'warning',
          message: '请选择要合并的服务器!'
        });
      } else {
        this.open = true;
        this.title = "合并服务器";
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getProject(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改私有项目";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        this.form.projectId = this.project.id;
        this.form.mergeServers = this.mergeServers.map(m => m.id)
        // 添加的提交
        mergeServers(this.form).then(response => {
          this.$modal.msgSuccess("合并成功");
          this.open = false;
          getProjectAll(this.form.projectId).then(res => {
            this.fixedModules = res.data.modules;
            this.fixedServers = res.data.servers;
            this.modifyModules = deepClone(this.fixedModules)
            this.modifyServers = deepClone(this.fixedServers)
          });
          this.mergeServers = [];
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除私有项目编号为"' + id + '"的数据项?').then(function () {
        return deleteProject(id);
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
      this.$modal.confirm('是否确认导出所有私有项目数据项?').then(() => {
        this.exportLoading = true;
        return exportProjectExcel(params);
      }).then(response => {
        this.$download.excel(response, '私有项目.xls');
        this.exportLoading = false;
      }).catch(() => {
      });
    }
  }
};
</script>

<style lang='scss'>
@import '@/styles/home';

.step-content {
  //border-top: 1px solid #f1e8e8;;
}

body {
  overflow: hidden;
}

.el-row {
  margin-bottom: 20px;
}

.left-module-scrollbar {
  height: calc(100vh - 50px);
  overflow: hidden;
  border-right: 1px solid #f1e8e8;
  //border-bottom: 1px solid #f1e8e8;
}

.components-draggable1 {
  padding-bottom: 20px;
  border-radius: 4px;
}

.components-item1 {
  display: inline-block;
  //border: 1px solid #f1e8e8;
  width: 48%;
  margin: 1%;
  transition: transform 0ms !important;
}

.center-scrollbar1 {
  height: calc(100vh - 50px);
  overflow: hidden;
  //border-left: 1px solid #f1e8e8;
  //border-right: 1px solid #f1e8e8;
  box-sizing: border-box;
}

.action-bar1 {
  position: relative;
  height: 50px;
  text-align: left;
  padding: 0 5px;
  box-sizing: border-box;;
  border: 1px solid #f1e8e8;
  border-top: none;
  border-left: none;

  .delete-btn {
    color: #F56C6C;
  }
}

.left-module1-board {
  float: left;
  width: 100%;
  display: inline;

}

.right-server-board {
  display: inline;
  float: left;
  width: 100%;
}

.right-server-board-content {
  //margin-left: 400px;
}

.server-scrollbar {
  height: 200px;
  border: 1px solid #f1e8e8;
}

.module-scrollbar {
  height: 200px;
  //border-left: 1px solid #f1e8e8;
  //border-right: 1px solid #f1e8e8;
  //border-bottom: 1px solid #f1e8e8;
  //border-top: 1px solid #f1e8e8;
}


.list-server {
  display: flex;
  flex-direction: column;
  padding-left: 0;
  margin-bottom: 0;
  border: 0;
}

.list-server-item:first-child {
  border-top-left-radius: 0.25rem;
  border-top-right-radius: 0.25rem;
}

.list-server-item {
  position: relative;
  display: block;
  padding: 0.75rem 1.25rem;
  //margin-bottom: -1px;
  background-color: #fff;
  //border: 1px solid rgba(0, 0, 0, 0.125);
  box-sizing: border-box;
  border-top: none;
}

.list-module-item:first-child {
  border-top-left-radius: 0.25rem;
  border-top-right-radius: 0.25rem;
}

.list-module-item {
  position: relative;
  display: block;
  padding: 0.75rem 1.25rem;
  //margin-bottom: -1px;
  background-color: #fff;
  //border: 1px solid rgba(0, 0, 0, 0.125);
  box-sizing: border-box;
  border-top: none;
}

.el-collapse-item__header {
  border: 0;
  height: 22px;
  line-height: 22px;
}

.el-collapse-item__wrap {
  border-bottom: 0;
}

.list-server-item {
  cursor: move;
}

h3 {
  font-size: 28px;
  margin-bottom: 20px;
}

.el-icon-circle-close {
  color: #c9a2a2;
  font-size: 20px;
  right: 50px;
}

.el-icon-circle-close:hover {
  color: #f40;
}

.components-title1 {
  font-size: 14px;
  color: #222;
  margin: 6px 2px;
  display: flex;
  justify-content: left;

  .svg-icon {
    color: #666;
    font-size: 18px;
    margin-right: 5px;
  }

  .el-tag {
    //color: #666;
    margin-right: 5px;
  }

  .logo-wrapper {
    position: relative;
    height: 50px;
    background: #fff;
    border-bottom: 1px solid #f1e8e8;
    box-sizing: border-box;
  }
}

.svg-icon {
  color: #666;
  font-size: 18px;
  margin-right: 5px;
}

.el-tag {
  //color: #666;
  margin-right: 5px;
}

</style>
