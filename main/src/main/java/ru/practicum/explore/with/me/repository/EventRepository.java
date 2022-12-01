package ru.practicum.explore.with.me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.model.events.State;
import ru.practicum.explore.with.me.model.user.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Optional<Event> findByIdAndInitiator(Long eventId, User user);

    Optional<Event> findByIdAndState(Long eventId, State state);

    List<Event> findAllByInitiator(User user);
}
