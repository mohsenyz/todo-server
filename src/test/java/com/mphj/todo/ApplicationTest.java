package com.mphj.todo;

import com.jayway.jsonpath.JsonPath;
import com.mphj.todo.models.request.LoginRequest;
import com.mphj.todo.models.request.RegisterRequest;
import com.mphj.todo.repositories.UserRepository;
import com.mphj.todo.repositories.UserSessionRepository;
import com.mphj.todo.utils.IntegrationTestUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserSessionRepository userSessionRepository;
    @Autowired
    private MockMvc mockMvc;
    private String userToken;

    @Test
    public void test1_userSignUp() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.email = "test@test.com";
        registerRequest.name = "test";
        registerRequest.password = "testtesttest";
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(registerRequest)))
                .andExpect(jsonPath("$.status", is("OK")))
                .andDo(print());


        String randomString = userRepository.findUnverifiedByEmail("test@test.com").verificationCode;

        mockMvc.perform(get("/user/register/verify")
                .param("token", randomString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("OK")))
                .andDo(print());
    }

    @Test
    public void test1_userSignUpErr1() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.email = "test@test.com";
        registerRequest.name = "test";
        registerRequest.password = "testtesttest";
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(registerRequest)))
                .andExpect(jsonPath("$.status", is(500)))
                .andDo(print());
    }

    @Test
    public void test1_userSignUpErr2() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.email = "test1@test.com";
        registerRequest.name = "test";
        registerRequest.password = "testtesttest";
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(registerRequest)))
                .andExpect(jsonPath("$.status", is("OK")))
                .andDo(print());

        String randomString = userRepository.findUnverifiedByEmail("test1@test.com").verificationCode;

        mockMvc.perform(get("/user/register/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", randomString.replace('a', 'b')))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.msg", is("BAD_VERIFICATION_TOKEN")))
                .andDo(print());
    }

    @Test
    public void test2_userSignIn() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.imei = UUID.randomUUID().toString();
        loginRequest.email = "test@test.com";
        loginRequest.password = "testtesttest";
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(loginRequest)))
                .andExpect(jsonPath("$.status", is("OK")))
                .andDo((res) -> {
                    userToken = JsonPath.read(res.getResponse().getContentAsString(), "$.token");
                })
                .andDo(print());
    }

    @Test
    public void test2_userSignInErr() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.imei = UUID.randomUUID().toString();
        loginRequest.email = "test@test.com";
        loginRequest.password = "testtesttest123";
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(loginRequest)))
                .andExpect(jsonPath("$.status", is(403)))
                .andDo(print());
    }

    @Test
    public void test3_listCreation() {

    }


    @Test
    public void test4_todoCreation() {

    }


    @Test
    public void test4_todoAndListCreation() {

    }


    @Test
    public void test5_userSync() {

    }

    @Test
    public void zCleanUp() {
        userSessionRepository.clear();
        userRepository.clear();
    }


}
