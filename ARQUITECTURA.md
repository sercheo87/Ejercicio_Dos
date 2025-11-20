# Arquitectura en Capas Tradicional

Este proyecto sigue una **arquitectura en capas tradicional** (Layered Architecture), organizando el código en capas bien definidas con responsabilidades claras.

## 📋 Estructura del Proyecto

```
src/main/java/com/example/demo/
│
├── DemoApplication.java              # Clase principal de Spring Boot
│
├── controller/                        # 🎮 CAPA DE PRESENTACIÓN
│   └── ClienteController.java        # Controlador REST que expone endpoints HTTP
│
├── service/                          # 💼 CAPA DE LÓGICA DE NEGOCIO
│   ├── ClienteService.java          # Interfaz del servicio (Contrato)
│   └── impl/
│       └── ClienteServiceImpl.java  # Implementación con la lógica de negocio
│
├── repository/                       # 💾 CAPA DE ACCESO A DATOS
│   └── ClienteRepository.java       # Repositorio JPA para persistencia
│
├── model/                            # 📦 CAPA DE MODELO
│   ├── entity/                       # Entidades JPA (Dominio)
│   │   └── Cliente.java             # Entidad Cliente (tabla en BD)
│   └── dto/                          # Data Transfer Objects
│       ├── ClienteRequestDTO.java   # DTO para requests
│       └── ClienteResponseDTO.java  # DTO para responses
│
├── mapper/                           # 🔄 UTILIDADES DE CONVERSIÓN
│   └── ClienteMapper.java           # Mapea entre DTOs y Entidades
│
├── exception/                        # ⚠️ MANEJO DE EXCEPCIONES
│   ├── ClienteNotFoundException.java
│   ├── ClienteAlreadyExistsException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
└── config/                           # ⚙️ CONFIGURACIONES
    (Listo para futuras configuraciones)
```

## 🏗️ Descripción de las Capas

### 1️⃣ Capa de Presentación (Controller)
**Responsabilidad**: Manejar las peticiones HTTP y respuestas

- **Archivos**: `ClienteController.java`
- **Tecnologías**: `@RestController`, `@RequestMapping`
- **Funciones**:
  - Recibe peticiones HTTP (GET, POST, PUT, DELETE)
  - Valida datos de entrada con `@Valid`
  - Delega la lógica al servicio
  - Retorna respuestas HTTP apropiadas

**Ejemplo**:
```java
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
    private final ClienteService service;
    
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.status(CREATED).body(service.crear(dto));
    }
}
```

### 2️⃣ Capa de Lógica de Negocio (Service)
**Responsabilidad**: Contener toda la lógica de negocio y reglas del dominio

- **Archivos**: 
  - `ClienteService.java` (Interfaz)
  - `ClienteServiceImpl.java` (Implementación)
- **Tecnologías**: `@Service`, `@Transactional`
- **Funciones**:
  - Validaciones de negocio
  - Orquestación de operaciones
  - Transformación de datos (vía Mapper)
  - Gestión de transacciones

**Patrón Interface + Implementación**:
```java
// Interfaz (Contrato)
public interface ClienteService {
    ClienteResponseDTO crear(ClienteRequestDTO dto);
    ClienteResponseDTO obtenerPorId(Long id);
    // ... más métodos
}

// Implementación
@Service
public class ClienteServiceImpl implements ClienteService {
    @Override
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        // Lógica de negocio aquí
    }
}
```

### 3️⃣ Capa de Acceso a Datos (Repository)
**Responsabilidad**: Interactuar con la base de datos

- **Archivos**: `ClienteRepository.java`
- **Tecnologías**: Spring Data JPA, `@Repository`
- **Funciones**:
  - CRUD básico (heredado de JpaRepository)
  - Consultas personalizadas
  - Abstracción de la persistencia

**Ejemplo**:
```java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Cliente> findByActivo(Boolean activo);
}
```

### 4️⃣ Capa de Modelo (Model)
**Responsabilidad**: Definir las estructuras de datos

#### Entidades (Entity)
- Representan las tablas de la base de datos
- Anotaciones JPA: `@Entity`, `@Table`, `@Column`
- Contienen validaciones de campo

#### DTOs (Data Transfer Objects)
- **Request DTOs**: Datos que llegan del cliente
- **Response DTOs**: Datos que se envían al cliente
- Desacoplan la API de la estructura interna de datos

### 5️⃣ Mapper (Conversión)
**Responsabilidad**: Convertir entre DTOs y Entidades

- **Archivos**: `ClienteMapper.java`
- **Patrón**: Mapper/Converter
- **Funciones**:
  - `toEntity()`: DTO → Entidad
  - `toResponseDTO()`: Entidad → DTO
  - `toResponseDTOList()`: Lista conversión

### 6️⃣ Exception (Manejo de Excepciones)
**Responsabilidad**: Gestión centralizada de errores

- **Excepciones personalizadas**:
  - `ClienteNotFoundException`
  - `ClienteAlreadyExistsException`
- **GlobalExceptionHandler**: Captura y formatea errores
- **ErrorResponse**: Formato estándar de respuesta de error

## 🔄 Flujo de Datos

```
┌─────────────┐
│   Cliente   │ HTTP Request (JSON)
└──────┬──────┘
       ↓
┌──────────────────────┐
│  1. Controller       │ @RestController
│  ClienteController   │ Recibe y valida request
└──────┬───────────────┘
       ↓
┌──────────────────────┐
│  2. Service          │ @Service
│  ClienteServiceImpl  │ Lógica de negocio
└──────┬───────────────┘
       ↓
┌──────────────────────┐
│  3. Mapper           │ @Component
│  ClienteMapper       │ DTO ↔ Entity
└──────┬───────────────┘
       ↓
┌──────────────────────┐
│  4. Repository       │ @Repository
│  ClienteRepository   │ Acceso a datos
└──────┬───────────────┘
       ↓
┌──────────────────────┐
│  5. Base de Datos    │ H2 / PostgreSQL
│  Tabla: clientes     │
└──────────────────────┘
```

## ✅ Ventajas de esta Arquitectura

### 1. **Separación de Responsabilidades**
Cada capa tiene una función específica y bien definida.

### 2. **Mantenibilidad**
Es fácil localizar y modificar código porque está organizado lógicamente.

### 3. **Testabilidad**
Cada capa puede probarse de forma independiente:
- **Controller**: Tests de integración con MockMvc
- **Service**: Tests unitarios con Mockito
- **Repository**: Tests con base de datos en memoria

### 4. **Escalabilidad**
Fácil agregar nuevas funcionalidades sin afectar capas existentes.

### 5. **Desacoplamiento**
El uso de interfaces (Service) y DTOs reduce el acoplamiento entre capas.

## 📝 Principios Aplicados

### SOLID
- **S** - Single Responsibility: Cada clase tiene una única responsabilidad
- **D** - Dependency Inversion: Controller depende de la interfaz Service, no de la implementación

### DTO Pattern
- Evita exponer entidades directamente
- Control sobre qué datos se envían/reciben
- Validaciones específicas por operación

### Repository Pattern
- Abstracción del acceso a datos
- Facilita cambiar la implementación de persistencia

## 🎯 Testing por Capas

### Tests Unitarios (Service)
```java
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock private ClienteRepository repository;
    @Mock private ClienteMapper mapper;
    @InjectMocks private ClienteServiceImpl service;
}
```

### Tests de Integración (Controller)
```java
@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerIT {
    @Autowired private MockMvc mockMvc;
}
```

### Tests E2E
```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ClienteE2ETest {
    // REST Assured
}
```

## 🚀 Cómo Agregar Nueva Funcionalidad

### Ejemplo: Agregar módulo de "Productos"

1. **Entity**: Crear `model/entity/Producto.java`
2. **DTOs**: Crear `model/dto/ProductoRequestDTO.java` y `ProductoResponseDTO.java`
3. **Repository**: Crear `repository/ProductoRepository.java`
4. **Mapper**: Crear `mapper/ProductoMapper.java`
5. **Service**: Crear interfaz `service/ProductoService.java` e implementación `service/impl/ProductoServiceImpl.java`
6. **Controller**: Crear `controller/ProductoController.java`
7. **Exceptions**: Si es necesario, crear excepciones específicas
8. **Tests**: Agregar tests en cada nivel de la pirámide

## 📚 Referencias

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Martin Fowler - Application Architecture Patterns](https://martinfowler.com/eaaCatalog/)
- [Layered Architecture Pattern](https://en.wikipedia.org/wiki/Multitier_architecture)
