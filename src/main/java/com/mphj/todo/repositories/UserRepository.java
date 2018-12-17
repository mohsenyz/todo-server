package com.mphj.todo.repositories;

import com.mphj.todo.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    String TABLE_NAME = "user";

    @Query(value = "select count(id) from `" + TABLE_NAME + "` where email = :email", nativeQuery = true)
    int countByEmail(@Param("email") String email);

    @Query(value = "select * from `" + TABLE_NAME + "` where verification_code = :vc", nativeQuery = true)
    Optional<User> findByVerificationCode(@Param("vc") String token);

    @Query(value = "select * from `" + TABLE_NAME + "` where email = :e and is_verified = 1", nativeQuery = true)
    User findByEmail(@Param("e") String email);

    @Query(value = "select * from `" + TABLE_NAME + "` where email = :e", nativeQuery = true)
    User findUnverifiedByEmail(@Param("e") String email);

    @Query(value = "delete from `" + TABLE_NAME + "` where 1 = 1", nativeQuery = true)
    @Modifying
    @Transactional
    int clear();
}
