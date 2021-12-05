package com.example.securitybestpractice.member;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithMockUser
@Retention(RetentionPolicy.RUNTIME)
public @interface WithUser {
}
