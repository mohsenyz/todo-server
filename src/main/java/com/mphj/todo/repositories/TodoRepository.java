package com.mphj.todo.repositories;

import com.mphj.todo.entities.Todo;
import com.mphj.todo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {

    String TABLE_NAME = "todo";

    @Query(value = "select updated_at from `" + TABLE_NAME + "` where user_id = :user_id", nativeQuery = true)
    long lastUpdateByUser(@Param("user_id") int userId);

    @Query(value = "select count(id) from `" + TABLE_NAME + "` where id in (:ids) and user_id = :user_id")
    int countByIdsAndUserId(@Param("ids") List<Integer> ids, @Param("user_id") int userId);

}
