package com.example.securitybestpractice.init;

import com.example.securitybestpractice.member.Member;
import com.example.securitybestpractice.member.MemberService;
import com.example.securitybestpractice.role.Role;
import com.example.securitybestpractice.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitService {

    private final RoleInitService roleInitService;
    private final MemberInitService memberInitService;

    @PostConstruct
    public void init() {
        roleInitService.init();
        memberInitService.init();
    }

    @RequiredArgsConstructor
    @Component
    static class RoleInitService {
        private final RoleRepository roleRepository;

        public void init() {
            roleRepository.save(Role.builder().name("ROLE_TEMPORARY_USER").build());
            roleRepository.save(Role.builder().name("ROLE_USER").build());
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        }

    }

    @RequiredArgsConstructor
    @Component
    static class MemberInitService {

        private final MemberService memberService;
        private final RoleRepository roleRepository;

        @Transactional
        public void init() {

            Member temporaryUser = Member.builder()
                    .email("temporaryUser@blind.com")
                    .password("temporaryUser")
                    .role(roleRepository.findByName("ROLE_TEMPORARY_USER"))
                    .build();

            memberService.signUp(temporaryUser);

            Member user = Member.builder()
                    .email("user@blind.com")
                    .password("user")
                    .role(roleRepository.findByName("ROLE_USER"))
                    .build();

            memberService.signUp(user);

            Member admin = Member.builder()
                    .email("admin@blind.com")
                    .password("admin")
                    .role(roleRepository.findByName("ROLE_ADMIN"))
                    .build();

            memberService.signUp(admin);
        }

    }
}
