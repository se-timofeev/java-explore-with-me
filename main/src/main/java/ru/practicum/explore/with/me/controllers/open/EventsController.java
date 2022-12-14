package ru.practicum.explore.with.me.controllers.open;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.service.EventService;
import ru.practicum.explore.with.me.model.events.dto.EventFullDto;
import ru.practicum.explore.with.me.model.events.dto.EventDtoShort;
import ru.practicum.explore.with.me.service.StatService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/events")
public class EventsController {

    private EventService eventService;
    private StatService statsService;


    @GetMapping
    public List<EventDtoShort> getAll(
            HttpServletRequest request,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, defaultValue = "") String rangeStart,
            @RequestParam(required = false, defaultValue = "") String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "ID") String sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size)
            throws URISyntaxException, IOException, InterruptedException {

        List<EventDtoShort> eventDtoShortList = eventService.getAll(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);
        return statsService.getViewStats(request, eventDtoShortList);

    }

    @GetMapping("/{id}")
    public EventFullDto getOne(
            HttpServletRequest request, @PathVariable Long id) throws URISyntaxException, IOException, InterruptedException {
        EventFullDto eventFullDto = eventService.getOne(id);
        return statsService.getViewStats(request, eventFullDto);
    }
}
