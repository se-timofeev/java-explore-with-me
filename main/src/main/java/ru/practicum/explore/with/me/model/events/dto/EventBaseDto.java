package ru.practicum.explore.with.me.model.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class EventBaseDto {

    @Size(min = 20)
    @Size(max = 2000)
    @NotBlank
    protected String annotation;
    @NotNull
    protected Long category;
    @Size(min = 20)
    @Size(max = 7000)
    @NotBlank
    protected String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;
    @Builder.Default
    protected Boolean paid = false;
    @Builder.Default
    protected Integer participantLimit = 0;
    @Size(min = 3)
    @Size(max = 120)
    protected String title;

}
