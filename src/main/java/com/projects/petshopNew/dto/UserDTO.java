package com.projects.petshopNew.dto;

import com.projects.petshopNew.entities.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String cpf;
    private String name;
    private Long clientId;
    private final List<RoleDTO> roles = new ArrayList<>();

    public UserDTO(){}

    public UserDTO(User entity){
        this.cpf = entity.getCpf();
        this.name = entity.getName();
        this.clientId = entity.getClient().getId();

        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public UserDTO(String cpf, String name, Long clientId) {
        this.cpf = cpf;
        this.name = name;
        this.clientId = clientId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(cpf, userDTO.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
