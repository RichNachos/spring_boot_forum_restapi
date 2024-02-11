package com.richnachos.forum.services;

import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserDTOByUsername(username);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public boolean promoteUserById(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        if (currentUser.getRole() == Role.USER) return false;
        if (user.getRole() == Role.ADMIN) return false;
        user.setRole(Role.ADMIN);
        return true;
    }

    public boolean demoteUserById(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        if (currentUser.getRole() == Role.USER) return false;
        if (user.getRole() == Role.USER) return false;
        user.setRole(Role.USER);
        return true;
    }

    public boolean deleteUserById(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        if (currentUser.equals(user) || currentUser.getRole() == Role.ADMIN) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteAllUsers() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getRole() != Role.ADMIN) return false;
        userRepository.deleteAll();
        return true;
    }
}
