package com.example.finalproject.services;

import com.example.finalproject.models.User;
import com.example.finalproject.repositories.UserRepository;
import com.example.finalproject.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Value("${spring.security.user.name}")
    private String adminUserName;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Autowired
    public UserDetailsService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user;
        if (username.equals(adminUserName)) {
            //дефолтный администратор из application.properties
            user = new User(adminUserName, adminPassword, roleService.getAdminRole());
        } else {
            Optional<User> optionalUser = userRepository.findByLogin(username);
            if (optionalUser.isEmpty()){
                throw new UsernameNotFoundException("Пользователь не найден");
            }
            user = optionalUser.get();
        }
        return new UserDetails(user);
    }
}