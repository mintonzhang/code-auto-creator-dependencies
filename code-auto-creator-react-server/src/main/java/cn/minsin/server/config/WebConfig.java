package cn.minsin.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: minton.zhang
 * @since: 2020/5/30 16:46
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure cross origin requests processing.
     *
     * @since 4.2
     */
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST")
                .maxAge(3600);
    }
}
