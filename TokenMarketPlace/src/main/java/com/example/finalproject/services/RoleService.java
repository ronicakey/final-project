package com.example.finalproject.services;

import com.example.finalproject.models.Role;
import com.example.finalproject.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role getRole(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    public Role getAdminRole() {
        return getRole("ROLE_ADMIN");
    }

    @Transactional
    public Role getUserRole() {
        return getRole("ROLE_USER") ;
    }

    @Transactional
    public Role getCreatorRole() {
        return getRole("ROLE_CREATOR") ;
    }
}
