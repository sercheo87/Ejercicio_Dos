package com.example.demo.security;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Pruebas de SEGURIDAD (Security Testing)
 * <p>
 * Objetivo: Detectar vulnerabilidades y validar datos de entrada
 * Categorías: Injection, XSS, Validación de datos
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    private static final String API_CLIENTES_URL = "/api/v1/clientes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void givenSetup() {
        System.out.println("\n🔒 INICIANDO PRUEBA DE SEGURIDAD");
    }

    @Test
    void givenSqlInjectionPayload_whenCreatingCliente_thenReturnsBadRequest() throws Exception {
        System.out.println("🛡 Test: SQL Injection Protection");

        var maliciousClient = new ClienteRequestDTO();
        maliciousClient.setNombre("'; DROP TABLE clientes; --");
        maliciousClient.setEmail("hacker@test.com");

        mockMvc.perform(MockMvcRequestBuilders.post(API_CLIENTES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(maliciousClient)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("✅ SQL Injection prevención: BLOQUEADO por validación @Pattern");
    }

    @Test
    void givenXssPayload_whenCreatingCliente_thenReturnsBadRequest() throws Exception {
        System.out.println("🛡 Test: XSS Protection");

        var xssClient = new ClienteRequestDTO();
        xssClient.setNombre("<script>alert('XSS')</script>");
        xssClient.setEmail("xss@test.com");

        mockMvc.perform(MockMvcRequestBuilders.post(API_CLIENTES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(xssClient)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("✅ XSS Prevention: BLOQUEADO por validación @Pattern");
    }

    @Test
    void givenInvalidEmailFormat_whenCreatingCliente_thenReturnsBadRequest() throws Exception {
        System.out.println("🛡 Test: Email Validation");

        var invalidEmailClient = new ClienteRequestDTO();
        invalidEmailClient.setNombre("Test User");
        invalidEmailClient.setEmail("invalid-email-format");

        mockMvc.perform(MockMvcRequestBuilders.post(API_CLIENTES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailClient)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("✅ Email validation: Sistema valida formato correctamente");
    }

    @Test
    void givenEmptyNombre_whenCreatingCliente_thenReturnsBadRequest() throws Exception {
        System.out.println("🛡 Test: Empty Fields Validation");

        var emptyClient = new ClienteRequestDTO();
        emptyClient.setNombre("");
        emptyClient.setEmail("valid@test.com");

        mockMvc.perform(MockMvcRequestBuilders.post(API_CLIENTES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyClient)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("✅ Empty fields validation: Sistema rechaza campos vacíos correctamente");
    }
}