package com.projects.petshopNew.resources;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.projects.petshopNew.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AddressResourceTests {

    private String adminToken, clientToken, invalidToken;
    private Long existingId, nonExistingId;
    private Map<String, Object> putAddressInstance;

    @BeforeEach
    public void setUp() throws JSONException{
        baseURI = "http://localhost:8080";

        String clientUsername = "123.456.789-00";
        String clientPassword = "123456";
        String adminUsername = "000.123.456-78";
        String adminPassword = "123456";

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "aaaaaaaaa";

        // dto attributes
        putAddressInstance = new HashMap<>();
        putAddressInstance.put("street", "Rua Saldanha Marinho");
        putAddressInstance.put("city", "Garibaldi");
        putAddressInstance.put("neighborhood", "Centro");
        putAddressInstance.put("complement", 1);
        putAddressInstance.put("tag", "Tag2");
        putAddressInstance.put("clientId", 1);
    }

    @Test
    public void updateShouldReturnAddressWhenIdExistsAndAdminLogged() {
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString()) // <- added toString()
        .when()
                .put("/addresses/{id}", existingId)
        .then()
                .statusCode(200)
                .body("street", equalTo("Rua Saldanha Marinho"))
                .body("city", equalTo("Garibaldi"))
                .body("neighborhood", equalTo("Centro"))
                .body("complement", is(1))
                .body("tag", equalTo("Tag2"))
                .body("clientId", is(1));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExistsAndAdminLogged() {
        JSONObject address = new JSONObject(putAddressInstance);
        nonExistingId = 1000L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
        .when()
                .put("/addresses/{id}", nonExistingId)
        .then()
                .statusCode(404)
                .body("error", equalTo("Address id not found"))
                .body("status", equalTo(404));
    }

    @Test
    public void updateShouldReturnBadRequestWhenIdExistsAndInvalidComplementAndAdminLogged() {
        putAddressInstance.put("complement", "aaaaa");
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
        .when()
                .put("/addresses/{id}", existingId)
        .then()
                .statusCode(400);
    }

    @Test
    public void updateShouldReturnInternalServerErrorWhenIdExistsAndInvalidClientIdAndAdminLogged() {
        putAddressInstance.put("clientId", 1000);
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
        .when()
                .put("/addresses/{id}", existingId)
        .then()
                .statusCode(500);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenIdExistsAndStreetIsInvalidAndAdminLogged() {
        putAddressInstance.put("street", "a");
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
        .when()
                .put("/addresses/{id}", existingId)
        .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenIdExistsAndStreetIsBlankAndAdminLogged() {
        putAddressInstance.put("street", "");
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
                .when()
                .put("/addresses/{id}", existingId)
                .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenIdExistsAndCityIsBlankAndAdminLogged() {
        putAddressInstance.put("city", "");
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
                .when()
                .put("/addresses/{id}", existingId)
                .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenIdExistsAndNeighborhoodIsBlankAndAdminLogged() {
        putAddressInstance.put("neighborhood", "");
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
                .when()
                .put("/addresses/{id}", existingId)
                .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenIdExistsAndComplementIsNegativeAndAdminLogged() {
        putAddressInstance.put("complement", -20);
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
        .when()
                .put("/addresses/{id}", existingId)
        .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenIdExistsAndComplementIsZeroAndAdminLogged() {
        putAddressInstance.put("complement", 0);
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
                .when()
                .put("/addresses/{id}", existingId)
                .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenIdExistsAndClientIdIsNullAndAdminLogged() {
        putAddressInstance.put("clientId", null);
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
        .when()
                .put("/addresses/{id}", existingId)
        .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnAddressWhenIdExistsAndClientLoggedTryToUpdateOwnAddress(){
        putAddressInstance.put("complement", 4);
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
                .when()
                .put("/addresses/{id}", existingId)
                .then()
                .statusCode(200);
    }

    @Test
    public void updateShouldReturnForbiddenWhenIdExistsAndClientLoggedAndTryToUpdateOtherClientAddress(){
        putAddressInstance.put("complement", 4);
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
                .when()
                .put("/addresses/{id}", existingId)
                .then()
                .statusCode(403);
    }

    @Test
    public void updateShouldReturnUnauthorizedWhenIdExistsAndInvalidToken(){
        putAddressInstance.put("complement", 4);
        JSONObject address = new JSONObject(putAddressInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(address.toString())
                .when()
                .put("/addresses/{id}", existingId)
                .then()
                .statusCode(401);
    }
}
