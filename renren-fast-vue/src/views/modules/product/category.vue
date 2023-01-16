<template>
  <el-tree :data="data" :props="defaultProps" @node-click="getCategories"></el-tree>
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
    //查询分类数据
    getCategories() {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(res=>{
        // console.log("res:",res)
        // console.log("res.data",res.data)
        this.data=res.data.data

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

</style>
