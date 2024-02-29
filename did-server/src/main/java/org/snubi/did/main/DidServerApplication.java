package org.snubi.did.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;

@EnableScheduling
@ServletComponentScan
@EnableJpaAuditing(auditorAwareRef="loginUserAuditorAware") // JPA Auditing을 활성화 > 의존성주입 이슈 있다 
@SpringBootApplication
@EnableAsync
//@EntityScan(basePackages = "org.snubi.did.main.entity")
public class DidServerApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		//SpringApplication.run(DidResolverApplication.class, args);		
		ApplicationContext ctx = SpringApplication.run(DidServerApplication.class, args);
        DispatcherServlet dispatcherServlet = (DispatcherServlet)ctx.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
	}
}