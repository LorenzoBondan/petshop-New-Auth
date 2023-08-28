package com.projects.petshopNew.tests;

import com.projects.petshopNew.entities.Address;
import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Contact;
import com.projects.petshopNew.entities.User;

import java.time.Instant;

public class ClientFactory {

    public static Client createClient(){
        User user = UserFactory.createClientUser();
        return new Client(1L, "Alex", Instant.now(), "https://img", user, null, null);
    }

    public static Client createAdminClient(){
        User user = UserFactory.createAdminUser();
        return new Client(2L, "Maria", Instant.now(), "https://img", user, null, null);
    }
}
