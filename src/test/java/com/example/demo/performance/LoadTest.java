package com.example.demo.performance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml", properties = {
    "spring.datasource.hikari.maximum-pool-size=50",
    "spring.datasource.hikari.minimum-idle=20",
    "spring.jpa.properties.hibernate.jdbc.batch_size=50",
    "logging.level.root=OFF"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Load Tests - Concurrent Users Simulation")
class LoadTest {

    private static final int WARMUP_REQUESTS = 100;

    @LocalServerPort
    private int port;

    @BeforeAll
    void givenWarmup() throws InterruptedException {
        RestAssured.port = port;
        System.out.println("\n🔥 CALENTANDO APLICACIÓN (Puerto: " + port + ")");

        ExecutorService warmupExecutor = Executors.newFixedThreadPool(10);
        List<Future<?>> warmupFutures = new ArrayList<>();

        for (int i = 0; i < WARMUP_REQUESTS; i++) {
            warmupFutures.add(warmupExecutor.submit(() -> {
                try {
                    given().when().get("/api/v1/clientes");
                } catch (Exception ignored) {
                }
            }));
        }

        warmupExecutor.shutdown();
        warmupExecutor.awaitTermination(30, TimeUnit.SECONDS);

        Thread.sleep(500);
        System.out.println("✅ Warmup completado (" + WARMUP_REQUESTS + " requests)");
    }

    @BeforeEach
    void givenSetup() {
        System.out.println("\n🔥 INICIANDO PRUEBA DE CARGA");
    }

    @Test
    @DisplayName("50 concurrent users should have at least 95% success rate")
    void givenFiftyConcurrentUsers_whenCallingEndpoint_thenAtLeast95PercentSucceed() throws InterruptedException, ExecutionException {
        var numberOfUsers = 50;

        var executor = Executors.newFixedThreadPool(numberOfUsers);

        var successCount = new AtomicInteger(0);
        var errorCount = new AtomicInteger(0);

        List<Future<Long>> futures = new ArrayList<>();

        System.out.println("👥 Simulando " + numberOfUsers + " usuarios concurrentes...");

        var testStartTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfUsers; i++) {
            final int userId = i + 1;

            Future<Long> future = executor.submit(() -> {
                var requestStart = System.currentTimeMillis();

                try {
                    given().when().get("/api/v1/clientes").then().statusCode(200);

                    successCount.incrementAndGet();

                    var requestEnd = System.currentTimeMillis();
                    return requestEnd - requestStart;

                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    System.err.println("❌ Usuario " + userId + " falló: " + e.getMessage());
                    return -1L;
                }
            });

            futures.add(future);
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        var testEndTime = System.currentTimeMillis();
        var totalDuration = testEndTime - testStartTime;

        long totalResponseTime = 0;
        long maxResponseTime = 0;
        long minResponseTime = Long.MAX_VALUE;

        for (Future<Long> future : futures) {
            var responseTime = future.get();
            if (responseTime > 0) {
                totalResponseTime += responseTime;
                maxResponseTime = Math.max(maxResponseTime, responseTime);
                minResponseTime = Math.min(minResponseTime, responseTime);
            }
        }

        var avgResponseTime = successCount.get() > 0 ? totalResponseTime / successCount.get() : 0;

        System.out.println("\n📊 RESULTADOS DE CARGA:");
        System.out.println("  👥 Usuarios simulados: " + numberOfUsers);
        System.out.println("  ✅ Peticiones exitosas: " + successCount.get());
        System.out.println("  ❌ Peticiones fallidas: " + errorCount.get());
        System.out.println("  ⏱ Tiempo total: " + totalDuration + "ms");
        System.out.println("  📈 Tiempo promedio: " + avgResponseTime + "ms");
        System.out.println("  ⚡ Tiempo mínimo: " + minResponseTime + "ms");
        System.out.println("  🐌 Tiempo máximo: " + maxResponseTime + "ms");
        System.out.println("  📊 Throughput: " + (numberOfUsers * 1000 / totalDuration) + " req/s");

        assertThat("Al menos 95% deben ser exitosas", successCount.get(), greaterThanOrEqualTo((int) (numberOfUsers * 0.95)));
        assertThat("Menos del 5% pueden fallar", errorCount.get(), lessThan(numberOfUsers / 20));
        assertThat("Tiempo promedio bajo carga < 500ms", avgResponseTime, lessThan(500L));

        System.out.println("✅ PRUEBA DE CARGA EXITOSA");
    }
}

