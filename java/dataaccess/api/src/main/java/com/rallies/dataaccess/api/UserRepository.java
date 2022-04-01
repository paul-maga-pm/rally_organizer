package com.rallies.dataaccess.api;

import models.User;

import java.util.Optional;

public interface UserRepository extends Repository<Long, User> {
    Optional<User> findUserByUserName(String username);
}
