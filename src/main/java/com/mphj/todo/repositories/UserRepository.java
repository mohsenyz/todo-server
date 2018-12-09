package com.mphj.todo.repositories;

import com.mphj.todo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    String TABLE_NAME = "user";

    @Query(value = "select count(id) from `" + TABLE_NAME + "` where email = :email", nativeQuery = true)
    int countByEmail(@Param("email") String email);

}
