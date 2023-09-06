package com.projects.petshopNew.dto;

import com.projects.petshopNew.entities.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    @NotBlank(message = "Required field")
    private String name;
    @Past(message = "Birthdate must be in the past")
    private Instant birthDate;
    private String imgUrl;
    private Long clientId;
    private Long breedId;
    private final List<AssistanceDTO> assistances = new ArrayList<>();

    public PetDTO(){}

    public PetDTO(Pet entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.birthDate = entity.getBirthDate();
        this.imgUrl = entity.getImgUrl();
        this.clientId = entity.getClient().getId();
        this.breedId = entity.getBreed().getId();

        entity.getAssistances().forEach(assistance -> this.assistances.add(new AssistanceDTO(assistance)));
    }

    public PetDTO(Long id, String name, Instant birthDate, String imgUrl, Long clientId, Long breedId) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.imgUrl = imgUrl;
        this.clientId = clientId;
        this.breedId = breedId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getBreedId() {
        return breedId;
    }

    public void setBreedId(Long breedId) {
        this.breedId = breedId;
    }

    public List<AssistanceDTO> getAssistances() {
        return assistances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetDTO petDTO = (PetDTO) o;
        return Objects.equals(id, petDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
