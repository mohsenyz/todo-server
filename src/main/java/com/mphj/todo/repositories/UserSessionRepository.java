package com.mphj.todo.repositories;

import com.mphj.todo.entities.UserSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, Long> {

    String TABLE_NAME = "user_session";

    @Query(value = "select token from `" + TABLE_NAME + "` where id = :id and imei = :imei", nativeQuery = true)
    String findTokenByIdAndIMEI(@Param("id") int id, @Param("imei") String imei);

    @Query(value = "select * from `" + TABLE_NAME +"` where token = :token", nativeQuery = true)
    Optional<UserSession> findByToken(@Param("token") String token);

    @Query(value = "delete from `" + TABLE_NAME + "` where 1 = 1", nativeQuery = true)
    @Modifying
    @Transactional
    int clear();
}
