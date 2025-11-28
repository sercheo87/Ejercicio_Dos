package com.example.demo.e2e;

import com.example.demo.model.dto.ClienteRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
@DisplayName("E2E Tests - Complete Cliente Workflows (Real HTTP Calls)")
class ClienteE2ETest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void givenSetup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/clientes";
    }

    @Test
    @DisplayName("givenCompleteWorkflow_whenCreateListAndGetCliente_thenAllOperationsSucceed")
    void givenCompleteWorkflow_whenCreateListAndGetCliente_thenAllOperationsSucceed() {
        var createRequest = new ClienteRequestDTO();
        createRequest.setNombre("Carlos Ramirez");
        createRequest.setEmail("carlos.ramirez@example.com");
        createRequest.setTelefono("0998877665");

        var clienteId = given()
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("nombre", equalTo("Carlos Ramirez"))
                .body("email", equalTo("carlos.ramirez@example.com"))
                .body("telefono", equalTo("0998877665"))
                .body("activo", equalTo(true))
                .body("id", notNullValue())
                .body("fechaRegistro", notNullValue())
                .extract()
                .path("id");

        Assertions.assertNotNull(clienteId);

        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("find { it.id == " + clienteId + " }.nombre", equalTo("Carlos Ramirez"));

        given()
                .when()
                .get("/{id}", clienteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(clienteId))
                .body("nombre", equalTo("Carlos Ramirez"))
                .body("email", equalTo("carlos.ramirez@example.com"))
                .body("activo", equalTo(true));
    }

    @Test
    @DisplayName("givenFullCRUDWorkflow_whenCreateUpdateAndDelete_thenAllOperationsSucceed")
    void givenFullCRUDWorkflow_whenCreateUpdateAndDelete_thenAllOperationsSucceed() {
        var createRequest = new ClienteRequestDTO();
        createRequest.setNombre("Laura Sanchez");
        createRequest.setEmail("laura.sanchez@example.com");
        createRequest.setTelefono("0987654321");

        var clienteId = given()
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("nombre", equalTo("Laura Sanchez"))
                .extract()
                .path("id");

        var updateRequest = new ClienteRequestDTO();
        updateRequest.setNombre("Laura Sanchez Updated");
        updateRequest.setEmail("laura.updated@example.com");
        updateRequest.setTelefono("0999887766");

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/{id}", clienteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(clienteId))
                .body("nombre", equalTo("Laura Sanchez Updated"))
                .body("email", equalTo("laura.updated@example.com"))
                .body("telefono", equalTo("0999887766"));

        given()
                .when()
                .get("/{id}", clienteId)
                .then()
                .statusCode(200)
                .body("nombre", equalTo("Laura Sanchez Updated"))
                .body("activo", equalTo(true));

        given()
                .when()
                .delete("/{id}", clienteId)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/{id}", clienteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(clienteId))
                .body("nombre", equalTo("Laura Sanchez Updated"))
                .body("activo", equalTo(false));
    }
}

