package com.fosss.gulimall.authserver.feign;

import com.fosss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: fosss
 * Date: 2023/5/3
 * Time: 13:53
 * Description:
 */
@FeignClient("gulimall-third-party")
public interface SmsSendFeign {
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone);
}
