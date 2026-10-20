package com.nmims.lms.config;

import com.nmims.lms.context.SchoolContextInterceptor;
import com.nmims.lms.context.UserContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private ApiLoggingInterceptor interceptor;

    @Autowired
    private SchoolContextInterceptor schoolContextInterceptor;

    @Autowired
    private UserContextInterceptor userContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(interceptor);
       registry.addInterceptor(schoolContextInterceptor);
       registry.addInterceptor(userContextInterceptor);
    }
}
