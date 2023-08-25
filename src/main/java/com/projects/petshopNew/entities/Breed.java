package com.projects.petshopNew.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "tb_breed")
public class Breed implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @OneToMany(mappedBy = "breed", fetch = FetchType.EAGER)
    private Set<Pet> pets = new HashSet<>(); // List was duplicating pets from users with more than one role

    public Breed(){}

    public Breed(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Breed breed = (Breed) o;
        return Objects.equals(id, breed.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
