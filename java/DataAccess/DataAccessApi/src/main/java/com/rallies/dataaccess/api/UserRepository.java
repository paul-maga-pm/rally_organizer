package com.rallies.dataaccess.api;

import com.rallies.domain.models.User;

import java.util.Optional;

public interface UserRepository extends Repository<Long, User> {
    Optional<User> findUserByUserName(String username);
}
