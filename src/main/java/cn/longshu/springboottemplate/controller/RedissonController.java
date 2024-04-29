package cn.longshu.springboottemplate.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : longshu
 * @description : redisson控制类测试
 * @createTime : 2024-04-29 21:31:27
 */
@RestController
@Slf4j
@RequestMapping("/redisson")
public class RedissonController {
    private final RedissonClient redissonClient;

    public RedissonController(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @PostMapping("/save")
    public String save(String key, String value){
        RLock rLock = redissonClient.getLock("lock");
        log.info(rLock.getName());
        try {
            rLock.lock();
            Thread.sleep(100000);
            System.out.println("test");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return null;
    }
}
