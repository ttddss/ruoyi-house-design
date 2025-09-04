package com.ruoyi.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.function.CustomConsumer;
import com.ruoyi.common.utils.function.CustomFunction;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 锁工具类
 *
 * @author tongdashuai
 * @version 1.0
 * @description
 * @module
 * @date 2024/8/13 13:51
 */
@Slf4j
public class LockUtils {

    /**
     * 开启方法锁
     *
     * @param lockKey 锁的key
     * @param waitTime 获取锁等待时间
     * @param leaseTime 锁的过期时间。如果小于等于0，会启用看门狗机制，每隔10s自动续期30s
     * @param unit 时间单位
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @return 返回operate的返回结果
     * @throws Exception
     */
    public static <T, R> R tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit, String tip, T t, CustomFunction<T, R> operate) throws Exception {
        RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
        RLock lock = redissonClient.getLock(RedisCache.genKey(lockKey));
        try {
            boolean lockAcquired;
            if (leaseTime > 0L) {
                // 尝试waitTime去获取锁，获取不到返回false。锁获取后会在leaseTime后释放掉
                lockAcquired = lock.tryLock(waitTime, leaseTime, unit);
            } else {
                // 尝试waitTime去获取锁，获取不到返回false。所得释放具有Watch Dog 自动延期机制,默认续30s 每隔30/3=10 秒续到30s
                lockAcquired = lock.tryLock(waitTime, unit);
            }
            if (lockAcquired) {
                return operate.apply(t);
            } else {
                log.info("redis获取锁失败");
                if (StrUtil.isBlank(tip)) {
                    throw new ServiceException("获取锁失败，请稍后重试");
                } else {
                    throw new ServiceException(tip);
                }
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 开启方法锁
     *
     * @param lockKey 锁的key
     * @param leaseTime 锁的过期时间。如果小于等于0，会启用看门狗机制，每隔10s自动续期30s
     * @param unit 时间单位
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @return 返回operate的返回结果
     * @throws Exception
     */
    public static <T, R> R tryLock(String lockKey, long leaseTime, TimeUnit unit, String tip, T t, CustomFunction<T, R> operate) throws Exception {
        return tryLock(lockKey, 0L, leaseTime, unit, tip, t, operate);
    }

    /**
     * 开启方法锁
     *
     * @param lockKey 锁的key
     * @param leaseTime 锁的过期时间。如果小于等于0，会启用看门狗机制，每隔10s自动续期30s
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @return 返回operate的返回结果
     * @throws Exception
     */
    public static <T, R> R tryLock(String lockKey, long leaseTime, String tip, T t, CustomFunction<T, R> operate) throws Exception {
        return tryLock(lockKey, 0L, leaseTime, TimeUnit.SECONDS, tip, t, operate);
    }

    /**
     * 开启方法锁
     * 默认5分钟后释放锁。
     *
     * @param lockKey 锁的key
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @return 返回operate的返回结果
     * @throws Exception
     */
    public static <T, R> R tryLock(String lockKey, String tip, T t, CustomFunction<T, R> operate) throws Exception {
        return tryLock(lockKey, 60 * 5, tip, t, operate);
    }

    /**
     * 开启方法锁
     *
     * @param lockKey 锁的key
     * @param waitTime 获取锁等待时间
     * @param leaseTime 锁的过期时间。如果小于等于0，会启用看门狗机制，每隔10s自动续期30s
     * @param unit 时间单位
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @throws Exception
     */
    public static <T> void tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit, String tip, T t, CustomConsumer<T> operate) throws Exception {
        RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
        RLock lock = redissonClient.getLock(RedisCache.genKey(lockKey));
        try {
            boolean lockAcquired;
            if (leaseTime > 0L) {
                // 尝试waitTime去获取锁，获取不到返回false。锁获取后会在leaseTime后释放掉
                lockAcquired = lock.tryLock(waitTime, leaseTime, unit);
            } else {
                // 尝试waitTime去获取锁，获取不到返回false。所得释放具有Watch Dog 自动延期机制,默认续30s 每隔30/3=10 秒续到30s
                lockAcquired = lock.tryLock(waitTime, unit);
            }
            if (lockAcquired) {
                operate.accept(t);
            } else {
                log.info("redis获取锁失败");
                if (StrUtil.isBlank(tip)) {
                    throw new ServiceException("获取锁失败，请稍后重试");
                } else {
                    throw new ServiceException(tip);
                }
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 开启方法锁
     *
     * @param lockKey 锁的key
     * @param leaseTime 锁的过期时间。如果小于等于0，会启用看门狗机制，每隔10s自动续期30s
     * @param unit 时间单位
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @return 返回operate的返回结果
     * @throws Exception
     */
    public static <T> void tryLock(String lockKey, long leaseTime, TimeUnit unit, String tip, T t, CustomConsumer<T> operate) throws Exception {
        tryLock(lockKey, 0L, leaseTime, unit, tip, t, operate);
    }

    /**
     * 开启方法锁
     *
     * @param lockKey 锁的key
     * @param leaseTime 锁的过期时间。如果小于等于0，会启用看门狗机制，每隔10s自动续期30s
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @return 返回operate的返回结果
     * @throws Exception
     */
    public static <T> void tryLock(String lockKey, long leaseTime, String tip, T t, CustomConsumer<T> operate) throws Exception {
        tryLock(lockKey, 0L, leaseTime, TimeUnit.SECONDS, tip, t, operate);
    }

    /**
     * 开启方法锁
     * 默认5分钟后释放锁。
     *
     * @param lockKey 锁的key
     * @param tip 错误提示信息
     * @param t 方法需要的参数
     * @param operate 需要开启锁的方法
     * @return 返回operate的返回结果
     * @throws Exception
     */
    public static <T> void tryLock(String lockKey, String tip, T t, CustomConsumer<T> operate) throws Exception {
        tryLock(lockKey, 60 * 5, tip, t, operate);
    }
}
