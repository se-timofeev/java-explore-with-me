package ru.practicum.explore.with.me.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class StatHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    @Column(length = 2048)
    private String uri;
    @Column(length = 45)
    private String ip;
    private LocalDateTime timestamp;

}
