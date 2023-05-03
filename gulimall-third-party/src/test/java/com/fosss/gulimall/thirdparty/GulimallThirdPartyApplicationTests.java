package com.fosss.gulimall.thirdparty;

import com.fosss.gulimall.thirdparty.component.SmsComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallThirdPartyApplicationTests {

    @Resource
    private SmsComponent smsComponent;

    /**
     * 测试发送邮箱验证码
     */
    @Test
    public void contextLoads() {
        smsComponent.sendMail("2726512560@qq.com");

    }

}
