package ru.practicum.explore.with.me.repository;

import org.springframework.data.jpa.repository.*;
import ru.practicum.explore.with.me.model.comments.Comment;
import ru.practicum.explore.with.me.model.user.User;


public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    void deleteByAuthorAndId(User author, Long id);
}


