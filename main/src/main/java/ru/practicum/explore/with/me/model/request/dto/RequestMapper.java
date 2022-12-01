package ru.practicum.explore.with.me.model.request.dto;

import ru.practicum.explore.with.me.model.request.Request;


public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

}
