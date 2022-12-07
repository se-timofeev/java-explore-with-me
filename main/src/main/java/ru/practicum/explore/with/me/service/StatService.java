package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.model.events.dto.EventFullDto;
import ru.practicum.explore.with.me.model.events.dto.EventDtoShort;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface StatService {
    void statsHit(HttpServletRequest request) throws URISyntaxException, IOException, InterruptedException;

    List<EventDtoShort> getViewStats(HttpServletRequest request, List<EventDtoShort> eventDtoShortList) throws URISyntaxException, IOException, InterruptedException;

    EventFullDto getViewStats(HttpServletRequest request, EventFullDto eventFullDto) throws URISyntaxException, IOException, InterruptedException;
}
