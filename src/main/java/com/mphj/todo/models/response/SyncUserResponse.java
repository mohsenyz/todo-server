package com.mphj.todo.models.response;

import com.mphj.todo.entities.Todo;
import com.mphj.todo.entities.UserList;

import java.util.List;

public class SyncUserResponse {

    public int status;

    public long updateTime;

    public List<UserList> userLists;

    public List<Todo> todoList;

}
