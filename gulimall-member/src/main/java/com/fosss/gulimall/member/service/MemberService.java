package com.fosss.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.member.entity.MemberEntity;
import com.fosss.gulimall.member.vo.UserRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:28:05
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 检查用户名是否唯一
     */
    void checkUsernameUnique(String userName);

    /**
     * 检查手机号是否唯一
     */
    void checkPhoneUnique(String password);

    /**
     * 注册功能
     */
    void register(UserRegisterVo userRegisterVo);
}

