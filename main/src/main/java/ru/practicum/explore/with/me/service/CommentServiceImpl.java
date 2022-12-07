package ru.practicum.explore.with.me.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.exceptions.NotFoundException;
import ru.practicum.explore.with.me.exceptions.ValidationException;
import ru.practicum.explore.with.me.model.comments.Comment;
import ru.practicum.explore.with.me.model.comments.State;
import ru.practicum.explore.with.me.model.comments.dto.CommentCreatingDto;
import ru.practicum.explore.with.me.model.comments.dto.CommentDto;
import ru.practicum.explore.with.me.model.comments.dto.CommentEditorDto;
import ru.practicum.explore.with.me.model.comments.dto.CommentMapper;
import ru.practicum.explore.with.me.model.events.Event;
import ru.practicum.explore.with.me.model.request.Status;
import ru.practicum.explore.with.me.model.user.User;
import ru.practicum.explore.with.me.repository.CommentRepository;
import ru.practicum.explore.with.me.repository.EventRepository;
import ru.practicum.explore.with.me.repository.RequestsRepository;
import ru.practicum.explore.with.me.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private EventRepository eventRepository;
    private RequestsRepository requestsRepository;
    private UserRepository userRepository;

    @Override
    public List<CommentDto> getAll(Long userId, Long eventId, Integer from, Integer size, String state) {
        int page = from / size;
        Page<Comment> commentList = commentRepository.findAll((root, query, cb) ->
                        cb.and(
                                (userId != null) ? cb.equal(root.get("author").get("id"), userId) : root.isNotNull(),
                                (eventId != null) ? cb.equal(root.get("event").get("id"), eventId) : root.isNotNull(),
                                (state != null) ? cb.equal(root.get("state"), State.valueOf(state)) : cb.equal(root.get("state"), State.APPROVED)
                        ),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreate")));
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllOwner(Long userId, Long eventId, Integer from, Integer size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Access denied", userId.toString());
        }
        int page = from / size;
        Page<Comment> commentList = commentRepository.findAll((root, query, cb) ->
                        cb.and(
                                cb.equal(root.get("event").get("id"), eventId),
                                root.get("state").in(State.APPROVED, State.HIDDEN)
                        ),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreate")));
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto approve(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        comment.setState(State.APPROVED);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto hide(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        comment.setState(State.HIDDEN);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteForAdmin(Long commentId) {
        commentRepository.deleteById(commentId);

    }

    @Override
    public CommentDto edit(Long userId, CommentEditorDto commentsEditDto) {
        Comment comment = commentRepository.findById(commentsEditDto.getId())
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        comment.setState(State.EDITED);
        comment.setEdited(true);
        comment.setText(commentsEditDto.getText());
        comment.setDateEdit(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long userId, Long commentId) {
        commentRepository.deleteByAuthorAndId(User.builder().id(userId).build(), commentId);
    }

    @Override
    public CommentDto create(Long userId, CommentCreatingDto commentsCreateDto) {
        User author = userRepository.findById(commentsCreateDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(commentsCreateDto.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));
        Comment comment = Comment.builder()
                .author(author)
                .event(event)
                .dateCreate(LocalDateTime.now())
                .text(commentsCreateDto.getText())
                .state(State.CREATED)
                .build();
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            requestsRepository.findOne((root, query, cb) ->
                            cb.and(
                                    cb.equal(root.get("event"), event),
                                    cb.equal(root.get("requester"), author),
                                    cb.equal(root.get("status"), Status.CONFIRMED)
                            ))
                    .orElseThrow(() -> new NotFoundException("Access denied"));
        }
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}
