package com.fosss.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.fosss.common.exception.ExceptionResult;
import com.fosss.gulimall.member.dao.MemberLevelDao;
import com.fosss.gulimall.member.entity.MemberLevelEntity;
import com.fosss.gulimall.member.exception.PhoneUniqueException;
import com.fosss.gulimall.member.exception.UsernameUniqueException;
import com.fosss.gulimall.member.vo.MemberUserLoginVo;
import com.fosss.gulimall.member.vo.SocialUser;
import com.fosss.gulimall.member.vo.UserRegisterVo;
import com.netflix.hystrix.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fosss.gulimall.member.entity.MemberEntity;
import com.fosss.gulimall.member.service.MemberService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;

import javax.annotation.Resource;

import static com.fosss.common.exception.ExceptionResult.*;


/**
 * 会员
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:28:05
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * gitee登录中登录或注册账号
     */
    @PostMapping("/OAuth/login")
    public R GiteeLogin(@RequestBody SocialUser socialUser) throws Exception {
        MemberEntity memberEntity = memberService.GiteeLogin(socialUser);
        return memberEntity == null ?
                R.error(GITEE_LOGIN_ERROR.getCode(), GITEE_LOGIN_ERROR.getMessage())
                : R.ok();
    }

    /**
     * 登录功能
     */
    @PostMapping("/login")
    public R login(@RequestBody MemberUserLoginVo loginVo) {
        MemberEntity memberEntity = memberService.login(loginVo);
        return memberEntity == null ?
                R.error(ExceptionResult.LOGINACCT_PASSWORD_EXCEPTION.getCode(), ExceptionResult.LOGINACCT_PASSWORD_EXCEPTION.getMessage())
                : R.ok().put("data",memberEntity);
    }

    /**
     * 注册功能
     */
    @PostMapping("/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo) {
        try {
            memberService.register(userRegisterVo);
            return R.ok();
        } catch (UsernameUniqueException e) {
            return R.error(USER_EXIST_EXCEPTION.getCode(), USER_EXIST_EXCEPTION.getMessage());
        } catch (PhoneUniqueException e) {
            return R.error(PHONE_EXIST_EXCEPTION.getCode(), PHONE_EXIST_EXCEPTION.getMessage());
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
