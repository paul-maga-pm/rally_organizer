package service;

import exceptions.AuthenticationException;
import interfaces.UserRepository;
import models.User;
import utils.PasswordEncryptor;

import java.util.Optional;

public class UserService {
    private UserRepository userRepository;
    private PasswordEncryptor encryptor;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        var foundUserOptional = userRepository.findUserByUserName(username);

        var foundUser = foundUserOptional.orElseThrow(() -> {
            throw new AuthenticationException("Username doesn't exist!");
        });

        if (encryptor.authenticate(password, foundUser.getPassword()))
            return foundUser;

        throw new AuthenticationException("Incorrect password!");
    }

    public User register(String username, String password) {
        if (userRepository.findUserByUserName(username).isPresent())
            throw new AuthenticationException("Username " + username + " is already used!");

        String hashPassword = encryptor.hash(password);
        User user = new User(username, hashPassword);
        return userRepository.save(user);
    }
}
