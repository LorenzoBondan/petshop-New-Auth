package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    //@EntityGraph(attributePaths = {"client, breed, assistances"})
    //Pet getById(Long id);
}
