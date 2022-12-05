package ru.practicum.explore.with.me.model.comments.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEditorDto {

    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 2000)
    private String text;

}
