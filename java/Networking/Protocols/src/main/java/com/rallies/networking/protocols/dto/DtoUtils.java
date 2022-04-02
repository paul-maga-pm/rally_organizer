package com.rallies.networking.protocols.dto;

import com.rallies.domain.models.Participant;
import com.rallies.domain.models.User;

public class DtoUtils {
    public static User getModelFromDto(UserDto dto) {
        return new User(dto.getUsername(), dto.getPassword());
    }

    public static UserDto getDtoFromModel(User user) {
        return new UserDto(user.getUserName(), user.getPassword());
    }
}
