package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import org.example.expert.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // ArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver());
    }

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // InterceptorRegistry- 인터셉터 관리 컨테이너
        // addInterceptors - 인터셉터를 등록하는 역할을 함
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/comments/{commentId}")
                .addPathPatterns("/admin/users/{userId}");
    }

}
