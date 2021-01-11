package io.github.xiaohua.feign.spring;

import feign.Contract;
import feign.Feign;
import feign.RequestLine;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.xiaohua.feign.spring.FeignTest.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author z-shenghua
 * @date 2021/1/11
 */
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = AppConfig.class)
public class FeignTest {
    @Autowired
    FeignTestService feignTestService;

    @Test
    void githubTest() {
        String request = feignTestService.request();
        System.out.println(request);
    }

    @FeignClient(url = "https://api.github.com", name = "test")
    public interface FeignTestService {

        @RequestLine("GET /repos/z-xiaohua/feign-spring/git/trees/main")
        String request();

    }

    @Configuration
    @EnableFeignClients(basePackages = "io.github.xiaohua.feign.spring")
    static class AppConfig {

        @Bean
        public static FeignContext feignContext() {
            return new FeignContext();
        }

        @Bean
        public FeignLoggerFactory feignLoggerFactory() {
            return new DefaultFeignLoggerFactory(null);
        }

        @Bean
        public Feign.Builder feignBuilder(Retryer retryer) {
            return Feign.builder().retryer(retryer);
        }

        @Bean
        public Retryer feignRetryer() {
            return Retryer.NEVER_RETRY;
        }

        @Bean
        public Decoder feignDecoder() {
            return new Decoder.Default();
//            return new JacksonDecoder();
        }

        @Bean
        public Encoder feignEncoder() {
            return new Encoder.Default();
//            return new JacksonEncoder();
        }

        @Bean
        public Contract feignContract() {
            // 如果想支持 Spring Mvc 的注解, 可参考 Spring Cloud Openfeign 中的 SpringMvcContract
            return new Contract.Default();
        }

        @Bean
        public FeignClientProperties feignClientProperties() {
            return new FeignClientProperties();
        }

        @Bean
        public Targeter feignTargeter() {
            return new DefaultTargeter();
        }
    }
}
