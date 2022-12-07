package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.model.request.dto.RequestDto;

import java.util.List;

public interface RequestsService {
    List<RequestDto> getAll(Long userId, Long eventId);

    RequestDto confirm(Long userId, Long eventId, Long reqId);

    RequestDto reject(Long userId, Long eventId, Long reqId);

    List<RequestDto> getAll(Long userId);

    RequestDto add(Long userId, Long eventId);

    RequestDto cancel(Long userId, Long requestId);
}
