package com.projects.petshopNew.tests;

import com.projects.petshopNew.entities.Breed;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Pet;

import java.time.Instant;

public class BreedFactory {

    public static Breed createBreed(){
        return new Breed(1L, "Pincher");
    }

    public static Breed createBreed(Long id, String name){
        return new Breed(id, name);
    }

    public static Breed createDependentBreed(){
        Breed breed = new Breed(2L, "Pug");
        Pet pet = new Pet(1L, "Max", Instant.now(), "https://img", breed, new Client());
        breed.getPets().add(pet);
        return breed;
    }
}
