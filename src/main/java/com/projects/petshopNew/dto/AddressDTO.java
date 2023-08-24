package com.projects.petshopNew.dto;

import com.projects.petshopNew.entities.Address;

import java.util.Objects;

public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String neighborhood;
    private Integer complement;
    private String tag;
    private Long clientId;

    public AddressDTO(){}

    public AddressDTO(Address entity){
        this.id = entity.getId();
        this.street = entity.getStreet();
        this.city = entity.getCity();
        this.neighborhood = entity.getNeighborhood();
        this.complement = entity.getComplement();
        this.tag = entity.getTag();
        this.clientId = entity.getClient().getId();
    }

    public AddressDTO(Long id, String street, String city, String neighborhood, Integer complement, String tag, Long clientId) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.neighborhood = neighborhood;
        this.complement = complement;
        this.tag = tag;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getComplement() {
        return complement;
    }

    public void setComplement(Integer complement) {
        this.complement = complement;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        AddressDTO that = (AddressDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
