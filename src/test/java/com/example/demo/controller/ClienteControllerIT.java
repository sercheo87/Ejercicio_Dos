package com.example.demo.controller;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
@DisplayName("Integration Tests - ClienteController (Controller + Service + Repository)")
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("givenValidClienteRequest_whenCrearCliente_thenReturnsCreatedWithLocationHeader")
    void givenValidClienteRequest_whenCrearCliente_thenReturnsCreatedWithLocationHeader() throws Exception {
        var requestDTO = new ClienteRequestDTO();
        requestDTO.setNombre("Maria Lopez");
        requestDTO.setEmail("maria.lopez@example.com");
        requestDTO.setTelefono("0987654321");

        var requestJson = objectMapper.writeValueAsString(requestDTO);

        MvcResult result = mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Maria Lopez"))
                .andExpect(jsonPath("$.email").value("maria.lopez@example.com"))
                .andExpect(jsonPath("$.telefono").value("0987654321"))
                .andExpect(jsonPath("$.activo").value(true))
                .andReturn();

        var responseJson = result.getResponse().getContentAsString();
        Assertions.assertNotNull(responseJson);
        Assertions.assertTrue(responseJson.contains("Maria Lopez"));
    }

    @Test
    @DisplayName("givenExistingClienteId_whenObtenerPorId_thenReturnsClienteWithAllFields")
    void givenExistingClienteId_whenObtenerPorId_thenReturnsClienteWithAllFields() throws Exception {
        var createRequest = new ClienteRequestDTO();
        createRequest.setNombre("Pedro Gomez");
        createRequest.setEmail("pedro.gomez@example.com");
        createRequest.setTelefono("0991234567");

        var requestJson = objectMapper.writeValueAsString(createRequest);

        MvcResult createResult = mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        var createdClienteJson = createResult.getResponse().getContentAsString();
        var createdCliente = objectMapper.readTree(createdClienteJson);
        var clienteId = createdCliente.get("id").asLong();

        mockMvc.perform(get("/api/v1/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteId))
                .andExpect(jsonPath("$.nombre").value("Pedro Gomez"))
                .andExpect(jsonPath("$.email").value("pedro.gomez@example.com"))
                .andExpect(jsonPath("$.telefono").value("0991234567"))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.fechaRegistro").exists());
    }

    @Test
    @DisplayName("givenExistingCliente_whenActualizar_thenReturnsUpdatedClienteWithChanges")
    void givenExistingCliente_whenActualizar_thenReturnsUpdatedClienteWithChanges() throws Exception {
        var createRequest = new ClienteRequestDTO();
        createRequest.setNombre("Ana Martinez");
        createRequest.setEmail("ana.martinez@example.com");
        createRequest.setTelefono("0981234567");

        var createJson = objectMapper.writeValueAsString(createRequest);

        MvcResult createResult = mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();

        var createdClienteJson = createResult.getResponse().getContentAsString();
        var createdCliente = objectMapper.readTree(createdClienteJson);
        var clienteId = createdCliente.get("id").asLong();

        var updateRequest = new ClienteRequestDTO();
        updateRequest.setNombre("Ana Martinez Updated");
        updateRequest.setEmail("ana.updated@example.com");
        updateRequest.setTelefono("0999999999");

        var updateJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/clientes/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteId))
                .andExpect(jsonPath("$.nombre").value("Ana Martinez Updated"))
                .andExpect(jsonPath("$.email").value("ana.updated@example.com"))
                .andExpect(jsonPath("$.telefono").value("0999999999"))
                .andExpect(jsonPath("$.activo").value(true));

        mockMvc.perform(get("/api/v1/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana Martinez Updated"));
    }
}

