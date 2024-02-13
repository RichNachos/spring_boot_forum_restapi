package com.richnachos.forum.services;

import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean promoteUserById(Long id) {
        User authenticatedUser = getAuthenticatedUser();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        if (authenticatedUser.getRole() == Role.USER) return false;
        if (user.getRole() == Role.ADMIN) return false;
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return true;
    }

    public boolean demoteUserById(Long id) {
        User authenticatedUser = getAuthenticatedUser();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        if (authenticatedUser.getRole() == Role.USER) return false;
        if (user.getRole() == Role.USER) return false;
        user.setRole(Role.USER);
        userRepository.save(user);
        return true;
    }

    public boolean deleteUserById(Long id) {
        User authenticatedUser = getAuthenticatedUser();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        if (authenticatedUser.equals(user) || authenticatedUser.getRole() == Role.ADMIN) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteAllUsers() {
        User currentUser = getAuthenticatedUser();
        if (currentUser.getRole() != Role.ADMIN) return false;
        userRepository.deleteAll();
        return true;
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
