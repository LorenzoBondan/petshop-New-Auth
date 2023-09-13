package com.projects.petshopNew.resources;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import com.projects.petshopNew.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AssistanceResourceTests {

    private String adminToken, clientToken, invalidToken;

    private Long existingId, nonExistingId;

    private Map<String, Object> postAssistanceInstance;

    private Map<String, Object> putAssistanceInstance;

    @BeforeEach
    public void setUp() throws JSONException {
        baseURI = "http://localhost:8080";

        String clientUsername = "123.456.789-00";
        String clientPassword = "123456";
        String adminUsername = "000.123.456-78";
        String adminPassword = "123456";

        nonExistingId = 1000L;

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "aaaaaaaaa";

        // dto attributes
        postAssistanceInstance = new HashMap<>();
        postAssistanceInstance.put("description", "First Assistance");
        postAssistanceInstance.put("assistanceValue", 20.0F);
        postAssistanceInstance.put("date", Instant.now().plusSeconds(5L));
        postAssistanceInstance.put("petId", 1);

        putAssistanceInstance = new HashMap<>();
        putAssistanceInstance.put("description", "Updated Assistance");
        putAssistanceInstance.put("assistanceValue", 200.0);
        putAssistanceInstance.put("date", Instant.now());
        putAssistanceInstance.put("petId", 1);
    }

    @Test
    public void findAllShouldReturnListOfAssistancesWhenAdminLogged(){
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .get("/assistances")
        .then()
                .statusCode(200)
                .body("id", hasItems(1, 2, 3));
    }

    @Test
    public void findAllShouldReturnForbiddenWhenClientLogged(){
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
        .when()
                .get("/assistances")
        .then()
                .statusCode(403);
    }

    @Test
    public void findAllShouldReturnUnauthorizedWhenInvalidToken(){
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
        .when()
                .get("/assistances")
        .then()
                .statusCode(401);
    }

    @Test
    public void findAllPageableShouldReturnPagedAssistancesWhenAdminLogged(){
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .get("/assistances/pageable")
        .then()
                .statusCode(200)
                .body("content.id[0]", is(1))
                .body("content.description[0]", equalTo("Regular exams"))
                .body("totalPages", is(1))
                .body("empty", is(false));
    }

    @Test
    public void findAllPageableShouldReturnForbiddenWhenClientLogged() {
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
        .when()
                .get("/assistances/pageable")
        .then()
                .statusCode(403);
    }

    @Test
    public void findAllPageableShouldReturnUnauthorizedWhenInvalidToken(){
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
        .when()
                .get("/assistances/pageable")
        .then()
                .statusCode(401);
    }

    @Test
    public void findByIdShouldReturnAssistanceWhenExistingIdAndAdminLogged(){
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .get("/assistances/{id}", existingId)
        .then()
                .statusCode(200)
                .body("id", is(1))
                .body("description", equalTo("Regular exams"))
                .body("assistanceValue", is(50.0F))
                .body("petId", is(1));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistsAndAdminLogged(){
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .get("/assistances/{id}", nonExistingId)
        .then()
                .statusCode(404);
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenIdExistsAndClientLogged(){
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
        .when()
                .get("/assistances/{id}", existingId)
        .then()
                .statusCode(403);
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenIdDoesNotExistsAndClientLogged(){
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
        .when()
                .get("/assistances/{id}", nonExistingId)
        .then()
                .statusCode(403);
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenIdExistsAndInvalidToken(){
        existingId = 1L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
        .when()
                .get("/assistances/{id}", existingId)
        .then()
                .statusCode(401);
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenIdDoesNotExistsAndInvalidToken(){
        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
        .when()
                .get("assistances/{id}", nonExistingId)
        .then()
                .statusCode(401);
    }

    @Test
    public void insertShouldReturnAssistanceCreatedWhenAdminLogged(){
        JSONObject newAssistance = new JSONObject(postAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newAssistance.toString())
        .when()
                .post("/assistances")
        .then()
                .statusCode(201)
                .body("description", equalTo("First Assistance"))
                .body("assistanceValue", is(20.0F))
                .body("petId", is(1));
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndDescriptionIsBlank(){
        postAssistanceInstance.put("description", "");
        JSONObject newAssistance = new JSONObject(postAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newAssistance.toString())
        .when()
                .post("/assistances")
        .then()
                .statusCode(422);
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndAssistanceValueIsNegative(){
        postAssistanceInstance.put("assistanceValue", -2.0);
        JSONObject newAssistance = new JSONObject(postAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newAssistance.toString())
        .when()
                .post("/assistances")
        .then()
                .statusCode(422);
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPetIdIsNull(){
        postAssistanceInstance.put("petId", null);
        JSONObject newAssistance = new JSONObject(postAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newAssistance.toString())
        .when()
                .post("/assistances")
        .then()
                .statusCode(422);
    }

    @Test
    public void insertShouldReturnBadRequestWhenAdminLoggedAndAssistanceValueIsString(){
        postAssistanceInstance.put("assistanceValue", "abc");
        JSONObject newAssistance = new JSONObject(postAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newAssistance.toString())
        .when()
                .post("/assistances")
        .then()
                .statusCode(400);
    }

    @Test
    public void insertShouldReturnForbiddenWhenClientLogged(){
        JSONObject newAssistance = new JSONObject(postAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newAssistance.toString())
        .when()
                .post("/assistances")
        .then()
                .statusCode(403);
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken(){
        JSONObject newAssistance = new JSONObject(postAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newAssistance.toString())
        .when()
                .post("/assistances")
        .then()
                .statusCode(401);
    }

    @Test
    public void updateShouldReturnAssistanceWhenIdExistsAndAdminLogged(){
        existingId = 2L;

        JSONObject assistance = new JSONObject(putAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", existingId)
        .then()
                .statusCode(200)
                .body("description", equalTo("Updated Assistance"))
                .body("assistanceValue", is(200.0F))
                .body("petId", is(1));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExistsAndAdminLogged(){
        JSONObject assistance = new JSONObject(putAssistanceInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", nonExistingId)
        .then()
                .statusCode(404);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenDescriptionIsBlankAndAdminLogged(){
        putAssistanceInstance.put("description", "");
        JSONObject assistance = new JSONObject(putAssistanceInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", existingId)
        .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenAssistanceValueIsNegativeAndAdminLogged(){
        putAssistanceInstance.put("assistanceValue", -1.0F);
        JSONObject assistance = new JSONObject(putAssistanceInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", existingId)
        .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnUnprocessableEntityWhenPetIdIsNullAndAdminLogged(){
        putAssistanceInstance.put("petId", null);
        JSONObject assistance = new JSONObject(putAssistanceInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", existingId)
        .then()
                .statusCode(422);
    }

    @Test
    public void updateShouldReturnBadRequestWhenAssistanceValueIsStringAndAdminLogged(){
        putAssistanceInstance.put("assistanceValue", "abc");
        JSONObject assistance = new JSONObject(putAssistanceInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", existingId)
        .then()
                .statusCode(400);
    }

    @Test
    public void updateShouldReturnForbiddenWhenClientLogged(){
        JSONObject assistance = new JSONObject(putAssistanceInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", existingId)
        .then()
                .statusCode(403);
    }

    @Test
    public void updateShouldReturnUnauthorizedWhenInvalidToken(){
        JSONObject assistance = new JSONObject(putAssistanceInstance);
        existingId = 2L;

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(assistance.toString())
        .when()
                .put("/assistances/{id}", existingId)
        .then()
                .statusCode(401);
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExistsAndAdminLogged(){
        existingId = 4L;

        given()
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .delete("/assistances/{id}", existingId)
        .then()
                .statusCode(204);
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExistsAndAdminLogged(){
        given()
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .delete("/assistances/{id}", nonExistingId)
        .then()
                .statusCode(404);
    }

    @Test
    public void deleteShouldReturnForbiddenWhenClientLogged(){
        given()
                .header("Authorization", "Bearer " + clientToken)
        .when()
                .delete("/assistances/{id}", nonExistingId)
        .then()
                .statusCode(403);
    }

    @Test
    public void deleteShouldReturnUnauthorizedWhenClientLogged(){
        given()
                .header("Authorization", "Bearer " + invalidToken)
        .when()
                .delete("/assistances/{id}", nonExistingId)
        .then()
                .statusCode(401);
    }
}
