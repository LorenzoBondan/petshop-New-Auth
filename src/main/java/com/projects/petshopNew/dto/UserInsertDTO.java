package com.projects.petshopNew.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

public class UserInsertDTO extends UserDTO{
    @Serial
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Required field")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    public UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
