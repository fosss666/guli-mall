package com.fosss.gulimall.search;

import com.fosss.gulimall.search.config.EsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

    @Resource
    private EsConfig esConfig;

    @Test
    public void contextLoads() {
        System.out.println(esConfig.esClient());
    }

}


















