package ru.practicum.explore.with.me.model.events;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.explore.with.me.model.categories.Categories;
import ru.practicum.explore.with.me.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation;
    @ManyToOne()
    @JoinColumn(name = "categories_id")
    private Categories categories;
    @Builder.Default
    private Long confirmedRequests = 0L;
    private LocalDateTime createdOn;
    @Column(length = 7000)
    private String description;
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;
    @Embedded
    private Location location;
    private Boolean paid;
    @Builder.Default
    private Integer participantLimit = 0;
    private LocalDateTime publishedOn;
    @Builder.Default
    private Boolean requestModeration = true;
    @Enumerated
    private State state;
    private String title;

    public void incrementConfirmedRequests() {
        confirmedRequests++;
    }

    public void decrementConfirmedRequests() {
        confirmedRequests--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
