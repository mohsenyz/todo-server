package com.mphj.todo.controllers.user;

import com.mphj.todo.confs.Constants;
import com.mphj.todo.entities.Todo;
import com.mphj.todo.entities.User;
import com.mphj.todo.entities.UserList;
import com.mphj.todo.models.response.SyncUserResponse;
import com.mphj.todo.repositories.TodoRepository;
import com.mphj.todo.repositories.UserListRepository;
import com.mphj.todo.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
public class UserSyncController {

    @Autowired
    TodoRepository todoRepo;

    @Autowired
    UserListRepository usrListRepo;

    @GetMapping("/user/sync")
    @Secured(User.Roles.USER_VERIFIED)
    public Object sync(@RequestParam("lastUpdateTime") long lastUpdateTime, @AuthenticationPrincipal User user) {
        int userId = user.id;
        if (lastUpdateTime >= Math.max(todoRepo.lastUpdateByUser(userId).orElse(BigInteger.ZERO).longValue(), usrListRepo.lastUpdateByUser(userId).orElse(BigInteger.ZERO).longValue())) {
            return ErrorUtils.from(401, Constants.Error.ALREADY_UPDATED);
        }

        List<UserList> userLists = usrListRepo.findAllUpdatedSince(lastUpdateTime, userId);
        List<Todo> todoList = todoRepo.findAllUpdatedSince(lastUpdateTime, userId);

        long updateTime = Math.max(
                (userLists.size() == 0) ? lastUpdateTime : userLists.get(0).updatedAt,
                (todoList.size() == 0) ? lastUpdateTime : todoList.get(0).updatedAt
        );

        SyncUserResponse suResponse = new SyncUserResponse();
        suResponse.updateTime = updateTime;
        suResponse.userLists = userLists;
        suResponse.todoList = todoList;
        suResponse.status = 200;
        return suResponse;
    }

}
