package com.fosss.gulimall.authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: fosss
 * Date: 2023/5/2
 * Time: 14:36
 * Description:
 */
@Controller
public class LoginController {

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }

    @GetMapping("/reg.html")
    public String register() {
        return "reg";
    }
}























