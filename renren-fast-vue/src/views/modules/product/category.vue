<template>
  <div>
    <el-tree :data="data"
             :props="defaultProps"
             :expand-on-click-node="false"
             node-key="catId"
             show-checkbox
             :default-expanded-keys="openCategory"
    >
    <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level<=2"
            type="text"
            @click="() => append(data)">
            新增
          </el-button>
          <el-button
            type="text"
            @click="() => exit(data)">
            编辑
          </el-button>
          <el-button
            v-if="node.childNodes.length===0"
            type="text"
            @click="() => remove(node, data)">
            删除
          </el-button>
        </span>
      </span>
    </el-tree>

    <!--    添加菜单的对话框-->
    <el-dialog title="添加菜单" :visible.sync="dialogFormVisible">
      <el-form :model="category">
        <el-form-item label="菜单名称" width="30%">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import categoryApi from '@/store/modules/api/product/category.js'
export default {
  data() {
    return {
      //封装对话框数据
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0
      },
      //对话框是否可视
      dialogFormVisible: false,
      //需要展开的菜单
      openCategory: [],
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name',
      }
    };
  },
  created() {
    this.getCategories()
  },
  methods: {
    //判断时新增还是修改
    saveOrUpdate(){
      // console.log("菜单id",this.category.catId)
      if(this.category.catId===undefined){
        //调用新增方法
        // console.log("新增")
        this.saveMenu()
      }else {
        //调用修改方法
        // console.log("修改")
        this.updateMenu()
      }
    },
    //修改菜单
    updateMenu(){

      this.$http({
        url: this.$http.adornUrl('/product/category/update'),
        method: 'post',
        data: this.$http.adornData(this.category, false)
      }).then(() => {
        this.$message({
          message: '编辑菜单成功',
          type: 'success'
        })
        //关闭对话框
        this.dialogFormVisible = false
        //刷新页面
        this.getCategories()
        //设置仍展开的菜单
        this.openCategory = [this.category.parentCid]

      })
    },
    //编辑菜单
    exit(data){
      //设置菜单id
      this.category.catId=data.catId
      //设置数据
      this.category.parentCid = data.parentCid
      this.category.catLevel=data.catLevel
      // console.log("编辑菜单",data)
      //出现对话框
      this.dialogFormVisible=true
      //回显数据
      this.category.name=data.name
    },
    //保存菜单
    saveMenu() {

      this.$http({
        url: this.$http.adornUrl('/product/category/save'),
        method: 'post',
        data: this.$http.adornData(this.category, false)
      }).then(() => {
        this.$message({
          message: '添加菜单成功',
          type: 'success'
        })
        //关闭对话框
        this.dialogFormVisible = false
        //刷新页面
        this.getCategories()
        //设置仍展开的菜单
        this.openCategory = [this.category.parentCid]

      })
    },
    //添加菜单
    append(data) {
      //清空对话框的名字数据
      this.category.name = ""
      // console.log("对话框数据：", data)
      this.dialogFormVisible = true;
      //设置数据
      this.category.parentCid = data.catId
      this.category.catLevel = data.catLevel + 1
    },
    //删除菜单
    remove(node, data) {
      // console.log("node", node)
      // console.log("data", data)
      var ids = [node.data.catId]
      this.$confirm(`确定删除【${data.name}】菜单吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(() => {
          this.$message({
            message: '删除成功',
            type: 'success',
          })
          //刷新页面
          this.getCategories()
          //设置仍要展开的菜单
          this.openCategory = [node.parent.data.catId]//中括号！！

        })
      }).catch(() => {
      })
    },
    //查询分类数据
    getCategories() {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(res => {
        // console.log("res:",res)
        // console.log("res.data",res.data)
        this.data = res.data.data

      })
      // categoryApi.getCategories().then(res=>{
      //   console.log(res)
      //   this.data=res.data
      // })
    }
  }
};
</script>

<style scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
