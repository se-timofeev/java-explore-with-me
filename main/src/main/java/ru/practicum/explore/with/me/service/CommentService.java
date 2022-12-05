package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.model.comments.dto.CommentCreatingDto;
import ru.practicum.explore.with.me.model.comments.dto.CommentDto;
import ru.practicum.explore.with.me.model.comments.dto.CommentEditorDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAll(Long userId, Long eventId, Integer from, Integer size, String state);

    List<CommentDto> getAllOwner(Long userId, Long eventId, Integer from, Integer size);

    CommentDto approve(Long commentId);

    CommentDto hide(Long commentId);

    void deleteForAdmin(Long commentId);

    CommentDto edit(Long userId, CommentEditorDto commentsEditDto);

    void delete(Long userId, Long commentId);

    CommentDto create(Long userId, CommentCreatingDto commentsCreateDto);
}
