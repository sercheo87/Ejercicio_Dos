# 📝 Testing Cheat Sheet - Referencia Rápida

## 🎯 Tipos de Tests y Cuándo Usarlos

| Tipo | % Recomendado | Velocidad | Scope | Cuándo Usar |
|------|---------------|-----------|-------|-------------|
| **Unitarios** | 70% | ⚡⚡⚡ Muy rápido | Método/Clase | Lógica de negocio, cálculos, validaciones |
| **Integración** | 20% | ⚡⚡ Rápido | Múltiples capas | Controllers, DB access, APIs internas |
| **E2E** | 10% | ⚡ Lento | Sistema completo | Flujos críticos de negocio |

---

## 🧪 JUnit 5 - Annotations

```java
import org.junit.jupiter.api.*;

@DisplayName("Descripción legible del test")
class MiTest {
    
    @BeforeAll
    static void setupAll() {
        // Se ejecuta UNA VEZ antes de todos los tests
    }
    
    @BeforeEach
    void setup() {
        // Se ejecuta ANTES de cada test
    }
    
    @Test
    void miPrimerTest() {
        // Test individual
    }
    
    @RepeatedTest(5)
    void testRepetido() {
        // Se ejecuta 5 veces
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"valor1", "valor2"})
    void testParametrizado(String valor) {
        // Se ejecuta para cada valor
    }
    
    @Disabled("Razón por la que está deshabilitado")
    @Test
    void testDeshabilitado() {
        // No se ejecuta
    }
    
    @AfterEach
    void tearDown() {
        // Se ejecuta DESPUÉS de cada test
    }
    
    @AfterAll
    static void tearDownAll() {
        // Se ejecuta UNA VEZ después de todos los tests
    }
}
```

---

## ✅ Assertions Comunes

```java
import static org.junit.jupiter.api.Assertions.*;

// Igualdad
assertEquals(expected, actual);
assertEquals(expected, actual, "Mensaje si falla");

// No nulo
assertNotNull(objeto);
assertNull(objeto);

// Booleanos
assertTrue(condicion);
assertFalse(condicion);

// Excepciones
assertThrows(ExceptionType.class, () -> {
    // código que debe lanzar excepción
});

// Colecciones
assertIterableEquals(expectedList, actualList);

// Múltiples assertions
assertAll(
    () -> assertEquals(expected1, actual1),
    () -> assertEquals(expected2, actual2),
    () -> assertTrue(condition)
);

// Timeout
assertTimeout(Duration.ofSeconds(1), () -> {
    // código que no debe tardar más de 1 segundo
});
```

---

## 🎭 Mockito - Mocks y Verificaciones

### Setup

```java
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {
    
    @Mock
    private Repository repository; // Crea un mock
    
    @Spy
    private RealClass realClass; // Usa la implementación real pero permite stubbing
    
    @InjectMocks
    private ServiceImpl service; // Inyecta los mocks en el service
    
    @Captor
    private ArgumentCaptor<Entity> entityCaptor; // Captura argumentos
}
```

### Stubbing (When-Then)

```java
// Retornar un valor
when(repository.findById(1L)).thenReturn(Optional.of(entity));

// Retornar diferent values en llamadas sucesivas
when(repository.count()).thenReturn(5L).thenReturn(6L).thenReturn(7L);

// Lanzar excepción
when(repository.save(any())).thenThrow(new RuntimeException("Error"));

// Hacer algo cuando se llama al método
doAnswer(invocation -> {
    Entity arg = invocation.getArgument(0);
    arg.setId(1L);
    return arg;
}).when(repository).save(any(Entity.class));

// Para métodos void
doNothing().when(service).metodoVoid();
doThrow(new RuntimeException()).when(service).metodoVoid();

// Con ArgumentMatchers
when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
when(service.buscar(eq("nombre"), anyInt())).thenReturn(lista);
```

### Verification

```java
// Verificar que se llamó
verify(repository).save(any(Entity.class));

// Verificar número de veces
verify(repository, times(1)).save(any());
verify(repository, times(2)).findAll();
verify(repository, never()).delete(any());
verify(repository, atLeast(1)).count();
verify(repository, atMost(3)).findById(anyLong());

// Verificar orden de llamadas
InOrder inOrder = inOrder(repository, service);
inOrder.verify(repository).findById(1L);
inOrder.verify(service).process(any());

// Verificar que NO se llamaron otros métodos
verifyNoMoreInteractions(repository);

// Capturar argumentos
verify(repository).save(entityCaptor.capture());
Entity capturedEntity = entityCaptor.getValue();
assertEquals("valor esperado", capturedEntity.getNombre());
```

### ArgumentMatchers Útiles

```java
any()                  // Cualquier objeto
any(Class.class)       // Cualquier objeto de ese tipo
anyString()            // Cualquier String
anyInt(), anyLong()    // Cualquier número
anyList()              // Cualquier List
eq(value)              // Valor exacto (cuando mezclas matchers)
isNull()               // Argumento null
notNull()              // Argumento no null
contains("substring")  // String que contiene
startsWith("prefix")   // String que empieza con
matches("regex")       // String que coincide con regex
argThat(lambda)        // Matcher personalizado
```

---

## 🌐 MockMvc - Tests de Integración

### Setup

```java
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper; // Para JSON
}
```

### Requests

```java
// GET simple
mockMvc.perform(get("/api/v1/clientes"))
    .andExpect(status().isOk());

// GET con path variable
mockMvc.perform(get("/api/v1/clientes/{id}", 1L))
    .andExpect(status().isOk());

// GET con query params
mockMvc.perform(get("/api/v1/clientes/buscar")
        .param("nombre", "Juan")
        .param("activo", "true"))
    .andExpect(status().isOk());

// POST con JSON body
ClienteRequestDTO request = new ClienteRequestDTO(...);
mockMvc.perform(post("/api/v1/clientes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isCreated());

// PUT
mockMvc.perform(put("/api/v1/clientes/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonContent))
    .andExpect(status().isOk());

// DELETE
mockMvc.perform(delete("/api/v1/clientes/{id}", 1L))
    .andExpect(status().isNoContent());

// Con headers
mockMvc.perform(get("/api/v1/clientes")
        .header("Authorization", "Bearer token")
        .header("Accept-Language", "es"))
    .andExpect(status().isOk());
```

### Expectations

```java
// Status codes
.andExpect(status().isOk())                    // 200
.andExpect(status().isCreated())               // 201
.andExpect(status().isNoContent())             // 204
.andExpect(status().isBadRequest())            // 400
.andExpect(status().isNotFound())              // 404
.andExpect(status().isConflict())              // 409

// JSON Path
.andExpect(jsonPath("$.id").exists())
.andExpect(jsonPath("$.nombre").value("Juan"))
.andExpect(jsonPath("$.activo").value(true))
.andExpect(jsonPath("$.precio").value(99.99))
.andExpect(jsonPath("$[0].id").exists())       // Array
.andExpect(jsonPath("$", hasSize(3)))          // Tamaño array
.andExpect(jsonPath("$.nombre", containsString("Juan")))
.andExpect(jsonPath("$.email", not(emptyString())))

// Content
.andExpect(content().contentType(MediaType.APPLICATION_JSON))
.andExpect(content().string(containsString("texto")))

// Headers
.andExpect(header().string("Location", "/api/v1/clientes/1"))
.andExpect(header().exists("Content-Type"))
```

### Debugging

```java
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

mockMvc.perform(get("/api/v1/clientes"))
    .andDo(print())  // Imprime request y response
    .andExpect(status().isOk());
```

---

## 🚀 REST Assured - Tests E2E

### Setup

```java
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E2ETest {
    
    @LocalServerPort
    private int port;
    
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/clientes";
    }
}
```

### Given-When-Then

```java
// GET simple
given()
    .when()
        .get()
    .then()
        .statusCode(200)
        .body("size()", greaterThan(0));

// GET con path param
given()
        .pathParam("id", 1L)
    .when()
        .get("/{id}")
    .then()
        .statusCode(200)
        .body("nombre", equalTo("Juan"));

// GET con query params
given()
        .queryParam("nombre", "Juan")
        .queryParam("activo", true)
    .when()
        .get("/buscar")
    .then()
        .statusCode(200);

// POST con JSON
ClienteRequestDTO request = new ClienteRequestDTO(...);

Long id = ((Number) given()
        .contentType(ContentType.JSON)
        .body(request)
    .when()
        .post()
    .then()
        .statusCode(201)
        .body("nombre", equalTo("Juan"))
    .extract()
        .path("id")).longValue();

// PUT
given()
        .contentType(ContentType.JSON)
        .pathParam("id", id)
        .body(updateRequest)
    .when()
        .put("/{id}")
    .then()
        .statusCode(200);

// DELETE
given()
        .pathParam("id", id)
    .when()
        .delete("/{id}")
    .then()
        .statusCode(204);
```

### Hamcrest Matchers

```java
// Igualdad
.body("nombre", equalTo("Juan"))
.body("activo", is(true))
.body("precio", equalTo(99.99f))

// Comparación
.body("stock", greaterThan(0))
.body("stock", greaterThanOrEqualTo(5))
.body("stock", lessThan(100))

// Strings
.body("nombre", containsString("Juan"))
.body("email", startsWith("juan@"))
.body("email", endsWith(".com"))
.body("nombre", not(emptyString()))

// Null
.body("descripcion", nullValue())
.body("nombre", notNullValue())

// Colecciones
.body("$", hasSize(3))
.body("$", hasItem(hasEntry("nombre", "Juan")))
.body("nombre", hasItems("Juan", "Pedro"))
.body("$", everyItem(hasKey("id")))

// Lógicos
.body("activo", anyOf(equalTo(true), equalTo(false)))
.body("activo", allOf(notNullValue(), equalTo(true)))
```

---

## 📊 Patrones de Testing

### Patrón AAA (Arrange-Act-Assert)

```java
@Test
void debeCrearClienteCorrectamente() {
    // ARRANGE (Given) - Preparar
    ClienteRequestDTO request = new ClienteRequestDTO("Juan", "juan@test.com", "123");
    Cliente cliente = new Cliente();
    cliente.setId(1L);
    when(repository.save(any())).thenReturn(cliente);
    
    // ACT (When) - Ejecutar
    ClienteResponseDTO result = service.crear(request);
    
    // ASSERT (Then) - Verificar
    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(repository).save(any());
}
```

### Test Data Builders

```java
class ClienteTestDataBuilder {
    private String nombre = "Juan";
    private String email = "juan@test.com";
    
    public ClienteTestDataBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }
    
    public ClienteTestDataBuilder conEmail(String email) {
        this.email = email;
        return this;
    }
    
    public Cliente build() {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setEmail(email);
        return cliente;
    }
}

// Uso
@Test
void test() {
    Cliente cliente = new ClienteTestDataBuilder()
        .conNombre("Pedro")
        .conEmail("pedro@test.com")
        .build();
}
```

### Object Mother

```java
class ClienteMother {
    public static Cliente clienteValido() {
        return new Cliente("Juan", "juan@test.com", "123", true);
    }
    
    public static Cliente clienteInactivo() {
        Cliente cliente = clienteValido();
        cliente.setActivo(false);
        return cliente;
    }
    
    public static ClienteRequestDTO requestValido() {
        return new ClienteRequestDTO("Juan", "juan@test.com", "123");
    }
}

// Uso
@Test
void test() {
    Cliente cliente = ClienteMother.clienteValido();
}
```

---

## 🎯 Nomenclatura de Tests

### ✅ Buenas Prácticas

```java
// Patrón: debe[Acción]Cuando[Condición]
@Test
void debeCrearClienteCuandoDatosValidos()

@Test
void debeLanzarExcepcionCuandoNombreDuplicado()

@Test
void debeRetornarListaVaciaCuandoNoHayClientes()

// Patrón: [método]_[escenario]_[resultadoEsperado]
@Test
void crear_conNombreDuplicado_lanzaExcepcion()

@Test
void listar_conClientesActivos_retornaListaNoVacia()
```

### ❌ Malas Prácticas

```java
@Test
void test1() // No descriptivo

@Test
void testCliente() // Muy genérico

@Test
void crearCliente() // No indica qué espera

@Test
void testCrearClienteConNombreDuplicadoDeberiaLanzarUnaExcepcionDeClienteYaExiste() // Muy largo
```

---

## 🚫 Test Smells (Anti-patrones)

### 1. Test Interdependientes

❌ **Malo:**
```java
private static Long clienteId; // Estado compartido

@Test
@Order(1)
void crearCliente() {
    clienteId = service.crear(...).getId();
}

@Test
@Order(2)
void actualizarCliente() {
    service.actualizar(clienteId, ...); // Depende del test anterior
}
```

✅ **Bueno:**
```java
@Test
void crearYActualizarCliente() {
    // Un test auto-contenido
    Long id = service.crear(...).getId();
    service.actualizar(id, ...);
}
```

### 2. Test que Testea Demasiado

❌ **Malo:**
```java
@Test
void testTodo() {
    // crear
    // actualizar
    // eliminar
    // listar
    // buscar
    // ... demasiado en un solo test
}
```

✅ **Bueno:**
```java
@Test
void debeCrearCliente() { /* solo crear */ }

@Test
void debeActualizarCliente() { /* solo actualizar */ }
```

### 3. Test Flaky (Inconsistente)

❌ **Malo:**
```java
@Test
void test() {
    Thread.sleep(1000); // Depende del timing
    LocalDateTime now = LocalDateTime.now(); // Depende del reloj
    int random = new Random().nextInt(); // No determinista
}
```

✅ **Bueno:**
```java
@Test
void test() {
    Clock fixedClock = Clock.fixed(instant, ZoneId.systemDefault());
    when(clockService.now()).thenReturn(LocalDateTime.now(fixedClock));
}
```

---

## 📈 Coverage vs Quality

### Coverage NO es Calidad

```
100% Coverage ≠ Buenos Tests
```

✅ **Buen test** (aunque dé menos coverage):
```java
@Test
void debeCalcularDescuentoCorrectamentePara10Items() {
    // Testea un caso específico pero importante
    BigDecimal descuento = service.calcularDescuento(10, precioUnitario);
    assertEquals(new BigDecimal("0.10"), descuento);
}
```

❌ **Mal test** (aunque dé 100% coverage):
```java
@Test
void testCalcularDescuento() {
    service.calcularDescuento(1, BigDecimal.ONE); // No valida nada
}
```

### Qué Buscar

- **Mutation Testing**: ¿Los tests detectan cambios en el código?
- **Edge Cases**: ¿Testeas límites, nulls, vacíos?
- **Business Rules**: ¿Testeas las reglas de negocio clave?

---

## 🔧 Comandos Maven Útiles

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar un test específico
mvn test -Dtest=ClienteServiceTest

# Ejecutar tests de una clase con patrón
mvn test -Dtest=*ServiceTest

# Skip tests
mvn clean install -DskipTests

# Ejecutar solo tests unitarios (convención de nombre)
mvn test -Dtest=*Test

# Ejecutar solo tests de integración (convención de nombre)
mvn test -Dtest=*IT

# Ver report de coverage (con Jacoco)
mvn clean test jacoco:report
# Ver: target/site/jacoco/index.html

# Ejecutar tests en paralelo
mvn test -T 4 # 4 threads

# Debug de tests
mvnDebug test
```

---

## 🎓 Recursos de Aprendizaje

### Libros
- "Test-Driven Development by Example" - Kent Beck
- "Growing Object-Oriented Software, Guided by Tests" - Freeman & Pryce
- "xUnit Test Patterns" - Gerard Meszaros

### Online
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [REST Assured](https://rest-assured.io/)
- [Baeldung Testing Tutorials](https://www.baeldung.com/spring-boot-testing)
- [Test Pyramid - Martin Fowler](https://martinfowler.com/articles/practical-test-pyramid.html)

---

## ✅ Checklist Rápido

Antes de hacer commit de tus tests:

- [ ] ¿Todos los tests pasan?
- [ ] ¿Los nombres son descriptivos?
- [ ] ¿Cada test verifica UNA cosa?
- [ ] ¿No hay lógica compleja en los tests?
- [ ] ¿No hay dependencias entre tests?
- [ ] ¿Usas assertions significativos?
- [ ] ¿Verificas las interacciones importantes?
- [ ] ¿Testeas casos de error?
- [ ] ¿Los tests son rápidos?
- [ ] ¿El código de producción es testeable?

---

**Happy Testing!** 🚀
