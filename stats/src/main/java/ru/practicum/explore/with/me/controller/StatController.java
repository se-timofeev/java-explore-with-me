package ru.practicum.explore.with.me.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.model.StatHitDto;
import ru.practicum.explore.with.me.model.StatViewDto;
import ru.practicum.explore.with.me.service.StatService;


import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class StatController {

    private StatService statService;

    @PostMapping("/hit")
    public void addHit(@Valid @RequestBody StatHitDto statHitDto) {
        log.info("addHit= {}", statHitDto);
        statService.addHit(statHitDto);
    }

    @GetMapping("/stats")
    public List<StatViewDto> getViewStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean isUnique) {
        log.info("getViewStats start {}, end, uris {},isUnique{{", start, end, uris, isUnique);
        return statService.getStat(start, end, uris, isUnique);
    }

}
