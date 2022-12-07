package ru.practicum.explore.with.me.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.service.CompilationsService;
import ru.practicum.explore.with.me.model.compilations.dto.CompilationDto;
import ru.practicum.explore.with.me.model.compilations.dto.CompilationsDtoShort;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationsController {

    private CompilationsService compilationsService;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody CompilationsDtoShort compilationsDtoShort) {
        log.info(" AdminCompilationsController add {}", compilationsDtoShort);
        return compilationsService.add(compilationsDtoShort);
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable Long compId) {
        log.info(" AdminCompilationsController delete {}", compId);
        compilationsService.delete(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info(" AdminCompilationsController deleteEvent {}", compId);
        compilationsService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info(" AdminCompilationsController addEvent {}", compId);
        compilationsService.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deletePin(@PathVariable Long compId) {
        log.info(" AdminCompilationsController deletePin {}", compId);
        compilationsService.deletePin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pin(@PathVariable Long compId) {
        log.info(" AdminCompilationsController pin {}", compId);
        compilationsService.pin(compId);
    }

}
