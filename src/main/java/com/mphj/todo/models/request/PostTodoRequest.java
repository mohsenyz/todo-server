package com.mphj.todo.models.request;

import com.mphj.todo.entities.Todo;
import com.mphj.todo.entities.UserList;

import java.util.List;

public class PostTodoRequest {

    public long lastUpdateTime;

    public List<Todo> todoList;

    public List<UserList> userLists;
}
