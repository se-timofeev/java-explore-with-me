package ru.practicum.explore.with.me.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.model.events.State;
import ru.practicum.explore.with.me.repository.EventRepository;
import ru.practicum.explore.with.me.exceptions.NotFoundException;
import ru.practicum.explore.with.me.exceptions.ValidationException;
import ru.practicum.explore.with.me.model.request.dto.RequestDto;
import ru.practicum.explore.with.me.model.request.dto.RequestMapper;
import ru.practicum.explore.with.me.model.request.Request;
import ru.practicum.explore.with.me.model.request.Status;
import ru.practicum.explore.with.me.repository.RequestsRepository;
import ru.practicum.explore.with.me.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestsServiceImpl implements RequestsService {

    private RequestsRepository requestsRepository;
    private EventRepository eventRepository;

    @Override
    public List<RequestDto> getAll(Long userId) {
        List<Request> requestList = requestsRepository.findAllByRequester(User.builder().id(userId).build());
        return requestList.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAll(Long userId, Long eventId) {
        List<Event> events = eventRepository.findAllByInitiator(User.builder().id(userId).build());
        List<Request> requestList = requestsRepository.findAll((root, query, criteriaBuilder) ->
                root.get("event").in(events.stream().map(Event::getId).collect(Collectors.toList())));
        return requestList.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto confirm(Long userId, Long eventId, Long reqId) {
        Event event = eventOne(eventId, userId, true);
        Long requestsCount = requestsCount(event);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requestsCount) {
            throw new ValidationException("events are succeeded the limit", event.getParticipantLimit().toString());
        }
        Request request = requestOne(reqId, event);
        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            return RequestMapper.toRequestDto(request);
        }
        request.setStatus(Status.CONFIRMED);
        RequestDto requestDto = RequestMapper.toRequestDto(requestsRepository.save(request));
        event.incrementConfirmedRequests();
        eventRepository.save(event);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == requestsCount + 1) {
            List<Request> requestList = requestsRepository.findAll(((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("event"), event.getId()),
                            criteriaBuilder.equal(root.get("status"), Status.PENDING.ordinal())
                    )));
            requestList.forEach(el -> el.setStatus(Status.REJECTED));
            requestsRepository.saveAll(requestList);
        }
        return requestDto;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto reject(Long userId, Long eventId, Long reqId) {
        Event event = eventOne(eventId, userId, true);
        Request request = requestOne(reqId, event);
        if (request.getStatus().equals(Status.CONFIRMED)) {
            event.decrementConfirmedRequests();
            eventRepository.save(event);
        }
        request.setStatus(Status.REJECTED);
        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto add(Long userId, Long eventId) {
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(Event.builder().id(eventId).build())
                .requester(User.builder().id(userId).build())
                .status(Status.PENDING)
                .build();
        Event event = eventOne(eventId, userId, false);
        Long requestsCount = requestsCount(event);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requestsCount) {
            throw new ValidationException("Events have exceeded the limit", event.getParticipantLimit().toString());
        }
        requestsRepository.findByRequesterAndEvent(
                        User.builder().id(userId).build(),
                        Event.builder().id(eventId).build())
                .ifPresent(rq -> {
                    throw new ValidationException("repeated request", "");
                });
        if (!event.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
            event.incrementConfirmedRequests();
            eventRepository.save(event);
        }
        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto cancel(Long userId, Long requestId) {
        Request request = requestsRepository.findByIdAndRequester(requestId, User.builder().id(userId).build())
                .orElseThrow(() -> new NotFoundException("request not found"));
        Event event = eventRepository.findOne(((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), request.getEvent().getId()),
                        criteriaBuilder.equal(root.get("state"), State.PUBLISHED.ordinal())
                ))).orElseThrow(() -> new NotFoundException("event not found"));
        if (request.getStatus().equals(Status.CONFIRMED)) {
            event.decrementConfirmedRequests();
            eventRepository.save(event);
        }
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    private Event eventOne(Long eventId, Long userId, Boolean initiator) {
        return eventRepository.findOne(((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), eventId),
                        (initiator) ? criteriaBuilder.equal(root.get("initiator"), userId) : criteriaBuilder.notEqual(root.get("initiator"), userId),
                        criteriaBuilder.equal(root.get("state"), State.PUBLISHED.ordinal())
                ))).orElseThrow(() -> new NotFoundException("event not found"));
    }

    private Request requestOne(Long reqId, Event event) {
        return requestsRepository.findOne((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), reqId),
                        criteriaBuilder.equal(root.get("event"), event.getId())
                )).orElseThrow(() -> new NotFoundException("request not found"));
    }

    private Long requestsCount(Event event) {
        return requestsRepository.count((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("event"), event.getId()),
                        criteriaBuilder.equal(root.get("status"), Status.CONFIRMED.ordinal())
                ));
    }

}
