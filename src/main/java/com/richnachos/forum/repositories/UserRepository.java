package com.richnachos.forum.repositories;

import com.richnachos.forum.entities.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {
    User findUserByUsername(String username);

}
