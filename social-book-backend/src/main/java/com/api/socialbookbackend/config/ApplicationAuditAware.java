package com.api.socialbookbackend.config;

import com.api.socialbookbackend.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {

    /**
     * This method is used to get the current user id
     * @return
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication ==null || authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()){
            return Optional.empty();
        }
        User user = (User) authentication.getPrincipal();
        return Optional.ofNullable(user.getId());
    }
}
