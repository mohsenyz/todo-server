package com.mphj.todo.models.response;

import java.util.List;

public class PostTodoResponse {

    public int status;

    public long lastUpdateTime;

    public List<SavedObjectResponse> todoList;

    public List<SavedObjectResponse> userLists;

}
