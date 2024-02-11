package com.richnachos.forum.repositories;

import com.richnachos.forum.entities.Post;
import com.richnachos.forum.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    public List<Post> findAllByOrderByUploadDateDesc();

    public List<Post> findPostsByPoster(User user);
}
