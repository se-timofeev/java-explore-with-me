package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.model.StatHitDto;
import ru.practicum.explore.with.me.model.StatViewDto;

import java.util.List;

public interface StatService {

    void addHit(StatHitDto statHitDto);

    List<StatViewDto> getStat(String start, String end, List<String> uris, Boolean unique);

}
