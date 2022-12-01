package ru.practicum.explore.with.me.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatViewDto {

    private String app;
    private String uri;
    private Long hits = 0L;

}
