package com.projects.petshopNew.dto;

import com.projects.petshopNew.entities.Contact;
import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ContactDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String tag;
    private Boolean type;
    @NotBlank(message = "Required field")
    private String contactValue;
    private Long clientId;

    public ContactDTO(){}

    public ContactDTO(Contact entity){
        this.id = entity.getId();
        this.tag = entity.getTag();
        this.type = entity.getType();
        this.contactValue = entity.getContactValue();
        if(entity.getClient() != null){
            this.clientId = entity.getClient().getId();
        }
    }

    public ContactDTO(Long id, String tag, Boolean type, String contactValue, Long clientId) {
        this.id = id;
        this.tag = tag;
        this.type = type;
        this.contactValue = contactValue;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactDTO that = (ContactDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
