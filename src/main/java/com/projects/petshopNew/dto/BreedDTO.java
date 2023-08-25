package com.projects.petshopNew.dto;

import com.projects.petshopNew.entities.Breed;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BreedDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String description;
    private final List<Long> petsId = new ArrayList<>();

    public BreedDTO(){}

    public BreedDTO(Breed entity){
        this.id = entity.getId();
        this.description = entity.getDescription();
        this.petsId.clear();
        entity.getPets().forEach(pet -> this.petsId.add(pet.getId()));
    }

    public BreedDTO(Long id, String description) {
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

    public List<Long> getPetsId() {
        return petsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BreedDTO breedDTO = (BreedDTO) o;
        return Objects.equals(id, breedDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
