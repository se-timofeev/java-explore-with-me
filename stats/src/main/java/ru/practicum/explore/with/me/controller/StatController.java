package ru.practicum.explore.with.me.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.model.StatHitDto;
import ru.practicum.explore.with.me.model.StatViewDto;
import ru.practicum.explore.with.me.service.StatsService;


import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class StatController {

    private StatsService statsService;

    @PostMapping("/hit")
    public void addHit(@Valid @RequestBody StatHitDto statHitDto) {
        log.info("addHit= {}", statHitDto);
        statsService.addHit(statHitDto);
    }
    @PostMapping("/hits")
    public void addHits(@Valid @RequestBody List<StatHitDto> hitsDto) {
        log.info("addHits List<> = {}", hitsDto);
        statsService.addHits(hitsDto);
    }

    @GetMapping("/stats")
    public List<StatViewDto> getViewStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean isUnique) {
        log.info("getViewStats start {}, end, uris {},isUnique{}", start, end, uris, isUnique);
        return statsService.getStat(start, end, uris, isUnique);
    }

}
