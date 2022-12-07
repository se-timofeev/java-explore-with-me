package ru.practicum.explore.with.me.model.user.dto;


import ru.practicum.explore.with.me.model.user.User;

public class UsersMapper {

    public static UsersDto toUsersDto(User user) {
        return UsersDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UsersDto usersDto) {
        return User.builder()
                .id(usersDto.getId())
                .name(usersDto.getName())
                .email(usersDto.getEmail())
                .build();
    }

}
