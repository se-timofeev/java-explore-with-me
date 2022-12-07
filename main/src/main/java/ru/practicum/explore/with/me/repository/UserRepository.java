package ru.practicum.explore.with.me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
