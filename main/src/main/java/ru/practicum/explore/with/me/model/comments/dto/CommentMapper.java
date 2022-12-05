package ru.practicum.explore.with.me.model.comments.dto;

import ru.practicum.explore.with.me.model.comments.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .dateCreate(comment.getDateCreate())
                .dateEdit(comment.getDateEdit())
                .text(comment.getText())
                .edited(comment.getEdited())
                .state(comment.getState())
                .build();
    }

}