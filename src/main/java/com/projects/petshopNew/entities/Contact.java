package com.projects.petshopNew.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private String tag;
    @Nullable
    private Boolean type;
    @Nullable
    private String contactValue;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Contact(){}

    public Contact(Long id, @Nullable String tag, @Nullable Boolean type, @Nullable String contactValue, Client client) {
        this.id = id;
        this.tag = tag;
        this.type = type;
        this.contactValue = contactValue;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public String getTag() {
        return tag;
    }

    public void setTag(@Nullable String tag) {
        this.tag = tag;
    }

    @Nullable
    public Boolean getType() {
        return type;
    }

    public void setType(@Nullable Boolean type) {
        this.type = type;
    }

    @Nullable
    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(@Nullable String contactValue) {
        this.contactValue = contactValue;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
