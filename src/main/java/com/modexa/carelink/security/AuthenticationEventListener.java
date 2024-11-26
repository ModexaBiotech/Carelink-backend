package com.modexa.carelink.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        logger.info("User '{}' logged in successfully", 
            success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        logger.error("Login failed for user '{}': {}", 
            failures.getAuthentication().getName(),
            failures.getException().getMessage());
        logger.debug("Authentication failure details:", failures.getException());
    }
}
