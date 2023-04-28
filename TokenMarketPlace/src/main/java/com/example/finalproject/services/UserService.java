package com.example.finalproject.services;

import com.example.finalproject.models.Role;
import com.example.finalproject.models.User;
import com.example.finalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User getUserById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public User getUserByLogin(User user) {
        Optional<User> optionalUser = userRepository.findByLogin(user.getLogin());
        return optionalUser.orElse(null);
    }

    public List<User> getAllUsers(){
        return userRepository.findByOrderByIdAsc();
    }

    @Transactional
    public void register(User user, String filename) {
        user.setAvatar(filename);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.getUserRole());
        userRepository.save(user);
    }

    @Transactional
    public void updateUserRole(int id, User user, Role role) {
        user.setId(id);
        user.setRole(role);
        userRepository.save(user);
    }
}