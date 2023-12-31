package fr.uge.gitclout;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;

/**
 * Configuration class for enabling asynchronous execution using virtual threads in Spring.
 */
@EnableAsync
@Configuration
public class ThreadConfig {

    /**
     * Provides an application task executor using virtual threads.
     *
     * @return AsyncTaskExecutor configured to use virtual threads.
     */
    @Bean
    public AsyncTaskExecutor applicationTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * Configures a custom Tomcat protocol handler using virtual threads for execution.
     *
     * @return TomcatProtocolHandlerCustomizer configuring the executor for virtual threads.
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
