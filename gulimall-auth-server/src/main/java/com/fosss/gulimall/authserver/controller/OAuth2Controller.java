package com.fosss.gulimall.authserver.controller;

import com.fosss.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: fosss
 * Date: 2023/5/6
 * Time: 21:49
 * Description: 社交登录
 */
@Controller
public class OAuth2Controller {
    /**
     * gitee登录
     */
    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code") String code) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "e27708e7647d475cc0b7f78a94a838f266641055958b8c19d68731a9b4f89e16");
        map.put("client_secret", "ddaf917802e50417abf8dfc65df5feeb813228647353026dbae7516bf8d117e4");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://localhost/oauth2.0/gitee/success");
        map.put("code", code);
        //根据code获取token
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), map, new HashMap<>());
        //进行处理

        return "";
    }
}






















