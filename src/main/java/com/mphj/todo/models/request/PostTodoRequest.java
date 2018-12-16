package com.mphj.todo.models.request;

import com.mphj.todo.entities.Todo;
import com.mphj.todo.entities.UserList;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

public class PostTodoRequest {

    public int lastUpdateTime;

    public List<Todo> todoList;

    public List<UserList> userLists;
}
