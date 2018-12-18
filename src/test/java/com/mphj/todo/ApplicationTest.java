package com.mphj.todo;

import com.jayway.jsonpath.JsonPath;
import com.mphj.todo.entities.Flag;
import com.mphj.todo.entities.Todo;
import com.mphj.todo.entities.TodoTask;
import com.mphj.todo.entities.UserList;
import com.mphj.todo.models.request.LoginRequest;
import com.mphj.todo.models.request.PostTodoRequest;
import com.mphj.todo.models.request.RegisterRequest;
import com.mphj.todo.repositories.TodoRepository;
import com.mphj.todo.repositories.UserListRepository;
import com.mphj.todo.repositories.UserRepository;
import com.mphj.todo.repositories.UserSessionRepository;
import com.mphj.todo.utils.IntegrationTestUtils;
import net.minidev.json.JSONArray;
import org.junit.Assert;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
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

    private static String userToken;
    private static UserList userList1;

    @Autowired
    private MockMvc mockMvc;
    private static long lastUpdatedTime;

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

    @Autowired
    UserListRepository userListRepository;

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

    @Autowired
    TodoRepository todoRepository;

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
                .param("token", new StringBuilder(randomString).reverse().toString()))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.msg", is("BAD_VERIFICATION_TOKEN")))
                .andDo(print());
    }

    @Test
    public void test3_listCreation() throws Exception {
        List<UserList> userLists = new ArrayList<>();

        userList1 = new UserList();
        userList1.name = "Cat-1";
        userList1.localId = 1;
        userLists.add(userList1);

        final UserList userList2 = new UserList();
        userList2.name = "Cat-2";
        userList2.localId = 2;
        userLists.add(userList2);

        final PostTodoRequest ptRequest = new PostTodoRequest();
        ptRequest.todoList = new ArrayList<>();
        ptRequest.userLists = userLists;
        ptRequest.lastUpdateTime = 0;

        mockMvc.perform(post("/user/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(ptRequest))
                .header("Token", userToken))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.userLists", hasSize(2)))
                .andDo((res) -> {
                    String json = res.getResponse().getContentAsString();
                    userList1.id = (int) ((JSONArray) JsonPath.read(json, "$.userLists[?(@.localId == 1)].serverId")).get(0);
                    userList2.id = (int) ((JSONArray) JsonPath.read(json, "$.userLists[?(@.localId == 2)].serverId")).get(0);
                    ptRequest.lastUpdateTime = (long) JsonPath.read(json, "$.lastUpdateTime") + 1;
                })
                .andDo(print());

        ptRequest.userLists.remove(1);
        userList1.name = "U-Cat-1";

        mockMvc.perform(post("/user/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(ptRequest))
                .header("Token", userToken))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.userLists", hasSize(0)))
                .andDo((res) -> {
                    String json = res.getResponse().getContentAsString();
                    lastUpdatedTime = (long) JsonPath.read(json, "$.lastUpdateTime") + 1;
                })
                .andDo(print());

    }



    @Test
    public void test4_todoCreation() throws Exception {
        List<Todo> todoList = new ArrayList<>();

        userList1.name = null;
        userList1.user = null;

        final Todo todo1 = new Todo();
        todo1.userList = userList1;
        todo1.localId = 1;
        todo1.priority = 1;
        todo1.content = "Todo-1";
        todo1.todoTasks = Arrays.asList(new TodoTask("task-1-1"), new TodoTask("task-1-2"));
        todo1.flags = Arrays.asList(new Flag("flag-1-1"), new Flag("flag-1-2"));
        todoList.add(todo1);

        final Todo todo2 = new Todo();
        todo2.userList = userList1;
        todo2.localId = 2;
        todo2.priority = 1;
        todo2.content = "Todo-2";
        todo2.todoTasks = Arrays.asList(new TodoTask("task-2-1"), new TodoTask("task-2-2"));
        todo2.flags = Arrays.asList(new Flag("flag-2-1"), new Flag("flag-2-2"));
        todoList.add(todo2);

        final PostTodoRequest ptRequest = new PostTodoRequest();
        ptRequest.todoList = todoList;
        ptRequest.userLists = new ArrayList<>();
        ptRequest.lastUpdateTime = lastUpdatedTime + 1;

        mockMvc.perform(post("/user/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(ptRequest))
                .header("Token", userToken))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.todoList", hasSize(2)))
                .andDo((res) -> {
                    String json = res.getResponse().getContentAsString();
                    todo1.id = (int) ((JSONArray) JsonPath.read(json, "$.todoList[?(@.localId == 1)].serverId")).get(0);
                    todo2.id = (int) ((JSONArray) JsonPath.read(json, "$.todoList[?(@.localId == 2)].serverId")).get(0);
                    System.out.println("TAGG" + todo1.id);
                    ptRequest.lastUpdateTime = (long) JsonPath.read(json, "$.lastUpdateTime") + 1;
                })
                .andDo(print());

        ptRequest.todoList.remove(1);
        todo1.content = "U-Todo-1";

        mockMvc.perform(post("/user/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestUtils.convertObjectToJsonBytes(ptRequest))
                .header("Token", userToken))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.todoList", hasSize(0)))
                .andDo(print());

        Assert.assertTrue(userListRepository.count() == 2);
    }


    @Test
    public void test5_userSync() throws Exception {
        mockMvc.perform(get("/user/sync")
                .param("lastUpdateTime", "0")
                .header("Token", userToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userLists", hasSize(2)))
                .andExpect(jsonPath("$.todoList", hasSize(2)))
                .andDo(print());
    }

    @Test
    public void zCleanUp() {
        userListRepository.clear();
        userSessionRepository.clear();
        userRepository.clear();
        todoRepository.clear();
    }


}
