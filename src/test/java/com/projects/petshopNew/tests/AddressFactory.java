package com.projects.petshopNew.tests;

import com.projects.petshopNew.entities.Address;
import com.projects.petshopNew.entities.Client;

public class AddressFactory {

    public static Address createAddress(){
        Client client = ClientFactory.createClient();
        return new Address(1L, "Rua 123", "Bento Gonçalves", "Centro", null, "Tag 1", client);
    }
}
