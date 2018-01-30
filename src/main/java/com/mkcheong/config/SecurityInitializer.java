package com.mkcheong.config;

import com.mkcheong.security.SecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityInitializer() { super(SecurityConfig.class, HttpSessionConfig.class); }
}