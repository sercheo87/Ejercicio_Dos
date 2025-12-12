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

    @Test
    @DisplayName("100 rapid requests should complete in less than 10 seconds with less than 10% errors")
    void givenHundredRapidRequests_whenStressTesting_thenCompletesWithAcceptableMetrics() {
        var numberOfRequests = 100;

        var executor = Executors.newFixedThreadPool(20);
        List<Future<Long>> futures = new ArrayList<>();

        System.out.println("⚡ PRUEBA DE ESTRÉS: " + numberOfRequests + " peticiones...");

        var startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfRequests; i++) {
            Future<Long> future = executor.submit(() -> {
                var reqStart = System.currentTimeMillis();

                given()
                    .when()
                    .get("/api/v1/clientes")
                    .then()
                    .statusCode(200);

                var reqEnd = System.currentTimeMillis();
                return reqEnd - reqStart;
            });

            futures.add(future);
        }

        List<Long> responseTimes = new ArrayList<>();
        var errors = 0;

        for (Future<Long> future : futures) {
            try {
                responseTimes.add(future.get());
            } catch (Exception e) {
                errors++;
            }
        }

        executor.shutdown();
        var endTime = System.currentTimeMillis();
        var totalTime = endTime - startTime;

        var minTime = responseTimes.stream().min(Long::compare).orElse(0L);
        var maxTime = responseTimes.stream().max(Long::compare).orElse(0L);
        var avgTime = responseTimes.stream().mapToLong(Long::longValue).sum() / responseTimes.size();

        System.out.println("\n📊 ESTADÍSTICAS DE ESTRÉS:");
        System.out.println("  ⏱ Tiempo mínimo: " + minTime + "ms");
        System.out.println("  ⏱ Tiempo máximo: " + maxTime + "ms");
        System.out.println("  ⏱ Tiempo promedio: " + avgTime + "ms");
        System.out.println("  ⏱ Tiempo total: " + totalTime + "ms");
        System.out.println("  ❌ Errores: " + errors);
        System.out.println("  📊 Throughput: " + (numberOfRequests * 1000 / totalTime) + " req/s");

        assertThat("Debe completarse en < 10 segundos", totalTime, lessThan(10000L));
        assertThat("Máximo 10% de errores", errors, lessThan(numberOfRequests / 10));
        assertThat("Tiempo promedio bajo estrés < 500ms", avgTime, lessThan(500L));

        System.out.println("✅ Sistema resistió el estrés");
    }

    @Test
    @DisplayName("Performance degradation under increasing load should be less than 200%")
    void givenIncreasingLoad_whenMeasuringDegradation_thenDegradationIsAcceptable() throws InterruptedException {
        System.out.println("📈 PRUEBA DE DEGRADACIÓN...");

        int[] loadLevels = {1, 5, 10, 25, 50};
        var iterationsPerLevel = 5;
        List<Long> avgResponseTimes = new ArrayList<>();

        for (int users : loadLevels) {
            List<Long> levelTimes = new ArrayList<>();

            for (int iteration = 0; iteration < iterationsPerLevel; iteration++) {
                var latch = new CountDownLatch(users);
                var executor = Executors.newFixedThreadPool(users);
                List<Long> times = new ArrayList<>();

                for (int i = 0; i < users; i++) {
                    executor.submit(() -> {
                        try {
                            var start = System.currentTimeMillis();
                            given()
                                .when()
                                .get("/api/v1/clientes")
                                .then()
                                .statusCode(200);

                            var end = System.currentTimeMillis();
                            synchronized (times) {
                                times.add(end - start);
                            }
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await(30, TimeUnit.SECONDS);
                executor.shutdown();

                if (!times.isEmpty()) {
                    var iterationAvg = times.stream().mapToLong(Long::longValue).sum() / times.size();
                    levelTimes.add(iterationAvg);
                }
            }

            levelTimes.sort(Long::compareTo);
            var trimmedTimes = levelTimes.subList(1, levelTimes.size() - 1);
            var avgTime = trimmedTimes.stream().mapToLong(Long::longValue).sum() / trimmedTimes.size();
            avgResponseTimes.add(avgTime);

            System.out.println("  👥 " + users + " usuarios → ⏱ " + avgTime + "ms promedio");
        }

        var baselineTime = Math.max(avgResponseTimes.get(0), 5L);
        var highLoadTime = avgResponseTimes.get(avgResponseTimes.size() - 1);

        var degradation = ((highLoadTime - baselineTime) * 100.0) / baselineTime;

        System.out.println("\n📊 ANÁLISIS:");
        System.out.println("  📏 Baseline (1 usuario): " + baselineTime + "ms");
        System.out.println("  🔥 Alta carga (50 usuarios): " + highLoadTime + "ms");
        System.out.println("  📈 Degradación: " + String.format("%.2f", degradation) + "%");

        assertThat("Degradación debe ser < 200%", degradation, lessThan(200.0));

        System.out.println("✅ Degradación aceptable");
    }

}
