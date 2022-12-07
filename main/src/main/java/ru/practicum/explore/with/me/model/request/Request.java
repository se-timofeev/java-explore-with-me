package ru.practicum.explore.with.me.model.request;

import lombok.*;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ManyToOne
    private Event event;
    @ManyToOne
    private User requester;
    private Status status;

}
