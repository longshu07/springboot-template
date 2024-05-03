package cn.longshu.springboottemplate.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

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

    /**
     * redisson看门狗默认模式
     * 默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
     * 如果我们未制定 lock 的超时时间，就使用 30 秒作为看门狗的默认时间。只要占锁成功，就会启动一个定时任务：每隔 10 秒重新给锁设置过期的时间，过期时间为 30 秒。
     * 重点：如果设置了锁过期时间，那么看门狗将不会起作用，不会再自动续期
     */
    @PostMapping("/defaultWatchDogTest")
    public String save(){
        RLock rLock = redissonClient.getLock("lock");
        log.info(rLock.getName());
        try {
            rLock.lock();
            Thread.sleep(100 * 1000);
            System.out.println("test");
        } catch (Exception e) {
            log.error("defaultWatchDogTest", e);
        } finally {
            rLock.unlock();
        }
        return null;
    }

    /**
     * Redisson 的可重入锁（lock）是阻塞其他线程的，需要等待其他线程释放的
     */
    @GetMapping("rLockBlockTest")
    public String rLockBlockTest(){
        // 1.获取锁，只要锁的名字一样，获取到的锁就是同一把锁。
        RLock lock = redissonClient.getLock("longshu-lock");

        // 2.加锁
        lock.lock();
        try {
            log.info("加锁成功，执行后续代码。当前线程名称：{}， 当前线程 ID：{}", Thread.currentThread().getName(), Thread.currentThread().getId());
            // 等待十秒
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            log.error("rLockBlockTest error", e);
        } finally {
            // 3.解锁
            lock.unlock();
            log.info("finally，释放锁成功。当前线程 ID：{}", Thread.currentThread().getId());
        }

        return "test rLockBlock is ok";
    }

    @GetMapping("rLockExpirationTimeTest")
    public String rLockExpirationTimeTest(){
        // 1.获取锁，只要锁的名字一样，获取到的锁就是同一把锁。
        RLock lock = redissonClient.getLock("longshu-lock");

        // 2.加锁，设置8秒过期时间
        // 重点：如果设置了锁过期时间，那么看门狗将不会起作用，不会再自动续期
        lock.lock(20, TimeUnit.SECONDS);
        try {
            log.info("加锁成功，执行后续代码。当前线程名称：{}， 当前线程 ID：{}", Thread.currentThread().getName(), Thread.currentThread().getId());
            // 等待十秒
            Thread.sleep(30 * 1000);
        } catch (Exception e) {
            log.error("rLockExpirationTimeTest error", e);
        } finally {
            // 3.解锁
            lock.unlock();
            log.info("finally，释放锁成功。当前线程 ID：{}", Thread.currentThread().getId());
        }

        return "test rLockExpirationTime is ok";
    }
}
