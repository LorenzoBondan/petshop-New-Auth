package com.projects.petshopNew.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tb_assistance")
public class Assistance implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double assistanceValue;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant date;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public Assistance(){}

    public Assistance(Long id, String description, Double assistanceValue, Instant date, Pet pet) {
        this.id = id;
        this.description = description;
        this.assistanceValue = assistanceValue;
        this.date = date;
        this.pet = pet;
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

    public Double getAssistanceValue() {
        return assistanceValue;
    }

    public void setAssistanceValue(Double assistanceValue) {
        this.assistanceValue = assistanceValue;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistance that = (Assistance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
