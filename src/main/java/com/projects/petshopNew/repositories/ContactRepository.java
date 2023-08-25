package com.projects.petshopNew.repositories;

import com.projects.petshopNew.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
