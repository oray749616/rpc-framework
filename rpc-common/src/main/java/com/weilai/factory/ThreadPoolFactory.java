package com.weilai.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName ThreadPoolFactory
 * @Description: 线程池工厂
 */

@Slf4j
@NoArgsConstructor
public class ThreadPoolFactory {

    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix) {
        CustomThreadPoolConfig config = new CustomThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(threadNamePrefix, config, false);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, CustomThreadPoolConfig config) {
        return createCustomThreadPoolIfAbsent(threadNamePrefix, config, false);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, CustomThreadPoolConfig config, Boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix, k -> createCustomThreadPoolIfAbsent(threadNamePrefix, config, daemon));

        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadNamePrefix);
            threadPool = createCustomThreadPoolIfAbsent(threadNamePrefix, config, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
        return threadPool;
    }

    public static void shutDownAllThreadPool() {
        log.info("关闭所有线程池");
        THREAD_POOLS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService service = entry.getValue();
            service.shutdown();
            log.info("关闭线程池 [{}] [{}]", entry.getKey(), service.isTerminated());

            try {
                service.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("线程池关闭失败");
                service.shutdownNow();
            }
        });
    }

    private static ExecutorService createThreadPool(String threadNamePrefix, CustomThreadPoolConfig config, Boolean daemon) {
        ThreadFactory factory = createThreadFactory(threadNamePrefix, daemon);

        return new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaximumPoolSize(),
                config.getKeepAliveTime(), config.getUnit(), config.getWorkQueue(), factory);
    }

    /**
     * 创建 ThreadFactory
     * @param threadNamePrefix 作为创建线程名字的前缀
     * @param daemon 指定是否为 Daemon Thread (守护线程)
     * @return ThreadFactory
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(daemon).build();
            }
        } else {
            return new ThreadFactoryBuilder().setNameFormat(null + "-%d").build();
        }

        return Executors.defaultThreadFactory();
    }

    /**
     * 输出线程池状态
     * @param threadPool 线程池对象
     */
    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-thread-pool-status", false));
        service.scheduleAtFixedRate(() -> {
            log.info("============ThreadPool Status=============");
            log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
            log.info("Active Threads: [{}]", threadPool.getActiveCount());
            log.info("Number of Tasks : [{}]", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("==========================================");
        }, 0, 1, TimeUnit.SECONDS);
    }
}
