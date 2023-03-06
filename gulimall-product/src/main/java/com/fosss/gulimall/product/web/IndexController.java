package com.fosss.gulimall.product.web;

import com.fosss.common.constant.RedisConstant;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.CategoryService;
import com.fosss.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author fosss
 * @date 2023/2/24
 * @description： 前台首页页面
 */
@Slf4j
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试闭锁
     */
    @ResponseBody
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        //获取闭锁
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        //设置数量
        door.trySetCount(5);
        //进行等待
        door.await();

        return "放假咯。。。";
    }

    @ResponseBody
    @GetMapping("/gogogo/{id}")
    public String gogogo(@PathVariable Long id) {
        //数量减一
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();
        return id + "班都走光了。";
    }

    /**
     * 测试读写锁
     * 读写锁能够保证读到的数据一定是最新的，写锁是排他锁（互斥锁），多个写锁应排队进行，读锁是共享锁，能够同时读，写锁未释放，读锁就一直等待。同
     * 样，读锁未释放，也不能写
     */
    @ResponseBody
    @GetMapping("/write")
    public String write() {
        RReadWriteLock rReadWriteLock = redissonClient.getReadWriteLock("ReadWriteLock");
        //获取读锁
        RLock rLock = rReadWriteLock.writeLock();
        //上锁
        rLock.lock();
        try {
            stringRedisTemplate.opsForValue().set("writeValue", UUID.randomUUID().toString());
            Thread.sleep(30000);
        } catch (Exception e) {

        } finally {
            //释放写锁
            rLock.unlock();
        }
        return "写成功";
    }

    @ResponseBody
    @GetMapping("/read")
    public String read() {
        RReadWriteLock rReadWriteLock = redissonClient.getReadWriteLock("ReadWriteLock");
        //获取读锁
        RLock rLock = rReadWriteLock.readLock();
        //上锁
        rLock.lock();
        String writeValue = "";
        try {
            writeValue = stringRedisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {

        } finally {
            //释放读锁
            rLock.unlock();
        }
        return "读到的数据：" + writeValue;
    }

    /**
     * 测试redisson分布式锁
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        RLock lock = redissonClient.getLock(RedisConstant.REDISSON_LOCK_KEY);
        try {
            lock.lock();
            log.info("上锁" + Thread.currentThread().getId());
            Thread.sleep(10000);
        } catch (Exception e) {
        } finally {
            lock.unlock();
            log.info("解锁");
        }
        return "hello world";
    }

    /**
     * 获取所有一级分类
     */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //查询一级分类
        List<CategoryEntity> categories = categoryService.getLevel1();
        //转发到thymeleaf页面中
        model.addAttribute("categories", categories);
        //默认前缀：classpath:/templates/  后缀：.html
        return "index";
    }

    /**
     * 获取二三级分类
     */
    @ResponseBody
    @GetMapping("/index/catelog.json")
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        return categoryService.getCatelogJson();
    }
}





















