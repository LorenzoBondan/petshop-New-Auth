package com.projects.petshopNew.services;

import com.projects.petshopNew.dto.UserDTO;
import com.projects.petshopNew.entities.User;
import com.projects.petshopNew.repositories.UserRepository;
import com.projects.petshopNew.services.exceptions.ForbiddenException;
import com.projects.petshopNew.services.exceptions.ResourceNotFoundException;
import com.projects.petshopNew.util.CustomUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CustomUserUtil customUserUtil;

    public User authenticated() {
        try {
            String username = customUserUtil.getLoggedUsername();
            return repository.findByCpf(username).orElseThrow(() -> new ResourceNotFoundException("At UserService, User Cpf not found " + username));
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    public void validateSelfOrAdmin(String userCpf) {
        User me = authenticated();
        if (me.hasRole("ROLE_ADMIN")) {
            return;
        }
        if(!me.getCpf().equals(userCpf)) {
            throw new ForbiddenException("Access denied. Should be self or admin");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User entity = authenticated();
        return new UserDTO(entity);
    }
}
