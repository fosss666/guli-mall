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
            size="mini"
            @click="() => append(data)">
            Append
          </el-button>
          <el-button
            v-if="node.childNodes.length===0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            Delete
          </el-button>
        </span>
      </span>
    </el-tree>
  </div>
</template>

<script>
// import categoryApi from '@/store/modules/api/product/category.js'
export default {
  data() {
    return {
      //需要展开的菜单
      openCategory:[],
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
    //添加菜单
    append(data) {
      console.log(data)
    },
    //删除菜单
    remove(node, data) {
      console.log("node", node)
      console.log("data", data)
      var ids=[node.data.catId]
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
            this.openCategory=[node.parent.data.catId]//中括号！！

        })
      }).catch(() => {})
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
