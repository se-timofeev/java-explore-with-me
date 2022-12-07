package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.model.events.dto.EditEventDto;
import ru.practicum.explore.with.me.model.events.dto.EventFullDto;
import ru.practicum.explore.with.me.model.events.dto.EventDtoShort;
import ru.practicum.explore.with.me.model.events.dto.NewEventDto;

import java.util.List;

public interface EventService {
    EventFullDto getOne(Long eventId);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);

    List<EventDtoShort> getAll(String text,
                               List<Long> categories,
                               Boolean paid,
                               String rangeStart,
                               String rangeEnd,
                               Boolean onlyAvailable,
                               String sort,
                               Integer from,
                               Integer size);

    List<EventDtoShort> getAll(Long userId,
                               Integer from,
                               Integer size);

    EventFullDto edit(Long userId, EditEventDto editEventDto);

    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto edit(Long eventId, NewEventDto newEventDto);

    EventFullDto getOne(Long userId, Long eventId);

    EventFullDto cancelUserEvent(Long userId, Long eventId);

    List<EventFullDto> getAll(List<Long> users,
                               List<String> states,
                               List<Long> categories,
                               String rangeStart,
                               String rangeEnd,
                               Integer from,
                               Integer size);
}
