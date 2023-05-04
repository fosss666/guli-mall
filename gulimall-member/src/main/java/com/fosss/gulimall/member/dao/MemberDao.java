package com.fosss.gulimall.member.dao;

import com.fosss.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员
 * 
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:28:05
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
    /**
     * 查询username的个数
     */
    int selectUsernameCount(@Param("userName") String userName);
}
