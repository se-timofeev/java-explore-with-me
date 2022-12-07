package ru.practicum.explore.with.me.model;

import java.net.URISyntaxException;

public class Mapper {

    public static StatHit toStatsHit(StatHitDto statHitDto) throws URISyntaxException {
        return StatHit.builder()
                .app(statHitDto.getApp())
                .uri(statHitDto.getUri())
                .timestamp(statHitDto.getTimestamp())
                .ip(statHitDto.getIp())
                .build();
    }
}
