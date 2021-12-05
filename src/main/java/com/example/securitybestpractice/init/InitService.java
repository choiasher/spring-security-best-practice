package com.example.securitybestpractice.init;

import com.example.securitybestpractice.member.Member;
import com.example.securitybestpractice.member.MemberService;
import com.example.securitybestpractice.role.Privilege;
import com.example.securitybestpractice.role.PrivilegeRepository;
import com.example.securitybestpractice.role.Role;
import com.example.securitybestpractice.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitService {

    private final RoleInitService roleInitService;
    private final MemberInitService memberInitService;

    @PostConstruct
    public void init()  {
        roleInitService.init();
        memberInitService.init();
    }

    @RequiredArgsConstructor
    @Component
    static class RoleInitService {
        private final PrivilegeRepository privilegeRepository;
        private final RoleRepository roleRepository;

        public void init() {

            Privilege attachCorporation = privilegeRepository.save(Privilege.builder().name("ATTACH_CORPORATION").build());
            Privilege crudPostComment = privilegeRepository.save(Privilege.builder().name("READ_WRITE_POST_COMMENT").build());
            Privilege crudAll = privilegeRepository.save(Privilege.builder().name("ALL_CRUD").build());

            roleRepository.save(Role.builder().name("ROLE_TEMPORARY_USER").privileges(Set.of(attachCorporation)).build());
            roleRepository.save(Role.builder().name("ROLE_USER").privileges(Set.of(crudPostComment)).build());
            roleRepository.save(Role.builder().name("ROLE_ADMIN").privileges(Set.of(crudAll)).build());
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
