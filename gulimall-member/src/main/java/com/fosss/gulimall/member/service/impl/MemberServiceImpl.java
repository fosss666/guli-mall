package com.fosss.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.common.utils.HttpUtils;
import com.fosss.common.vo.MemberRespVo;
import com.fosss.gulimall.member.dao.MemberLevelDao;
import com.fosss.gulimall.member.entity.MemberLevelEntity;
import com.fosss.gulimall.member.exception.PhoneUniqueException;
import com.fosss.gulimall.member.exception.UsernameUniqueException;
import com.fosss.gulimall.member.vo.MemberUserLoginVo;
import com.fosss.gulimall.member.vo.SocialUser;
import com.fosss.gulimall.member.vo.UserRegisterVo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.member.dao.MemberDao;
import com.fosss.gulimall.member.entity.MemberEntity;
import com.fosss.gulimall.member.service.MemberService;
import org.springframework.util.StringUtils;

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
    public void checkUsernameUnique(String userName) {
        //查询username的个数
        int count = baseMapper.selectUsernameCount(userName);
        if (count > 0) {
            throw new UsernameUniqueException("用户名已存在");
        }
    }

    /**
     * 检查手机号是否唯一
     */
    @Override
    public void checkPhoneUnique(String phone) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getEmail, phone));
        if (count > 0) {
            throw new PhoneUniqueException("手机号已注册");
        }
    }

    /**
     * 注册功能
     */
    @Override
    public MemberRespVo register(UserRegisterVo userRegisterVo) {
        MemberEntity memberEntity = new MemberEntity();
        //1.设置默认会员等级
        //查询默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(levelEntity.getId());

        //2.设置用户名和手机号
        //检查用户名是否唯一
        checkUsernameUnique(userRegisterVo.getUserName());
        //检查手机号是否唯一
        checkPhoneUnique(userRegisterVo.getPhone());
        //设置用户名和手机号
        memberEntity.setUsername(userRegisterVo.getUserName());
        memberEntity.setNickname(userRegisterVo.getUserName());
        memberEntity.setEmail(userRegisterVo.getPhone());

        //3.设置密码，需要进行加密存储，利用spring提供的md5加密（自动加盐）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(userRegisterVo.getPassword());
        memberEntity.setPassword(password);
        memberEntity.setMobile(userRegisterVo.getPhone());
        memberEntity.setGender(0);
        memberEntity.setCreateTime(new Date());

        baseMapper.insert(memberEntity);
        MemberRespVo memberRespVo = new MemberRespVo();
        BeanUtils.copyProperties(memberEntity, memberRespVo);
        return memberRespVo;
    }

    /**
     * 登录功能
     */
    @Override
    public MemberEntity login(MemberUserLoginVo loginVo) {
        //查看是否存在该用户
        LambdaQueryWrapper<MemberEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberEntity::getUsername, loginVo.getLoginacct()).or().eq(MemberEntity::getEmail, loginVo.getLoginacct());
        MemberEntity memberEntity = baseMapper.selectOne(wrapper);
        if (memberEntity == null) {
            return null;
        }
        //查看密码是否正确
        String password = memberEntity.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(loginVo.getPassword(), password);
        return matches ? memberEntity : null;
    }

    /**
     * gitee登录中登录或注册账号
     */
    @Override
    public MemberEntity GiteeLogin(SocialUser socialUser) throws Exception {
        //查询用户信息
        //根据令牌获取用户id  https://gitee.com/api/v5/user?access_token=08bfa59b044a5163cafd9f45fd322134
        Map<String, String> headers = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        querys.put("access_token", socialUser.getAccess_token());
        HttpResponse httpResponse = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", headers, querys);
        JSONObject jsonObject = null;
        //获取并设置用户id
        HttpEntity entity1 = httpResponse.getEntity();
        String string = EntityUtils.toString(entity1);
        //这个jsonObject中储存着用户信息
        jsonObject = JSON.parseObject(string);
        socialUser.setUid(jsonObject.getString("id"));


        //根据是否存在该用户id判断是要登录还是要注册
        MemberEntity memberEntity = baseMapper.selectOne(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getSocialUid, socialUser.getUid()));
        if (memberEntity == null) {
            //需要进行注册
            MemberEntity entity = new MemberEntity();
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");

            if (!StringUtils.isEmpty(name)) {
                entity.setUsername(name);
                entity.setNickname(name);
            }
            if (!StringUtils.isEmpty(email)) {
                entity.setEmail(email);
            }
            entity.setLevelId(1L);//会员等级
            //……
            entity.setAccessToken(socialUser.getAccess_token());
            entity.setExpiresIn(socialUser.getExpires_in() + "");
            entity.setSocialUid(socialUser.getUid());
            baseMapper.insert(entity);
            return entity;
        } else {
            //直接登录，需要修改token等
            memberEntity.setSocialUid(socialUser.getUid());
            memberEntity.setExpiresIn(socialUser.getExpires_in() + "");
            memberEntity.setAccessToken(socialUser.getAccess_token());
            baseMapper.updateById(memberEntity);
            return memberEntity;

        }

    }

}















