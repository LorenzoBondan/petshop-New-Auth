package com.projects.petshopNew.tests;

import com.projects.petshopNew.entities.Assistance;
import com.projects.petshopNew.entities.Breed;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Pet;

import java.time.Instant;

public class PetFactory {

    public static Pet createPet(){
        Breed breed = BreedFactory.createBreed();
        Client client = ClientFactory.createClient();
        return new Pet(1L, "Bobby", Instant.now(), "https://img.com", breed, client);
    }

    public static Pet createDependentPet(){
        Breed breed = BreedFactory.createBreed();
        Client client = ClientFactory.createClient();
        Assistance assistance = AssistanceFactory.createAssistance();
        Pet pet = new Pet(2L, "Fox", Instant.now(), "https://img.com", breed, client);
        pet.getAssistances().add(assistance);
        return pet;
    }
}
