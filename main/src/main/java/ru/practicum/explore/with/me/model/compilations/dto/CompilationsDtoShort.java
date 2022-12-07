package ru.practicum.explore.with.me.model.compilations.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationsDtoShort {

    private Set<Long> events;
    @Builder.Default
    private Boolean pinned = false;
    @NotBlank
    private String title;

}
