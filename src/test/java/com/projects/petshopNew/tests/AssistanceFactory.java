package com.projects.petshopNew.tests;

import com.projects.petshopNew.entities.Assistance;
import com.projects.petshopNew.entities.Pet;

import java.time.Instant;

public class AssistanceFactory {

    public static Assistance createAssistance(){
        Pet pet = PetFactory.createPet();
        return new Assistance(1L, "Regular exams", 450.0, Instant.now(), pet);
    }
}
