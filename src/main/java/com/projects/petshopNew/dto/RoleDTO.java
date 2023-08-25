package com.projects.petshopNew.dto;

import com.projects.petshopNew.entities.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class RoleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String authority;

    public RoleDTO(){}

    public RoleDTO(Role entity){
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(id, roleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
