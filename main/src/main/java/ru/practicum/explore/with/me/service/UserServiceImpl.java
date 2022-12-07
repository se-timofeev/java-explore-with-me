package ru.practicum.explore.with.me.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.exceptions.ConflictException;
import ru.practicum.explore.with.me.exceptions.NotFoundException;
import ru.practicum.explore.with.me.model.user.dto.UsersDto;
import ru.practicum.explore.with.me.model.user.dto.UsersMapper;
import ru.practicum.explore.with.me.model.user.User;
import ru.practicum.explore.with.me.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    @Transactional
    public UsersDto add(UsersDto usersDto) {
        try {
            User user = userRepository.save(UsersMapper.toUser(usersDto));
            return UsersMapper.toUsersDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public UsersDto edit(UsersDto usersDto) {
        User user = userRepository.findById(usersDto.getId())
                .orElseThrow(() -> new NotFoundException("user not found"));
        if (usersDto.getName() != null && !usersDto.getName().isEmpty()) {
            user.setName(usersDto.getName());
        }
        if (usersDto.getEmail() != null && !usersDto.getEmail().isEmpty()) {
            user.setEmail(usersDto.getEmail());
        }
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
        return UsersMapper.toUsersDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UsersDto> search(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        return users.stream()
                .map(UsersMapper::toUsersDto)
                .collect(Collectors.toList());
    }

}
