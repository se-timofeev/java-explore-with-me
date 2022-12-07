package ru.practicum.explore.with.me.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.model.events.dto.EventDtoShort;
import ru.practicum.explore.with.me.model.events.dto.EventFullDto;
import ru.practicum.explore.with.me.model.stat.StatsHitDto;
import ru.practicum.explore.with.me.model.stat.StatsViewDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(StatServiceImpl.class);
    private final ObjectMapper objectMapper;
    @Value("${stats-server.url}")
    private String serverUrl;

    public StatServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void statsHit(HttpServletRequest request) throws URISyntaxException, IOException, InterruptedException {
        StatsHitDto statsHitDto = StatsHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .ip(request.getRemoteAddr())
                .build();
        sendStats(statsHitDto);
    }

    @Override
    public List<EventDtoShort> getViewStats(
            HttpServletRequest request,
            List<EventDtoShort> eventDtoShortList) throws URISyntaxException, IOException, InterruptedException {

        List<String> uris = eventDtoShortList.stream()
                .map(el -> "/events/" + el.getId().toString())
                .collect(Collectors.toList());
        List<StatsViewDto> statsViewDtoList = getStats(uris);

        Map<String, StatsViewDto> statsViewDtoMap = statsViewDtoList.stream()
                .collect(Collectors.toMap(StatsViewDto::getUri, v -> v));

        eventDtoShortList.forEach(el -> {
            String key = "/events/" + el.getId().toString();
            StatsViewDto stats = new StatsViewDto();
            Long hits = statsViewDtoMap.getOrDefault(key, stats).getHits();
            el.setViews(hits);
        });

        StringBuilder query = new StringBuilder();
        query.append("/events");
        if (request.getQueryString() != null) {
            query.append(request.getQueryString());
        }
        uris.add(query.toString());

        statsHit(request, uris);
        return eventDtoShortList;
    }

    @Override
    public EventFullDto getViewStats(HttpServletRequest request,
                                     EventFullDto eventFullDto) throws URISyntaxException, IOException, InterruptedException {
        List<String> uris = List.of("/events/" + eventFullDto.getId().toString());
        List<StatsViewDto> statsViewDto = getStats(uris);
        Long view = (statsViewDto.size() != 0) ? statsViewDto.get(0).getHits() : 0L;
        eventFullDto.setViews(view);
        statsHit(request, uris);
        return eventFullDto;
    }

    private void statsHit(HttpServletRequest request,
                          List<String> uris) throws IOException, URISyntaxException, InterruptedException {
        List<StatsHitDto> statsHitDtoList = uris.stream()
                .map(uri -> StatsHitDto.builder()
                        .app("ewm-main-service")
                        .uri(uri)
                        .timestamp(LocalDateTime.now())
                        .ip(request.getRemoteAddr())
                        .build())
                .collect(Collectors.toList());

        // для уменьшения накладных расходов на вызов в случае передачи большого списка
        if (statsHitDtoList.size() == 1) {
            sendStats(statsHitDtoList.get(0));
        } else {
            sendStatsList(statsHitDtoList);
        }

    }

    private void sendStats(StatsHitDto statsHitDto) throws IOException, URISyntaxException, InterruptedException {
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(statsHitDto);

        HttpRequest statRequest = HttpRequest.newBuilder(new URI(serverUrl + "/hit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient.newHttpClient()
                .send(statRequest, HttpResponse.BodyHandlers.ofString());

    }

    private void sendStatsList(List<StatsHitDto> statsHitDtoList) throws IOException, URISyntaxException, InterruptedException {
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(statsHitDtoList);

        HttpRequest statRequest = HttpRequest.newBuilder(new URI(serverUrl + "/hits"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient.newHttpClient()
                .send(statRequest, HttpResponse.BodyHandlers.ofString());

    }

    private List<StatsViewDto> getStats(List<String> uris) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URIBuilder(serverUrl + "/stats")
                .addParameter("start", LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addParameter(
                        "end", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addParameter(
                        "uris", listToString(uris))
                .build();

        HttpRequest statRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(statRequest, HttpResponse.BodyHandlers.ofString());
        log.warn("response.body() - {}", response.body());
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    private String listToString(List<String> uris) {
        StringBuilder sb = new StringBuilder();
        var endSize = uris.size() - 1;
        for (int i = 0; i < uris.size(); i++) {
            sb.append(uris.get(i));
            if (i != endSize) sb.append(",");
        }
        return sb.toString();
    }
}