package com.example.yunhists.controller;

import com.example.yunhists.YunhistsServerApplication;
import com.example.yunhists.service.EmailVerificationService;
import com.example.yunhists.service.UserService;
import com.example.yunhists.utils.JwtHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = YunhistsServerApplication.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    EmailVerificationService evService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Order(1)
    @Test
    public void sendVerificationEmail_validEmail_200success() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.com"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/sendVerificationEmail")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":200"));
    }

    @Order(2)
    @Test
    public void sendVerificationEmail_sendAgain_211WaitOneMinute() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.com"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/sendVerificationEmail")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":211"));
    }

    @Order(3)
    @Test
    public void sendVerificationEmail_DMInternalError_212EmailFail() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.c"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/sendVerificationEmail")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":212"));
    }

    @Order(4)
    @Test
    public void sendVerificationEmail_invalidEmail_210InvalidEmail() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/sendVerificationEmail")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":210"));
    }

    @Order(11)
    @Test
    public void register_usernameInvalid_216UsernameLength() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.com"));
        params.put("username", Collections.singletonList("t"));
        params.put("password", Collections.singletonList("test"));
        params.put("password2", Collections.singletonList("test"));
        params.put("code", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":216"));
    }

    @Order(12)
    @Test
    public void register_passwordInvalid_217PasswordLength() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.com"));
        params.put("username", Collections.singletonList("test"));
        params.put("password", Collections.singletonList("t"));
        params.put("password2", Collections.singletonList("test"));
        params.put("code", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":217"));
    }

    @Order(13)
    @Test
    public void register_passwordNotMatch_207PasswordNotMatch() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.com"));
        params.put("username", Collections.singletonList("test"));
        params.put("password", Collections.singletonList("testtest"));
        params.put("password2", Collections.singletonList("testtes"));
        params.put("code", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":207"));
    }

    @Order(13)
    @Test
    public void register_emailRegistered_215EmailAlreadyRegistered() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test0@yunnanhistory.com"));
        params.put("username", Collections.singletonList("test"));
        params.put("password", Collections.singletonList("testtest"));
        params.put("password2", Collections.singletonList("testtest"));
        params.put("code", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":215"));
    }

    @Order(15)
    @Test
    public void register_emailVerificationNotSend_218NoVerificationCode() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("admin@yunnanhistory.com"));
        params.put("username", Collections.singletonList("test"));
        params.put("password", Collections.singletonList("testtest"));
        params.put("password2", Collections.singletonList("testtest"));
        params.put("code", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":218"));
    }

    @Order(16)
    @Test
    public void register_emailVerificationExpired_213VerificationCodeExpired() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test1@yunnanhistory.com"));
        params.put("username", Collections.singletonList("test"));
        params.put("password", Collections.singletonList("testtest"));
        params.put("password2", Collections.singletonList("testtest"));
        params.put("code", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":213"));
    }

    @Order(17)
    @Test
    public void register_incorrectVerificationCode_214VerificationCodeIncorrect() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.com"));
        params.put("username", Collections.singletonList("test"));
        params.put("password", Collections.singletonList("testtest"));
        params.put("password2", Collections.singletonList("testtest"));
        params.put("code", Collections.singletonList("aaaaaa"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":214"));
    }

    @Order(18)
    @Test
    public void register_correctVerificationCode_200success() throws Exception {
        String code = evService.read("test@yunnanhistory.com").getVerificationCode();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList("zh"));
        params.put("email", Collections.singletonList("test@yunnanhistory.com"));
        params.put("username", Collections.singletonList("test"));
        params.put("password", Collections.singletonList("testtest"));
        params.put("password2", Collections.singletonList("testtest"));
        params.put("code", Collections.singletonList(code));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":200"));
    }

    @Order(21)
    @Test
    public void login_withoutParams_400() throws Exception {
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertEquals("", responseString);
    }

    @Order(22)
    @Test
    public void login_validUser_200Success() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("email", Collections.singletonList("yuhong@gmail.com"));
        params.put("password", Collections.singletonList("123456"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":200"));
    }

    @Order(23)
    @Test
    public void login_wrongPwd_206IncorrectPwd() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("email", Collections.singletonList("yuhong@gmail.com"));
        params.put("password", Collections.singletonList("12345"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":206"));
    }

    @Order(24)
    @Test
    public void login_googleAccount_209GoogleAccount() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("email", Collections.singletonList("alex@gmail.com"));
        params.put("password", Collections.singletonList(""));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":209"));
    }

    @Order(25)
    @Test
    public void login_emailNotRegistered_208EmailNotRegistered() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("email", Collections.singletonList("test@gmail.com"));
        params.put("password", Collections.singletonList("test"));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":208"));
    }

    @Order(41)
    @Test
    public void updateLang_validUser_200success() throws Exception {
        String lang = "en";
        int userId = 3;
        String token = JwtHelper.createToken((long) userId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList(lang));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/updateLang")
                        .header("token", token)
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":200"));
        assertEquals(lang, userService.getUserById(userId).getLang());
    }

    @Order(42)
    @Test
    public void updateLang_invalidUserId_205NoUser() throws Exception {
        String lang = "en";
        int userId = 114514;
        String token = JwtHelper.createToken((long) userId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("lang", Collections.singletonList(lang));
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/updateLang")
                        .header("token", token)
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseString.startsWith("{\"code\":205"));
    }

}
