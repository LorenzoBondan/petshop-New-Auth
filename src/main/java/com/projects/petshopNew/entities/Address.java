package com.projects.petshopNew.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_address")
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    @Column(columnDefinition = "TEXT")
    private String street;
    @Nullable
    private String city;
    @Nullable
    private String neighborhood;
    @Nullable
    private Integer complement;
    @Nullable
    private String tag;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Address(){}

    public Address(Long id, @Nullable String street, @Nullable String city, @Nullable String neighborhood, @Nullable Integer complement, @Nullable String tag, Client client) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.neighborhood = neighborhood;
        this.complement = complement;
        this.tag = tag;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public String getStreet() {
        return street;
    }

    public void setStreet(@Nullable String street) {
        this.street = street;
    }

    @Nullable
    public String getCity() {
        return city;
    }

    public void setCity(@Nullable String city) {
        this.city = city;
    }

    @Nullable
    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(@Nullable String neighborhood) {
        this.neighborhood = neighborhood;
    }

    @Nullable
    public Integer getComplement() {
        return complement;
    }

    public void setComplement(@Nullable Integer complement) {
        this.complement = complement;
    }

    @Nullable
    public String getTag() {
        return tag;
    }

    public void setTag(@Nullable String tag) {
        this.tag = tag;
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
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
