package interfaces;

import models.User;

public interface UserRepository extends Repository<Long, User> {
    User findUserByUserName(String username);
}
