package ru.practicum.explore.with.me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.model.compilations.dto.CompilationDto;
import ru.practicum.explore.with.me.model.compilations.dto.CompilationsMapper;
import ru.practicum.explore.with.me.model.compilations.dto.CompilationsDtoShort;
import ru.practicum.explore.with.me.model.compilations.Compilations;
import ru.practicum.explore.with.me.repository.CompilationRepository;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.exceptions.NotFoundException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CompilationsServiceImpl implements CompilationsService {

    private CompilationRepository compilationRepository;
    private EntityManager entityManager;

    @Transactional
    @Override
    public CompilationDto add(CompilationsDtoShort compilationsDtoShort) {
        Compilations compilations = compilationRepository.saveAndFlush(CompilationsMapper.toCompilations(compilationsDtoShort));
        entityManager.refresh(compilations);
        return CompilationsMapper.toCompilationDto(compilations);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        int page = from / size;
        return compilationRepository.findAll(
                        (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pinned"), pinned),
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(CompilationsMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CompilationDto getOne(Long compId) {
        Compilations compilations = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("compilations not found"));
        log.info("compilations - {}", compilations.getEvents());
        return CompilationsMapper.toCompilationDto(compilations);
    }

    @Override
    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEvent(Long compId, Long eventId) {
        Compilations compilations = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("compilations not found"));
        Set<Event> events = compilations.getEvents();
        events.remove(Event.builder().id(eventId).build());
        compilationRepository.save(compilations);
    }

    @Override
    public void addEvent(Long compId, Long eventId) {
        Compilations compilations = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("compilations not found"));
        Set<Event> events = compilations.getEvents();
        events.add(Event.builder().id(eventId).build());
        compilationRepository.save(compilations);
    }

    @Override
    public void deletePin(Long compId) {
        Compilations compilations = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("compilations not found"));
        compilations.setPinned(false);
        compilationRepository.save(compilations);
    }

    @Override
    public void pin(Long compId) {
        Compilations compilations = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("compilations not found"));
        compilations.setPinned(true);
        compilationRepository.save(compilations);
    }


}
