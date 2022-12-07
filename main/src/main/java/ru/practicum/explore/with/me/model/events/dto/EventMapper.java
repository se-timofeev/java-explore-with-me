package ru.practicum.explore.with.me.model.events.dto;

import ru.practicum.explore.with.me.model.categories.Categories;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.model.events.Location;
import ru.practicum.explore.with.me.model.events.State;
import ru.practicum.explore.with.me.model.user.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEvent(Long userId, NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .categories(Categories.builder().id(newEventDto.getCategory()).build())
                .initiator(User.builder().id(userId).build())
                .createdOn(LocalDateTime.now())
                .state(State.PENDING)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(Location.builder()
                        .lat(newEventDto.getLocation().getLat())
                        .lon(newEventDto.getLocation().getLon())
                        .build())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(EventFullDto.CategoryDto.builder()
                        .id(event.getCategories().getId())
                        .name(event.getCategories().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .description(event.getDescription())
                .initiator(EventFullDto.UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .location(EventFullDto.Location.builder()
                        .lat(event.getLocation().getLat())
                        .lon(event.getLocation().getLon())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static EventDtoShort toEventShortDto(Event event) {
        return EventDtoShort.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(EventDtoShort.CategoryDto.builder()
                        .id(event.getCategories().getId())
                        .name(event.getCategories().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(EventDtoShort.UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }
}
