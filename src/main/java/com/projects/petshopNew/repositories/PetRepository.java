package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
