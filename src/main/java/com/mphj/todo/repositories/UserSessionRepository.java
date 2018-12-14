package com.mphj.todo.repositories;

import com.mphj.todo.entities.UserSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {

    public static final String TABLE_NAME = "user_session";

    @Query(value = "select token from `" + TABLE_NAME + "` where id = :id and imei = :imei", nativeQuery = true)
    String findTokenByIdAndIMEI(@Param("id") int id, @Param("imei") String imei);

    @Query(value = "select `" + TABLE_NAME
            + "`.*` from `" + UserSessionRepository.TABLE_NAME
            + "`, `" + TABLE_NAME
            + "` where token = :token", nativeQuery = true)
    Optional<UserSession> findByToken(@Param("token") String token);
}
