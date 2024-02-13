package com.richnachos.forum.repositories;

import com.richnachos.forum.entities.Post;
import com.richnachos.forum.entities.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends ListCrudRepository<Post, Long> {

    List<Post> findAllByOrderByUploadDateDesc();

    List<Post> findPostsByPoster(User user);
}
