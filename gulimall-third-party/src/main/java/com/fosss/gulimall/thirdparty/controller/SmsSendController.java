package com.fosss.gulimall.thirdparty.controller;

import com.fosss.common.utils.R;
import com.fosss.gulimall.thirdparty.component.SmsComponent;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: fosss
 * Date: 2023/5/3
 * Time: 13:46
 * Description：发送邮箱验证码
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Resource
    private SmsComponent smsComponent;

    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        smsComponent.sendMail(phone);
        return R.ok();
    }
}





















