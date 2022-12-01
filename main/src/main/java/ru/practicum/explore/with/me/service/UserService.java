package ru.practicum.explore.with.me.service;


import ru.practicum.explore.with.me.model.user.dto.UsersDto;

import java.util.List;

public interface UserService {

    UsersDto add(UsersDto usersDto);

    UsersDto edit(UsersDto usersDto);

    void delete(Long id);

    List<UsersDto> search(List<Long> ids);

}
