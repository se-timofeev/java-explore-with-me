package ru.practicum.explore.with.me.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.model.comments.dto.CommentDto;
import ru.practicum.explore.with.me.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/comments")
@Validated
@Slf4j
public class AdminCommentsController {
    private CommentService commentService;

    @GetMapping
    public List<CommentDto> getAll(@RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) Long eventId,
                                   @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                   @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                   @RequestParam(required = false) String state) {
        log.info("getAll, userId - {}, eventId - {}, from - {}, size - {}, state - {}", userId, eventId, from, size, state);
        return commentService.getAll(userId, eventId, from, size, state);
    }

    @PatchMapping("/{commentId}/approve")
    public CommentDto approve(@PathVariable Long commentId) {
        log.info("approve  {}", commentId);
        return commentService.approve(commentId);
    }

    @PatchMapping("/{commentId}/hidden")
    public CommentDto hide(@PathVariable Long commentId) {
        log.info("hide {}", commentId);
        return commentService.hide(commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteAdmin(@PathVariable Long commentId) {
        log.info("deleteAdmin {}", commentId);
        commentService.deleteForAdmin(commentId);
    }
}
