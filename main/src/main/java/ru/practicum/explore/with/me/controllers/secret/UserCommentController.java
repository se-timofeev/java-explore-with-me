package ru.practicum.explore.with.me.controllers.secret;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.model.comments.dto.CommentCreatingDto;
import ru.practicum.explore.with.me.model.comments.dto.CommentDto;
import ru.practicum.explore.with.me.model.comments.dto.CommentEditorDto;
import ru.practicum.explore.with.me.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
@Validated
@Slf4j
public class UserCommentController {

    private CommentService commentsService;

    @GetMapping
    public List<CommentDto> getAll(@PathVariable Long userId,
                                   @RequestParam(required = false, defaultValue = "0") Integer from,
                                   @RequestParam(required = false, defaultValue = "10") @PositiveOrZero Integer size,
                                   @RequestParam(required = false, defaultValue = "ALL") @Positive String state) {
        log.info("UserCommentController getAll - {}, from - {}, size - {}, state - {}", userId, from, size, state);
        return commentsService.getAll(userId, null, from, size, state);
    }

    @PostMapping
    public CommentDto create(@PathVariable Long userId, @Valid @RequestBody CommentCreatingDto commentCreatingDto) {
        log.info("UserCommentController  create {}, {} ", userId, commentCreatingDto);
        return commentsService.create(userId, commentCreatingDto);
    }

    @PutMapping
    public CommentDto edit(@PathVariable Long userId, @Valid @RequestBody CommentEditorDto commentEditorDto) {
        log.info("UserCommentController  edit {}, {} ", userId, commentEditorDto);
        return commentsService.edit(userId, commentEditorDto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("UserCommentController  delete {}, {} ", userId, commentId);
        commentsService.delete(userId, commentId);
    }

    @GetMapping("/{eventId}")
    public List<CommentDto> getAllCommentEvent(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {

        log.info("UserCommentController  getAllCommentEvent ");
        return commentsService.getAllOwner(userId, eventId, from, size);
    }

}