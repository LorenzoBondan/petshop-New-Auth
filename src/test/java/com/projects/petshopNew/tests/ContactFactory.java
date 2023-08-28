package com.projects.petshopNew.tests;

import com.projects.petshopNew.entities.Client;
import com.projects.petshopNew.entities.Contact;

public class ContactFactory {

    public static Contact createContact(){
        Client client = ClientFactory.createClient();
        return new Contact(1L, "Tag 1", true, "test@email.com", client);
    }
}
