package com.mphj.todo.controllers.user;


import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.Todo;
import com.mphj.todo.entities.User;
import com.mphj.todo.entities.UserList;
import com.mphj.todo.models.request.PostTodoRequest;
import com.mphj.todo.models.response.PostTodoResponse;
import com.mphj.todo.models.response.SavedObjectResponse;
import com.mphj.todo.repositories.TodoRepository;
import com.mphj.todo.repositories.UserListRepository;
import com.mphj.todo.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TodoController {

    @Autowired
    TodoRepository todoRepo;

    @Autowired
    UserListRepository usrListRepo;

    @PostMapping("/user/todo")
    @Secured(User.Roles.USER_VERIFIED)
    public Object postTodo(@RequestBody PostTodoRequest ptRequest, @AuthenticationPrincipal User user) {
        // Check last update time
        int userId = user.id;
        if (ptRequest.lastUpdateTime < Math.max(
                todoRepo.lastUpdateByUser(userId).orElse(BigInteger.ZERO).longValue(),
                usrListRepo.lastUpdateByUser(userId).orElse(BigInteger.ZERO).longValue())) {
            return ErrorUtils.from(402, Constants.Error.OUTDATED_DATA);
        }

        long currentTime = System.currentTimeMillis();
        ;

        List<UserList> newUserLists = new ArrayList<>();

        if (ptRequest.userLists != null && !ptRequest.userLists.isEmpty()) {
            // Check todo owner
            newUserLists = ptRequest.userLists
                    .stream()
                    .filter((t) -> t.id == 0)
                    .collect(Collectors.toList());
            List<Integer> ids = newUserLists
                    .stream()
                    .map((t) -> t.id)
                    .collect(Collectors.toList());
            List<Integer> preSavedIds = newUserLists
                    .stream()
                    .filter((t) -> t.id != 0)
                    .map((t) -> t.id)
                    .collect(Collectors.toList());
            if (preSavedIds.size() > 0 && usrListRepo.countByIdsAndUserId(preSavedIds, userId) != ids.size())
                return ErrorUtils.from(500, Constants.Error.BAD_REQUEST);

            ptRequest.userLists
                    .stream()
                    .forEach((t) -> {
                        t.user = user;
                        t.updatedAt = currentTime;
                    });

            usrListRepo.saveAll(ptRequest.userLists);
        }


        List<Todo> newTodoList = new ArrayList<>();

        if (ptRequest.todoList != null && !ptRequest.todoList.isEmpty()) {
            newTodoList = ptRequest.todoList
                    .stream()
                    .filter((t) -> t.id == 0)
                    .collect(Collectors.toList());
            List<Integer> ids = newTodoList
                    .stream()
                    .map((t) -> t.id)
                    .collect(Collectors.toList());
            List<Integer> preSavedIds = newTodoList
                    .stream()
                    .filter((t) -> t.id != 0)
                    .map((t) -> t.id)
                    .collect(Collectors.toList());
            if (preSavedIds.size() > 0 && todoRepo.countByIdsAndUserId(preSavedIds, userId) != ids.size())
                return ErrorUtils.from(500, Constants.Error.BAD_REQUEST);

            ptRequest.todoList
                    .stream()
                    .forEach((t) -> {
                        t.user = user;
                        t.updatedAt = currentTime;
                    });

            todoRepo.saveAll(ptRequest.todoList);
        }

        PostTodoResponse ptResponse = new PostTodoResponse();
        ptResponse.status = 200;
        ptResponse.lastUpdateTime = currentTime;
        ptResponse.todoList = newTodoList
                .stream()
                .map((t) -> SavedObjectResponse.from(t.localId, t.id))
                .collect(Collectors.toList());
        ptResponse.userLists = newUserLists
                .stream()
                .map((t) -> SavedObjectResponse.from(t.localId, t.id))
                .collect(Collectors.toList());
        return ptResponse;
    }

}
