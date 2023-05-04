package com.fosss.gulimall.member.service.impl;

import com.fosss.common.utils.R;
import com.fosss.gulimall.member.dao.MemberLevelDao;
import com.fosss.gulimall.member.entity.MemberLevelEntity;
import com.fosss.gulimall.member.vo.UserRegisterVo;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.member.dao.MemberDao;
import com.fosss.gulimall.member.entity.MemberEntity;
import com.fosss.gulimall.member.service.MemberService;

import javax.annotation.Resource;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Resource
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 检查用户名是否唯一
     */
    @Override
    public boolean checkUsernameUnique(String userName) {
        return false;
    }

    /**
     * 检查手机号是否唯一
     */
    @Override
    public boolean checkPasswordUnique(String password) {
        return false;
    }

    @Override
    public R register(UserRegisterVo userRegisterVo) {
        MemberEntity memberEntity = new MemberEntity();
        //1.设置默认会员等级
        //查询默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(levelEntity.getId());

        //2.设置用户名和手机号
        //检查用户名是否唯一
        boolean usernameUnique = checkUsernameUnique(userRegisterVo.getUserName());

        //检查手机号是否唯一
        boolean passwordUnique = checkPasswordUnique(userRegisterVo.getPassword());
    }

}