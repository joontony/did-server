package org.snubi.did.main.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;


@Slf4j
@Component
public class LoginUserAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
        	//log.info("LoginUserAuditorAware user >>> null");
            return null;
        }
        //BrainUser user = (BrainUser) authentication.getPrincipal();
        String user = authentication.getPrincipal().toString();
        //log.info("LoginUserAuditorAware user {}", authentication.getPrincipal());
        
       // return Optional.of(user.getUserId());
    	return Optional.of(user);
    }
}