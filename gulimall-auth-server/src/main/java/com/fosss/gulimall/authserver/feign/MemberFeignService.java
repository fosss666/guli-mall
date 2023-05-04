package com.fosss.gulimall.authserver.feign;

import com.fosss.common.utils.R;
import com.fosss.gulimall.authserver.vo.UserLoginVo;
import com.fosss.gulimall.authserver.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: fosss
 * Date: 2023/5/4
 * Time: 11:18
 * Description:
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    /**
     * 登录功能
     */
    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo loginVo);

    /**
     * 注册功能
     */
    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo);
}
