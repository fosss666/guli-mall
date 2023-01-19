<template>
  <div>
    <el-tree :data="data"
             :props="defaultProps"
             :expand-on-click-node="false"
             node-key="catId"
             show-checkbox
             :default-expanded-keys="openCategory"
             draggable
             :allow-drop="allowDrop"
             @node-drop="handleDrop"
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

    <!--    添加修改菜单的对话框-->
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="category">
        <el-form-item label="菜单名称" width="30%">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <el-form :model="category">
        <el-form-item label="图标" width="30%">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <el-form :model="category">
        <el-form-item label="计量单位" width="30%">
          <el-input v-model="category.productUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <el-form :model="category">
        <el-form-item label="排序" width="30%">
          <el-input v-model="category.sort" autocomplete="off"></el-input>
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
      updateNodes: [],
      //最大深度
      maxLevel: 0,
      title: "",
      //封装对话框数据
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        icon: "",
        productUnit: ""
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
    //收集拖拽后的信息
    handleDrop(draggingNode, dropNode, dropType, ev) {
      // console.log("handleDrop: ", draggingNode, dropNode, dropType);
      //1、当前节点最新的父节点id
      let pCid = 0;
      let siblings = null;
      if (dropType === "before" || dropType === "after") {
        pCid = dropNode.parent.data.catId === undefined ? 0 : dropNode.parent.data.catId;
        siblings = dropNode.parent.childNodes;
      } else {
        pCid = dropNode.data.catId;
        siblings = dropNode.childNodes;
      }

      //2、当前拖拽节点的最新顺序
      for (let i = 0; i < siblings.length; i++) {
        //如果遍历的是当前正在拖拽的节点
        if (siblings[i].data.catId === draggingNode.data.catId) {
          let catLevel = draggingNode.level;
          if (siblings[i].level !== catLevel) {
            //当前节点的层级发生变化,则更新当前结点即其子节点的层级
            catLevel = siblings[i].level;
            //修改他子节点的层级
            this.updateChildNodeLevel(siblings[i]);
          }
          //将要更新的属性及数据封装
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,//排序
            parentCid: pCid,
            catLevel: catLevel
          });
        } else {
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i
          });
        }
      }

      //3、当前拖拽节点的最新层级
      // console.log("updateNodes", this.updateNodes);
      //进行更新
      this.$http({
        url: this.$http.adornUrl('/product/category/update/drag'),
        method: 'put',
        data: this.$http.adornData(this.updateNodes, false)
      }).then(() => {
        this.$message({
          message: '修改分类顺序成功',
          type: 'success'
        })
        //刷新页面
        this.getCategories()
        //设置仍展开的菜单
        this.openCategory = [pCid]
        //清空数据！！
        this.updateNodes = []
        this.maxLevel = 0
      })
    },
    //更新子节点的层级并封装
    updateChildNodeLevel(node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          var cNode = node.childNodes[i].data;
          //封装到要传向后端的数据集中
          this.updateNodes.push({
            catId: cNode.catId,
            catLevel: node.childNodes[i].level
          });
          //更新当前结点的子节点层级
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },
    //是否允许拖拽
    allowDrop(draggingNode, dropNode, type) {
      //1、被拖动的当前节点以及所在的父节点总层数不能大于3

      //1）、被拖动的当前节点总层数
      // console.log("allowDrop:", draggingNode, dropNode, type);
      //
      this.countNodeLevel(draggingNode);
      //当前正在拖动的节点+父节点所在的深度不大于3即可
      let deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
      // console.log("深度：", deep);

      //   this.maxLevel
      if (type === "inner") {
        // console.log(
        //   `this.maxLevel：${this.maxLevel}；draggingNode.data.catLevel：${draggingNode.data.catLevel}；dropNode.level：${dropNode.level}`
        // );
        return deep + dropNode.level <= 3;
      } else {
        return deep + dropNode.parent.level <= 3;
      }
    },
    countNodeLevel(node) {
      //找到所有子节点，求出最大深度
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level;
          }
          this.countNodeLevel(node.childNodes[i]);
        }
      }
    },
    //判断时新增还是修改
    saveOrUpdate() {
      // console.log("菜单id",this.category.catId)
      if (this.category.catId === undefined) {
        //调用新增方法
        // console.log("新增")
        this.saveMenu()
      } else {
        //调用修改方法
        // console.log("修改")
        this.updateMenu()
      }
    },
    //修改菜单
    updateMenu() {
      //只传输要修改的数据，其他数据不传送，否则就会以默认值替代掉以前的数据
      var {catId, name, icon, productUnit} = this.category

      this.$http({
        url: this.$http.adornUrl('/product/category/update'),
        method: 'post',
        data: this.$http.adornData({catId, name, icon, productUnit}, false)
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
    exit(data) {
      this.title = "编辑分类"
      //设置菜单id
      this.category.catId = data.catId
      //设置数据
      this.category.parentCid = data.parentCid
      this.category.catLevel = data.catLevel
      // console.log("编辑菜单",data)
      //出现对话框
      this.dialogFormVisible = true
      //回显数据
      this.category.name = data.name
      this.category.icon = data.icon
      this.category.productUnit = data.productUnit
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
      this.title = "新增分类"
      //清空对话框的数据
      this.category.name = ""
      this.category.icon = ""
      this.category.productUnit = ""
      this.category.sort = 0
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
