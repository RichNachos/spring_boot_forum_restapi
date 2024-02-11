package com.richnachos.forum.repositories;

import com.richnachos.forum.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public User findUserDTOByUsername(String username);

}
