package com.fosss.gulimall.authserver.controller;

import com.fosss.common.constant.AuthServerConstant;
import com.fosss.common.exception.ExceptionResult;
import com.fosss.common.utils.R;
import com.fosss.gulimall.authserver.feign.MemberFeignService;
import com.fosss.gulimall.authserver.feign.SmsSendFeign;
import com.fosss.gulimall.authserver.vo.UserLoginVo;
import com.fosss.gulimall.authserver.vo.UserRegisterVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fosss.common.constant.AuthServerConstant.SMS_CODE_CACHE_PREFIX;

/**
 * @author: fosss
 * Date: 2023/5/2
 * Time: 14:36
 * Description:
 */
@Controller
public class LoginController {

    @Resource
    private SmsSendFeign smsSendFeign;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MemberFeignService memberFeignService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo, RedirectAttributes attributes) {
        R r = memberFeignService.login(userLoginVo);
        if (r.getCode() == 0) {
            //登录成功
            return "redirect:http://localhost";
        } else {
            //登录失败
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", ExceptionResult.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.localhost/login.html";
        }
    }

    /**
     * TODO: 重定向携带数据：利用session原理，将数据放在session中。
     * TODO:只要跳转到下一个页面取出这个数据以后，session里面的数据就会删掉
     * TODO：分布下session问题
     * RedirectAttributes：重定向也可以保留数据，不会丢失
     * 用户注册
     */
    @PostMapping(value = "/register")
    public String register(@Valid UserRegisterVo vos, BindingResult result,
                           RedirectAttributes attributes) {

        //如果有错误回到注册页面
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            attributes.addFlashAttribute("errors", errors);

            //效验出错回到注册页面
            return "redirect:http://auth.localhost/login.html";
        }

        //1、效验验证码
        String code = vos.getCode();

        //获取存入Redis里的验证码
        String redisCode = stringRedisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + vos.getPhone());
        if (!StringUtils.isEmpty(redisCode)) {
            //截取字符串
            if (code.equals(redisCode)) {
                //删除验证码;令牌机制
                stringRedisTemplate.delete(SMS_CODE_CACHE_PREFIX + vos.getPhone());
                //验证码通过，真正注册，调用远程服务进行注册
                R register = memberFeignService.register(vos);
                if (register.getCode() == 0) {
                    //成功
                    return "redirect:http://auth.localhost/login.html";
                } else {
                    //失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", "注册失败");
                    attributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.localhost/reg.html";
                }


            } else {
                //效验出错回到注册页面
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.localhost/reg.html";
            }
        } else {
            //效验出错回到注册页面
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.localhost/reg.html";
        }
    }

    /**
     * 发送验证码
     */
    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        smsSendFeign.sendCode(phone);
        return R.ok();
    }

    /**
     * 用springmvc的视图跳转配置来替代
     */
    //@GetMapping("/login.html")
    //public String login() {
    //    return "login";
    //}
    //
    //@GetMapping("/reg.html")
    //public String register() {
    //    return "reg";
    //}
}























