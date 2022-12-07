package ru.practicum.explore.with.me.model.compilations.dto;

import ru.practicum.explore.with.me.model.compilations.Compilations;
import ru.practicum.explore.with.me.model.events.Event;

import java.util.stream.Collectors;

public class CompilationsMapper {

    public static Compilations toCompilations(CompilationsDtoShort compilationsDtoShort) {
        return Compilations.builder()
                .events(compilationsDtoShort.getEvents()
                        .stream()
                        .map(el -> Event.builder().id(el).build())
                        .collect(Collectors.toSet()))
                .pinned(compilationsDtoShort.getPinned())
                .title(compilationsDtoShort.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilations compilations) {
        return CompilationDto.builder()
                .id(compilations.getId())
                .events(compilations.getEvents()
                        .stream()
                        .map(CompilationsMapper::toEventShortDto)
                        .collect(Collectors.toSet()))
                .pinned(compilations.getPinned())
                .title(compilations.getTitle())
                .build();
    }

    private static CompilationDto.EventShortDto toEventShortDto(Event event) {
        return CompilationDto.EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CompilationDto.EventShortDto.CategoryDto.builder()
                        .id(event.getCategories().getId())
                        .name(event.getCategories().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(CompilationDto.EventShortDto.UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

}
