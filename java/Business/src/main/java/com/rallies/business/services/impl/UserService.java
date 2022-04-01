package com.rallies.business.services.impl;


import com.rallies.domain.models.User;
import com.rallies.dataaccess.api.UserRepository;

public class UserService {
    private UserRepository userRepository;
    private PasswordEncryptor encryptor = new PasswordEncryptor();
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(String username, String password) {
        var foundUserOptional = userRepository.findUserByUserName(username);

        var foundUser = foundUserOptional.orElseThrow(() -> {
            throw new AuthenticationException("Username doesn't exist!");
        });

        if (!encryptor.authenticate(password, foundUser.getPassword()))
            throw new AuthenticationException("Incorrect password!");
    }

    public void register(String username, String password) {
        if (userRepository.findUserByUserName(username).isPresent())
            throw new AuthenticationException("Username " + username + " is already used!");

        String hashPassword = encryptor.hash(password);
        User user = new User(username, hashPassword);
        userRepository.save(user);
    }
}
