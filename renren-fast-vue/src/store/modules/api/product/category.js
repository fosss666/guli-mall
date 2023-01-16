import request from 'src/utils/httpRequest.js'

export default {
  //查询分类
  getCategories(){
    return request({
      url:'/product/category/list/tree',
      method:'get'
    })
  }
}













