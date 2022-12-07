package ru.practicum.explore.with.me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.model.request.Request;
import ru.practicum.explore.with.me.model.user.User;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {

    List<Request> findAllByRequester(User user);

    Optional<Request> findByIdAndRequester(Long id, User user);

    Optional<Request> findByRequesterAndEvent(User user, Event event);
}
