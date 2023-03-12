package com.fosss.gulimall.search.service;

import com.fosss.gulimall.search.vo.SearchParam;
import com.fosss.gulimall.search.vo.SearchResult;

/**
 * @author: fosss
 * Date: 2023/3/12
 * Time: 19:44
 * Description:
 */
public interface MallSearchService {
    /**
     * 从es中查询搜索页数据并返回
     */
    SearchResult search(SearchParam searchParam);
}
