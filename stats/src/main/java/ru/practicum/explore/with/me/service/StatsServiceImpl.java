package ru.practicum.explore.with.me.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.model.StatHitDto;
import ru.practicum.explore.with.me.model.Mapper;
import ru.practicum.explore.with.me.model.StatViewDto;
import ru.practicum.explore.with.me.model.StatHit;
import ru.practicum.explore.with.me.repository.StatRepository;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private StatRepository statRepository;

    @Override
    public List<StatViewDto> getStat(String start, String end, List<String> uris, Boolean isUnique) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (isUnique) {
            return statRepository.countTotalIpDistinct(startDate, endDate, uris);
        } else {
            return statRepository.countTotalIp(startDate, endDate, uris);
        }
    }

    @Override
    @Transactional
    public void addHit(StatHitDto statHitDto) {
        try {
            StatHit statHit = Mapper.toStatsHit(statHitDto);
            statRepository.save(statHit);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



}
