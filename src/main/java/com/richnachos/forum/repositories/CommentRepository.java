package com.richnachos.forum.repositories;

import com.richnachos.forum.entities.Comment;
import com.richnachos.forum.entities.Post;
import com.richnachos.forum.entities.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends ListCrudRepository<Comment, Long> {
    List<Comment> findAllByOrderByUploadDateDesc();

    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByCommenter(User commenter);
}
