package com.example.securitybestpractice.member.adaptor;

import com.example.securitybestpractice.member.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@EqualsAndHashCode(callSuper = false)
@Getter
public class CurrentUser extends User {

    private final Member member;

    public CurrentUser(Member member) {
        super(member.getEmail(),
                member.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getRole())));
        this.member = member;
    }
}
