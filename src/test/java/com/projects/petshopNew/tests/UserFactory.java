package com.projects.petshopNew.tests;

import com.projects.petshopNew.dto.RoleDTO;
import com.projects.petshopNew.dto.UserInsertDTO;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Role;
import com.projects.petshopNew.entities.User;

public class UserFactory {

    public static User createClientUser(){
        User user = new User("123.456.789-10", "Alex", "123456", null);
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser(){
        User user = new User("000.111.222-33", "Maria", "123456", null);
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(String cpf, String username){
        User user = new User(cpf, username, "123456", null);
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(String cpf, String username){
        User user = new User(cpf, username, "123456", null);
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static UserInsertDTO createUserInsertDTO(Client client){
        UserInsertDTO dto = new UserInsertDTO();
        dto.setName("Alice");
        dto.setCpf("987.654.321-00");
        dto.setPassword("123456");
        dto.setClientId(client.getId());
        dto.getRoles().add(new RoleDTO(1L, "ROLE_CLIENT"));
        return dto;
    }
}
