package com.fh.conf;

import com.fh.common.MenberCon;
import com.fh.interceptors.IdempotentInterceptor;
import com.fh.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class Appconfig extends WebMvcConfigurationSupport {


    @Autowired
    private com.fh.common.MenberCon menberCon;

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Bean
    public IdempotentInterceptor idempotentInterceptor(){
        return new IdempotentInterceptor();
    }


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(idempotentInterceptor()).addPathPatterns("/**");
    }

}
