package com.mphj.todo.models.response;

import org.springframework.data.util.Pair;

import java.util.List;

public class PostTodoResponse {

    public int status;

    public List<SavedObjectResponse> todoList;

    public List<SavedObjectResponse> userLists;

}
