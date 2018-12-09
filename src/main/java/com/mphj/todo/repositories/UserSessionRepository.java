package com.mphj.todo.repositories;

import com.mphj.todo.entities.UserSession;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
}
