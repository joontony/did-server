package org.snubi.did.main.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfiguration implements WebMvcConfigurer {
	@Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {		
		
		// stomp
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(60 * 60 * 24 * 365); 
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/").resourceChain(false);
	    // stomp 
		
        registry.addResourceHandler("/club/image/**")
        .addResourceLocations("file://"+ CustomConfig.strFileUploadPath + "/") 
        // 접근 파일 캐싱 시간 
	    .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES));
    }
}
