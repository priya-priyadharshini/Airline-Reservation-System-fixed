package com.airline.config;
import com.airline.config.AuthInterceptor; import org.springframework.context.annotation.*; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class AppConfig implements WebMvcConfigurer{
 private final AuthInterceptor interceptor; public AppConfig(AuthInterceptor interceptor){this.interceptor=interceptor;}
 @Bean public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Override public void addInterceptors(InterceptorRegistry registry){registry.addInterceptor(interceptor).addPathPatterns("/admin/**","/user/**");}
}
