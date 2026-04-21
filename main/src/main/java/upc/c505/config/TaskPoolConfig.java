package upc.c505.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池相关配置
 *
 * @author qiutian
 */
@Configuration
@EnableAsync
public class TaskPoolConfig {
    @Bean("asyncExecutor")
    public ThreadPoolTaskExecutor asyncExecutor() {
        Runtime runtime = Runtime.getRuntime();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        设置核心线程数为CPU核心数
        taskExecutor.setCorePoolSize(runtime.availableProcessors());
//        设置最大线程数为CPU核心数两倍
        taskExecutor.setMaxPoolSize(runtime.availableProcessors() * 2);
//        阻塞队列长度
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setKeepAliveSeconds(60);
//        线程名称前缀
        taskExecutor.setThreadNamePrefix("taskExecutor--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }
    @Bean("ScheduledExecutorService")
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        Runtime runtime = Runtime.getRuntime();
        return new ScheduledThreadPoolExecutor(
                runtime.availableProcessors(),
                asyncExecutor().getThreadPoolExecutor().getThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
