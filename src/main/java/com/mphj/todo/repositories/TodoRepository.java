package com.mphj.todo.repositories;

import com.mphj.todo.entities.Todo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {

    String TABLE_NAME = "todo";

    @Query(value = "select max(updated_at) from `" + TABLE_NAME + "` where user_id = :user_id", nativeQuery = true)
    Optional<BigInteger> lastUpdateByUser(@Param("user_id") int userId);

    @Query(value = "select count(id) from `" + TABLE_NAME + "` where id in (:ids) and user_id = :user_id", nativeQuery = true)
    int countByIdsAndUserId(@Param("ids") List<Integer> ids, @Param("user_id") int userId);

    @Query(value = "select * from `" + TABLE_NAME + "` where updated_at > :updated_at and user_id = :user_id order by updated_at desc", nativeQuery = true)
    List<Todo> findAllUpdatedSince(@Param("updated_at") long updatedAt, @Param("user_id") int userId);

    @Query(value = "delete from `" + TABLE_NAME + "` where 1 = 1", nativeQuery = true)
    @Modifying
    @Transactional
    int clear();

}
