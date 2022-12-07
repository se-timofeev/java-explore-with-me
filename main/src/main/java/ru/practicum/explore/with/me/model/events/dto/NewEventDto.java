package ru.practicum.explore.with.me.model.events.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NewEventDto extends EventBaseDto {

    @NotNull
    private Location location;
    @Builder.Default
    private Boolean requestModeration = true;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private Float lat;
        private Float lon;
    }

}
