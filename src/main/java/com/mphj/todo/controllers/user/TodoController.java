package com.mphj.todo.controllers.user;


import com.mphj.todo.entities.User;
import com.mphj.todo.models.request.PostTodoRequest;
import com.mphj.todo.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {

    @PreAuthorize(User.Roles.USER_VERIFIED)
    @PostMapping("/user/todo")
    public Object postTodo(PostTodoRequest postTodoRequest, User user) {
        return null;
    }

}
