package com.example.demo.performance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@DisplayName("Performance Tests - Response Time Validation")
class PerformanceTest {

    private static final long SLA_RESPONSE_TIME_MS = 200L;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void givenSetup() throws Exception {
        System.out.println("\n=== INICIANDO PRUEBA DE PERFORMANCE ===");
        mockMvc.perform(get("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON));
        System.out.println("Warm-up completado");
    }

    /**
     * TEST 1: Verificar que GET /api/v1/clientes responde en menos de 200ms
     * <p>
     * Concepto: Medir latencia de endpoint que retorna lista
     * Métrica: Tiempo de respuesta < 200ms
     */
    @Test
    void givenGetAllClientes_whenCalled_thenRespondsWithinSLA() throws Exception {
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("⏱ GET /api/v1/clientes - Tiempo: " + duration + "ms");

        if (duration > SLA_RESPONSE_TIME_MS) {
            throw new AssertionError(
                    "❌ Endpoint tardó " + duration + "ms. SLA: < " + SLA_RESPONSE_TIME_MS + "ms"
            );
        }

        System.out.println("✅ Performance OK: " + duration + "ms < " + SLA_RESPONSE_TIME_MS + "ms");
    }

    /**
     * TEST 2: Verificar que GET /api/v1/clientes/{id} responde en menos de 100ms
     * <p>
     * Concepto: Operaciones por clave primaria son más rápidas
     * Métrica: Tiempo de respuesta < 100ms (más estricto que Test 1)
     */
    @Test
    void givenGetClienteById_whenCalled_thenRespondsWithinSLA() throws Exception {
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("⏱ GET /api/v1/clientes/1 - Tiempo: " + duration + "ms");

        if (duration > 100) {
            throw new AssertionError(
                    "❌ Endpoint tardó " + duration + "ms. SLA: < 100ms (operación simple)"
            );
        }

        System.out.println("✅ Performance OK: " + duration + "ms < 100ms");
    }

    @Test
    @DisplayName("givenPostCliente_whenCalled_thenRespondsWithinSLA")
    void givenPostCliente_whenCalled_thenRespondsWithinSLA() throws Exception {
        var clienteJson = """
                {
                    "nombre": "Performance Test User",
                    "email": "performance.test@example.com",
                    "telefono": "1234567890"
                }
                """;

        var startTime = System.currentTimeMillis();

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(status().isCreated());

        var endTime = System.currentTimeMillis();
        var responseTime = endTime - startTime;

        System.out.println("POST /api/v1/clientes - Tiempo de respuesta: " + responseTime + "ms");
        System.out.println("SLA esperado: < " + SLA_RESPONSE_TIME_MS + "ms");

        Assertions.assertTrue(responseTime < SLA_RESPONSE_TIME_MS,
                "El endpoint POST /api/v1/clientes excedió el SLA. Tiempo: " + responseTime + "ms, SLA: " + SLA_RESPONSE_TIME_MS + "ms");
    }
}

