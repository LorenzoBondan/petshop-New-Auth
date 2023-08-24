package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, Long> {
}
