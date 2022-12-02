package ru.practicum.explore.with.me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.model.categories.Categories;
import ru.practicum.explore.with.me.model.events.dto.*;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.model.events.EventSort;
import ru.practicum.explore.with.me.model.events.Location;
import ru.practicum.explore.with.me.model.events.State;
import ru.practicum.explore.with.me.repository.EventRepository;
import ru.practicum.explore.with.me.exceptions.NotFoundException;
import ru.practicum.explore.with.me.exceptions.ValidationException;
import ru.practicum.explore.with.me.model.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventFullDto getOne(Long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("event not found"));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getAll(List<Long> users,
                                     List<String> states,
                                     List<Long> categories,
                                     String rangeStart,
                                     String rangeEnd,
                                     Integer from,
                                     Integer size) {
        int page = from / size;
        LocalDateTime currentDate = LocalDateTime.now();
        Page<Event> events = eventRepository.findAll(
                (root,
                 query,
                 criteriaBuilder) -> criteriaBuilder
                        .and((users != null) ? root.get("initiator").in(users) : root.isNotNull(),
                                (states != null) ? root.get("state").in(states.stream().map(
                                        el -> State.valueOf(el).ordinal()).collect(Collectors.toList())) : root.isNotNull(),
                                (categories != null) ? root.get("categories").in(categories) : root.isNotNull(),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("eventDate"),
                                                        LocalDateTime.parse(rangeStart,
                                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                criteriaBuilder.lessThan(root.get("eventDate"),
                                                        LocalDateTime.parse(rangeEnd,
                                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        ) : criteriaBuilder.lessThan(root.get("eventDate"), currentDate)),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto publish(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found"));
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto reject(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found"));
        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<EventDtoShort> getAll(String text,
                                      List<Long> categories,
                                      Boolean paid,
                                      String rangeStart,
                                      String rangeEnd,
                                      Boolean onlyAvailable,
                                      String sort,
                                      Integer from,
                                      Integer size) {
        int page = from / size;
        LocalDateTime currentDate = LocalDateTime.now();
        EventSort eventSort;
        try {
            eventSort = EventSort.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("wrong sorting", sort);
        }
        Sort mySort;
        switch (eventSort) {
            case EVENT_DATE:
                mySort = Sort.by(Sort.Direction.ASC, "eventDate");
                break;
            case VIEWS:
                mySort = Sort.by(Sort.Direction.ASC, "views");
                break;
            default:
                mySort = Sort.by(Sort.Direction.ASC, "id");
        }
        Page<Event> events = eventRepository.findAll((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("state"), State.PUBLISHED.ordinal()),
                                (categories != null) ? root.get("categories").in(categories) : root.isNotNull(),
                                (paid != null) ? criteriaBuilder.equal(root.get("paid"), paid) : root.isNotNull(),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        criteriaBuilder.and(
                                                criteriaBuilder.greaterThan(root.get("eventDate"),
                                                        LocalDateTime.parse(rangeStart,
                                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                criteriaBuilder.lessThan(root.get("eventDate"),
                                                        LocalDateTime.parse(rangeEnd,
                                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        ) : criteriaBuilder.lessThan(root.get("eventDate"), currentDate),
                                (onlyAvailable) ? criteriaBuilder.or(
                                        criteriaBuilder.equal(root.get("participantLimit"), 0),
                                        criteriaBuilder.and(
                                                criteriaBuilder.notEqual(root.get("participantLimit"), 0),
                                                criteriaBuilder.greaterThan(root.get("participantLimit"),
                                                        root.get("confirmedRequests"))
                                        )) : root.isNotNull(),
                                (text != null) ? criteriaBuilder.or(
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                                                "%" + text.toLowerCase() + "%"),
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                                                "%" + text.toLowerCase() + "%")
                                ) : root.isNotNull()),
                PageRequest.of(page, size, mySort));
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoShort> getAll(Long userId, Integer from, Integer size) {
        int page = from / size;
        return eventRepository.findAll(
                        (root,
                         query,
                         criteriaBuilder) -> criteriaBuilder.equal(root.get("initiator"),
                                User.builder().id(userId).build()),
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto edit(Long userId, EditEventDto editEventDto) {
        if (LocalDateTime.now().plusHours(2).isAfter(editEventDto.getEventDate())) {
            throw new ValidationException("date error", editEventDto.getEventDate().toString());
        }
        Event event = eventRepository.findById(editEventDto.getEventId())
                .orElseThrow(() -> new NotFoundException("event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("access denied " + event.getInitiator(), userId.toString());
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("error, events had been published", event.getState().toString());
        }
        if (event.getState().equals(State.CANCELED)) {
            event.setState(State.PENDING);
        }
        verifyChange(event, editEventDto);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto edit(Long eventId, NewEventDto newEventDto) {
        if (LocalDateTime.now().plusHours(2).isAfter(newEventDto.getEventDate())) {
            throw new ValidationException("date error", newEventDto.getEventDate().toString());
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found"));
        verifyChange(event, newEventDto);
        if (newEventDto.getLocation() != null) {
            Location location = Location.builder()
                    .lon(newEventDto.getLocation().getLon())
                    .lat(newEventDto.getLocation().getLat())
                    .build();
            event.setLocation(location);
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getOne(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator(eventId, User.builder().id(userId).build())
                .orElseThrow(() -> new NotFoundException("event not found"));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator(eventId, User.builder().id(userId).build())
                .orElseThrow(() -> new NotFoundException("event not found"));
        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        if (LocalDateTime.now().plusHours(2).isAfter(newEventDto.getEventDate())) {
            throw new ValidationException("date error", newEventDto.getEventDate().toString());
        }
        Event event = EventMapper.toEvent(userId, newEventDto);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    private <T extends EventBaseDto> void verifyChange(Event event, T dto) {
        if (dto.getAnnotation() != null && !dto.getAnnotation().isEmpty()) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            event.setCategories(Categories.builder().id(dto.getCategory()).build());
        }
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            event.setTitle(dto.getTitle());
        }
    }

}
