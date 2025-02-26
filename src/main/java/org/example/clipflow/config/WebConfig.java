package org.example.clipflow.config;

import org.example.clipflow.service.user.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserService userService;

    public WebConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(userService))
                .addPathPatterns("/admin/**","/authorize/**")
                .addPathPatterns("/luckyjourney/**")
                .excludePathPatterns("/luckyjourney/login/**","/luckyjourney/index/**","/luckyjourney/cdn/**", "/luckyjourney/file/**");
    }
}
