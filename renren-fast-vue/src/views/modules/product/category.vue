<template>
  <el-tree :data="data" :props="defaultProps" @node-click="getCategories"
           :expand-on-click-node="false"
           node-key="catId"
           show-checkbox>
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
</template>

<script>
// import categoryApi from '@/store/modules/api/product/category.js'
export default {
  data() {
    return {
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    };
  },
  created() {
    this.getCategories()
  },
  methods: {
    append(data) {
      console.log(data)
    },

    remove(node, data) {
      console.log("node", node)
      console.log("data", data)
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
