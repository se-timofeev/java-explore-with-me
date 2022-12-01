package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.model.compilations.dto.CompilationDto;
import ru.practicum.explore.with.me.model.compilations.dto.CompilationsDtoShort;

import java.util.List;

public interface CompilationsService {
    CompilationDto add(CompilationsDtoShort compilationsDtoShort);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getOne(Long compId);

    void delete(Long compId);

    void deleteEvent(Long compId, Long eventId);

    void addEvent(Long compId, Long eventId);

    void deletePin(Long compId);

    void pin(Long compId);
}
