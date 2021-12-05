package com.example.securitybestpractice.member;

import com.example.securitybestpractice.SecurityBestPracticeApplication;
import com.example.securitybestpractice.role.Role;
import com.example.securitybestpractice.role.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SecurityBestPracticeApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RoleRepository roleRepository;

    Member given_유저(String email, String password) {
        return Member.builder()
                .email("tahun970@naver.com")
                .password("123abc")
                .role(roleRepository.findByName("ROLE_USER"))
                .build();
    }

    @Test
    void 회원가입을_한다() throws Exception {

        Member user = given_유저("tahun970@naver.com", "123abc");

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAdmin
    @Test
    void 어드민유저가_어드민_권한이_있는_페이지로_요청할_수_있다() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAdmin
    @Test
    void 어드민유저가_유저_권한이_있는_페이지로_요청할_수_있다() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUser
    @Test
    void 일반유저가_어드민_권한이_있는_페이지로_요청할_수_없다() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithUser
    @Test
    void 일반유저가_유저_권한이_있는_페이지로_요청할_수_있다() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void 회원가입을_하면_폼로그인에서_로그인을_수행할_수_있다() throws Exception {
        String email = "tahun970@naver.com";
        String password = "123abc";
        Member user = given_유저(email, password);

        memberService.signUp(user);

        mockMvc.perform(formLogin().user(email).password(password))
                .andDo(print())
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    void 없는_회원으로_폼로그인에서_로그인을_수행할_수_없다() throws Exception {

        mockMvc.perform(formLogin().user("zzz@zzz.com").password("99999"))
                .andDo(print())
                .andExpect(unauthenticated());
    }
}