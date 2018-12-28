package com.example.consumer.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * RestTemplate 定制器
 * @Author NicholasLiu
 * @Date 2018/12/28 10:52
 **/
@EnableScheduling
@Configuration
public class CustomRestTemplateCustomizer implements RestTemplateCustomizer {
    private static final Logger log = LoggerFactory.getLogger(CustomRestTemplateCustomizer.class);

    private static final int MAX_TOTAL_CONNECTIONS = 300;
    private static final int MAX_CONNECTION_PER_ROUTE = 100;
    private static final int CONNECT_TIMEOUT = 30000;
    private static final int SOCKET_TIMEOUT = 30000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 30000;
    private static final int CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS = 60;
    public static final PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();

    @Primary
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        poolingConnectionManager.setDefaultMaxPerRoute(MAX_CONNECTION_PER_ROUTE);
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager)
                .disableCookieManagement()
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
    }

    @Bean
    public Runnable idleConnectionMonitor() {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelay = 60000)
            public void run() {
                try {
                    if (poolingConnectionManager != null) {
                        log.info("run IdleConnectionMonitor - Closing expired and idle connections...");
                        poolingConnectionManager.closeExpiredConnections();
                        poolingConnectionManager.closeIdleConnections(CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS, TimeUnit.SECONDS);
                    }
                } catch (Exception e) {
                    log.error("run IdleConnectionMonitor - Exception occurred.", e);
                }
            }
        };
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        return scheduler;
    }
}



