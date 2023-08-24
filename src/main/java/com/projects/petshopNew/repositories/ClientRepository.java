package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
