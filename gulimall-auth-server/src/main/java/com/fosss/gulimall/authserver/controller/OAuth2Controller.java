package com.fosss.gulimall.authserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fosss.common.exception.ExceptionResult;
import com.fosss.common.utils.HttpUtils;
import com.fosss.common.utils.R;
import com.fosss.gulimall.authserver.feign.MemberFeignService;
import com.fosss.common.vo.MemberRespVo;
import com.fosss.gulimall.authserver.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: fosss
 * Date: 2023/5/6
 * Time: 21:49
 * Description: 社交登录
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Resource
    private MemberFeignService memberFeignService;

    @Value("${OAuth.gitee.grant_type}")
    private String grant_type;
    @Value("${OAuth.gitee.redirect_uri}")
    private String redirect_uri;
    @Value("${OAuth.gitee.client_id}")
    private String client_id;
    @Value("${OAuth.gitee.client_secret}")
    private String client_secret;

    /**
     * gitee登录
     */
    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code") String code, RedirectAttributes attributes, HttpSession session) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", client_id);
        map.put("client_secret", client_secret);
        map.put("grant_type", grant_type);
        map.put("redirect_uri", redirect_uri);
        map.put("code", code);
        //根据code获取token
        //https://gitee.com/oauth/token?grant_type=authorization_code&code=9f9e98d38d44d71177c2cc26be66102bcd6248133722cfe7ff8d396ff26e3e0c&client_id=e27708e7647d475cc0b7f78a94a838f266641055958b8c19d68731a9b4f89e16&redirect_uri=http://localhost/oauth2.0/gitee/success&client_secret=ddaf917802e50417abf8dfc65df5feeb813228647353026dbae7516bf8d117e4
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), map, new HashMap<>());
        //进行处理
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == 200) {
            //获取token成功
            HttpEntity entity = response.getEntity();
            //解析内容
            String json = EntityUtils.toString(entity);
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            //知道这是哪个社交用户
            //如果是第一次进网站就要为这个用户注册一个账号，然后登录，否则直接登录
            // 调用远程服务登录或注册账号
            R r = memberFeignService.GiteeLogin(socialUser);
            if (r.getCode() == 0) {
                //登录成功跳回首页
                //获取数据
                MemberRespVo memberRespVo = r.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("登录成功，用户信息：{}", memberRespVo);
                //存储到session中
                //todo 1.当前session的作用域为当前域，无法与子域分享信息 2.使用json的序列化存储数据到redis中
                session.setAttribute("loginUser", memberRespVo);
                return "redirect:http://localhost";
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("msg", ExceptionResult.GITEE_LOGIN_ERROR.getMessage());
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.localhost/login.html";
            }
        } else {
            //失败则跳到登录页
            return "redirect:http://auth.localhost/login.html";
        }

    }
}






















