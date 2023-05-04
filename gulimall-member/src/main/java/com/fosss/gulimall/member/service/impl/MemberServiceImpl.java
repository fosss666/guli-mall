package com.fosss.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.gulimall.member.dao.MemberLevelDao;
import com.fosss.gulimall.member.entity.MemberLevelEntity;
import com.fosss.gulimall.member.exception.PhoneUniqueException;
import com.fosss.gulimall.member.exception.UsernameUniqueException;
import com.fosss.gulimall.member.vo.MemberUserLoginVo;
import com.fosss.gulimall.member.vo.UserRegisterVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public void register(UserRegisterVo userRegisterVo) {
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
        memberEntity.setEmail(userRegisterVo.getPhone());

        //3.设置密码，需要进行加密存储，利用spring提供的md5加密（自动加盐）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(userRegisterVo.getPassword());
        memberEntity.setPassword(password);
        memberEntity.setMobile(userRegisterVo.getPhone());
        memberEntity.setGender(0);
        memberEntity.setCreateTime(new Date());

        baseMapper.insert(memberEntity);

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

}















