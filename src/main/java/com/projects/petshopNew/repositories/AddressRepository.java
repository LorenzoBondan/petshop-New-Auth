package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
