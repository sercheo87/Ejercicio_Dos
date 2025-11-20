# � Spring Boot Testing Pyramid - Proyecto Base para Taller

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-Project-8dd6f9)
![Testing](https://img.shields.io/badge/Testing-Pyramid-success)
![Architecture](https://img.shields.io/badge/Architecture-Layered-blueviolet)
![Workshop](https://img.shields.io/badge/Workshop-90%20min-yellow)

## 📋 Descripción

**Proyecto base para taller virtual de 90 minutos** sobre Testing Pyramid en Spring Boot. Implementa arquitectura en capas tradicional con API REST para gestión de clientes y suite completa de tests en todos los niveles.

### 🎯 Ideal para:
- ✅ **Talleres virtuales de testing** (50+ personas)
- ✅ **Práctica guiada** de JUnit 5, Mockito, MockMvc y REST Assured
- ✅ **Aprendizaje** de la Pirámide de Testing (70% Unit, 20% Integration, 10% E2E)
- ✅ **Ejemplo real** de arquitectura en capas con Spring Boot

## 🎯 Características del Proyecto

### Arquitectura y Código
- ✅ **Arquitectura en Capas Tradicional** (Controller → Service → Repository)
- ✅ **API REST** completa con CRUD de clientes
- ✅ **Persistencia** con JPA y base de datos H2
- ✅ **Patrón Service Interface + Implementation**
- ✅ **DTOs** para desacoplar API de modelo de dominio
- ✅ **Validaciones** con Bean Validation
- ✅ **Manejo de Excepciones** centralizado
- ✅ **36 tests** ya funcionando (Unit + Integration + E2E)

### Material del Taller
- 📚 **TALLER_VIRTUAL_90MIN.md** - Plan completo minuto a minuto
- 📝 **GUIA_PRACTICA.md** - Ejercicios paso a paso con TODOs
- 🔖 **TESTING_CHEATSHEET.md** - Referencia rápida de sintaxis
- 📖 **ARQUITECTURA.md** - Documentación de arquitectura
- 🎯 **REESTRUCTURACION.md** - Cómo se organizó el proyecto

## 🏗️ Arquitectura del Proyecto

### Estructura de Capas

```
src/main/java/com/example/demo/
│
├── DemoApplication.java              # Clase principal de Spring Boot
│
├── controller/                        # 🎮 CAPA DE PRESENTACIÓN
│   └── ClienteController.java        # Controlador REST - Endpoints HTTP
│
├── service/                          # 💼 CAPA DE LÓGICA DE NEGOCIO
│   ├── ClienteService.java          # Interfaz del servicio
│   └── impl/
│       └── ClienteServiceImpl.java  # Implementación con lógica de negocio
│
├── repository/                       # 💾 CAPA DE ACCESO A DATOS
│   └── ClienteRepository.java       # Repositorio JPA
│
├── model/                            # 📦 CAPA DE MODELO
│   ├── entity/                       # Entidades de dominio
│   │   └── Cliente.java             # Entidad JPA (tabla clientes)
│   └── dto/                          # Data Transfer Objects
│       ├── ClienteRequestDTO.java   # DTO para requests
│       └── ClienteResponseDTO.java  # DTO para responses
│
├── mapper/                           # 🔄 CONVERSIÓN DE DATOS
│   └── ClienteMapper.java           # Convierte entre DTOs y Entidades
│
├── exception/                        # ⚠️ MANEJO DE EXCEPCIONES
│   ├── ClienteNotFoundException.java
│   ├── ClienteAlreadyExistsException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
└── config/                           # ⚙️ CONFIGURACIONES
    (Preparado para configuraciones futuras)
```

### Flujo de Datos por Capas

```
HTTP Request → Controller → Service → Repository → Database
                   ↓           ↓          ↓
                  DTO    →   Entity  →  Table
```

Ver [ARQUITECTURA.md](ARQUITECTURA.md) para documentación detallada de la arquitectura.

## 🚀 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 3.2.0 | Framework principal |
| Spring Data JPA | 3.2.0 | Capa de persistencia |
| H2 Database | Runtime | Base de datos en memoria |
| Lombok | Latest | Reducir boilerplate |
| Bean Validation | 3.2.0 | Validaciones |
| JUnit 5 | 5.10.x | Testing unitario |
| Mockito | 5.x | Mocking en tests |
| REST Assured | 5.3.0 | Tests E2E |
| Gradle | 8.x (o Gradle Wrapper) | Gestión de dependencias |

## 🎓 Guía Rápida para Instructores

### Setup del Taller (15 min antes)

1. **Compartir con participantes:**
```bash
# Link del repositorio
https://github.com/[tu-repo]/springboot-testing-pyramid

# Instrucciones pre-taller
- Instalar JDK 17+
- Instalar Maven 3.8+
- Clonar el repositorio
- Ejecutar: mvn clean install
```

2. **Preparar ambiente:**
- Abrir Zoom/Teams con grabación habilitada
- Tener proyecto abierto en IDE
- Slides listos
- Chat/Slack para preguntas

### Estructura del Taller (90 min)

```
⏰ 00:00-10:00  │ Introducción + Verificación de Setup
⏰ 10:00-30:00  │ Teoría: Pirámide de Testing + Herramientas
⏰ 30:00-55:00  │ Práctica: Tests Unitarios (5 tests con Mockito)
⏰ 55:00-70:00  │ Práctica: Tests Integración (3 tests con MockMvc)
⏰ 70:00-80:00  │ Práctica: Tests E2E (2 tests con REST Assured)
⏰ 80:00-90:00  │ Q&A + Cierre + Desafío opcional
```

**📋 Ver plan detallado:** [TALLER_VIRTUAL_90MIN.md](TALLER_VIRTUAL_90MIN.md)

---

## 👨‍💻 Guía Rápida para Participantes

### Setup Rápido (5 min)

```bash
# 1. Clonar repositorio
git clone https://github.com/[tu-repo]/springboot-testing-pyramid.git
cd springboot-testing-pyramid

# 2. Compilar
mvn clean install

# 3. Ejecutar aplicación
mvn spring-boot:run

# 4. Verificar en navegador
http://localhost:8080/api/v1/clientes
```

### Durante el Taller

Sigue la guía paso a paso: **[GUIA_PRACTICA.md](GUIA_PRACTICA.md)**

Tendrás que completar:
- ✅ **5 Tests Unitarios** (25 min) - ClienteServiceTest.java
- ✅ **3 Tests de Integración** (15 min) - ClienteControllerIT.java
- ✅ **2 Tests E2E** (10 min) - ClienteE2ETest.java

### Referencia Rápida

Durante el taller usa: **[TESTING_CHEATSHEET.md](TESTING_CHEATSHEET.md)**

---

## 📦 Instalación y Ejecución

### Prerrequisitos

- ✅ Java JDK 17 o 21
- ✅ Gradle 8.x o usar el Gradle Wrapper (`./gradlew`)
- ✅ IDE (IntelliJ IDEA / VS Code / Eclipse)
- ✅ Git


### Pasos para ejecutar

1. **Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd springboot-testing-pyramid
```

2. **Compilar el proyecto**
```bash
# Si está disponible el wrapper (recomendado)
./gradlew clean build

# O con Gradle instalado globalmente
gradle clean build
```

3. **Ejecutar la aplicación**
```bash
# Con wrapper
./gradlew bootRun

# Con Gradle instalado
gradle bootRun
```

4. **La aplicación estará disponible en:**
```
http://localhost:8080
```

5. **Acceder a la consola H2** (para ver la base de datos)
```
http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:clientesdb
Usuario: sa
Password: (dejar en blanco)
```

### Ejecutar Tests

### Verificación rápida (para participantes)
```bash
# Ejecutar TODOS los tests (con wrapper si está disponible)
./gradlew clean test

# Resultado esperado:
# Tests run: 36, Failures: 0, Errors: 0, Skipped: 0 ✅
```

### Tests por tipo
```bash
# Solo tests unitarios
./gradlew test --tests *ClienteServiceTest*

# Solo tests de integración (tarea custom)
./gradlew integrationTest

# Solo tests E2E (tarea custom)
./gradlew e2eTest
```

### Distribución de Tests (36 total)

```
✅ ClienteServiceTest (Unitarios):      11 tests
✅ ClienteControllerIT (Integración):   11 tests  
✅ ClienteE2ETest (E2E):                14 tests
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
   TOTAL:                               36 tests
```

## 📊 Pirámide de Testing (Material del Taller)

```
         /\         
        /  \        E2E Tests (10%)
       / E2E\       - 2 tests en el taller
      /------\      - Flujos completos
     /        \     - REST Assured
    /   IT     \    
   / Integration\   Integration Tests (20%)
  /--------------\  - 3 tests en el taller
 /                \ - MockMvc + Spring Context
/   Unit Tests    \ 
/________________ \ Unit Tests (70%)
                    - 5 tests en el taller
                    - Mockito + JUnit 5
```

### Durante el Taller Crearás:

**1️⃣ Tests Unitarios (25 min)**
- Archivo: `ClienteServiceTest.java`
- Herramientas: JUnit 5 + Mockito
- Objetivo: Testear lógica de negocio aislada
- Tests a crear: 5

**2️⃣ Tests de Integración (15 min)**
- Archivo: `ClienteControllerIT.java`
- Herramientas: MockMvc + @SpringBootTest
- Objetivo: Testear Controller + Service + Repository
- Tests a crear: 3

**3️⃣ Tests E2E (10 min)**
- Archivo: `ClienteE2ETest.java`
- Herramientas: REST Assured
- Objetivo: Testear flujos completos como cliente real
- Tests a crear: 2

**📖 Ver teoría completa:** [TALLER_VIRTUAL_90MIN.md - Bloque 2](TALLER_VIRTUAL_90MIN.md#-bloque-2-teor%C3%ADa---pir%C3%A1mide-de-testing-20-min)

## 📡 API Endpoints

### Base URL: `/api/v1/clientes`

| Método | Endpoint | Descripción | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/health` | Health check | - | String |
| POST | `/` | Crear cliente | ClienteRequestDTO | ClienteResponseDTO (201) |
| GET | `/` | Listar clientes activos | - | List\<ClienteResponseDTO\> |
| GET | `/{id}` | Obtener por ID | - | ClienteResponseDTO |
| GET | `/buscar?nombre=` | Buscar por nombre | - | ClienteResponseDTO |
| PUT | `/{id}` | Actualizar cliente | ClienteRequestDTO | ClienteResponseDTO |
| DELETE | `/{id}` | Eliminar cliente | - | 204 No Content |

### Ejemplos de uso con cURL

#### Crear un cliente
```bash
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "telefono": "0991234567"
  }'
```

#### Listar todos los clientes
```bash
curl http://localhost:8080/api/v1/clientes
```

#### Obtener un cliente por ID
```bash
curl http://localhost:8080/api/v1/clientes/1
```

#### Buscar cliente por nombre
```bash
curl "http://localhost:8080/api/v1/clientes/buscar?nombre=Juan%20Pérez"
```

#### Actualizar un cliente
```bash
curl -X PUT http://localhost:8080/api/v1/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez Actualizado",
    "email": "juan.nuevo@example.com",
    "telefono": "0997654321"
  }'
```

#### Eliminar un cliente
```bash
curl -X DELETE http://localhost:8080/api/v1/clientes/1
```

## 🔒 Validaciones

El sistema implementa las siguientes validaciones:

- **Nombre:**
  - No puede estar vacío
  - Mínimo 2 caracteres
  - Máximo 100 caracteres
  - Debe ser único

- **Email:**
  - Debe ser un email válido
  - Único en el sistema

- **Teléfono:**
  - Máximo 15 caracteres

## 🎯 Manejo de Errores

La API retorna respuestas estructuradas para todos los errores:

```json
{
  "timestamp": "2025-11-19T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado con id: 999",
  "path": "/api/v1/clientes/999"
}
```

### Códigos de estado HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Cliente creado
- **204 No Content**: Cliente eliminado
- **400 Bad Request**: Datos inválidos
- **404 Not Found**: Cliente no encontrado
- **409 Conflict**: Cliente duplicado
- **500 Internal Server Error**: Error del servidor

## 📈 Desafío Opcional (Después del Taller)

¿Quieres seguir practicando? Crea tu propio módulo con tests completos:

### Opción 1: Módulo de Productos 🛒
```java
Producto {
    Long id;
    String nombre;
    String descripcion;
    BigDecimal precio;
    Integer stock;
    Boolean activo;
}
```

### Opción 2: Módulo de Órdenes 📦
```java
Orden {
    Long id;
    String numeroOrden;
    LocalDate fechaCreacion;
    BigDecimal total;
    String estado; // PENDIENTE, PROCESADA, CANCELADA
}
```

### Opción 3: Módulo de Usuarios 👤
```java
Usuario {
    Long id;
    String username;
    String email;
    String rol; // ADMIN, USER
    Boolean activo;
}
```

### Requisitos del Desafío

✅ Implementar arquitectura en capas completa
✅ Mínimo 5 tests unitarios
✅ Mínimo 3 tests de integración
✅ Mínimo 1 test E2E
✅ Seguir convenciones del proyecto base
✅ Todos los tests deben pasar

**🏆 Comparte tu solución:** Crea un fork del repo y comparte tu módulo

---

## 📚 Documentación y Recursos

### Material del Taller

| Documento | Descripción | Para quién |
|-----------|-------------|------------|
| [TALLER_VIRTUAL_90MIN.md](TALLER_VIRTUAL_90MIN.md) | Plan completo minuto a minuto | Instructores |
| [GUIA_PRACTICA.md](GUIA_PRACTICA.md) | Ejercicios paso a paso | Participantes |
| [TESTING_CHEATSHEET.md](TESTING_CHEATSHEET.md) | Referencia rápida sintaxis | Todos |
| [ARQUITECTURA.md](ARQUITECTURA.md) | Arquitectura en capas | Todos |
| [REESTRUCTURACION.md](REESTRUCTURACION.md) | Cómo se organizó el proyecto | Referencia |

### Recursos Externos

**Testing:**
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [REST Assured](https://rest-assured.io/)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/reference/testing/index.html)

**Conceptos:**
- [Test Pyramid - Martin Fowler](https://martinfowler.com/articles/practical-test-pyramid.html)
- [Test Doubles - Martin Fowler](https://martinfowler.com/bliki/TestDouble.html)

**Videos:**
- [Testing Spring Boot Applications](https://spring.io/guides/gs/testing-web/)
- [Mockito Tutorial](https://www.baeldung.com/mockito-series)

---

## 🎯 FAQ - Preguntas Frecuentes

### Para Participantes

**P: ¿Necesito experiencia previa con testing?**
R: No. El taller empieza desde cero. Solo necesitas conocimientos básicos de Java y Spring Boot.

**P: ¿Qué pasa si me quedo atrás durante el taller?**
R: Tranquilo! Tienes:
- ✅ La guía completa (GUIA_PRACTICA.md)
- ✅ El código de solución en branch `solucion`
- ✅ Grabación del taller
- ✅ Cheat sheet de referencia

**P: ¿Puedo hacer el taller a mi ritmo?**
R: ¡Absolutamente! El material está diseñado para:
- Taller guiado de 90 min (modalidad virtual)
- Estudio individual (a tu ritmo)

**P: No puedo ejecutar los tests, ¿qué hago?**
R: Verifica:
1. Java 17+ instalado: `java -version`
2. Maven 3.8+: `mvn -version`
3. Recompila: `mvn clean install`
4. Si persiste: revisa [GUIA_PRACTICA.md - FAQ](GUIA_PRACTICA.md#-preguntas-frecuentes)

### Para Instructores

**P: ¿Cuántas personas pueden participar?**
R: El taller está optimizado para **50 personas virtuales**, pero puede escalar hasta 100 con:
- Co-instructor para soporte
- Moderador de chat
- Breakout rooms opcionales para Q&A

**P: ¿Necesito adaptar el material?**
R: El material está listo para usar "as-is", pero puedes:
- Ajustar tiempos según tu audiencia
- Agregar/quitar ejercicios
- Personalizar con tu branding

**P: ¿Qué herramientas necesito para dar el taller?**
R: Mínimo:
- Zoom/Teams/Google Meet
- Compartir pantalla (dual screen recomendado)
- IDE con proyecto abierto
- Slides de teoría

Opcional:
- Postman para demos
- Miro/Mural para colaboración
- Slack/Discord para Q&A asíncrono

---

## 🛠️ Troubleshooting

### Error: Puerto 8080 en uso

```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID [número] /F

# Mac/Linux
lsof -ti:8080 | xargs kill -9

# O cambiar puerto en application.properties:
server.port=8081
```

### Error: Tests fallan con NullPointerException

Verifica que tengas las anotaciones correctas:
```java
@ExtendWith(MockitoExtension.class)
class MiTest {
    @Mock
    private Repository repository;
    
    @InjectMocks
    private ServiceImpl service;
}
```

### Error: Maven no encuentra dependencias

```bash
# Limpiar caché de Maven
mvn dependency:purge-local-repository

# Forzar actualización
mvn clean install -U
```

---

## � Métricas de Éxito del Taller

### Durante el Taller
- ✅ 80%+ completan los 5 tests unitarios
- ✅ 70%+ completan los 3 tests de integración
- ✅ 60%+ completan los 2 tests E2E
- ✅ 90%+ ejecutan `mvn test` exitosamente

### Post-Taller
- ✅ 60%+ completan el desafío opcional
- ✅ Feedback promedio: 4+ estrellas de 5
- ✅ 70%+ reportan que aplicarán lo aprendido

---

## 🤝 Contribuciones

¿Mejoraste el material del taller? ¡Compártelo!

1. Fork el proyecto
2. Crea tu rama (`git checkout -b feature/MejoraMaterial`)
3. Commit cambios (`git commit -m 'Agrego ejercicio X'`)
4. Push a la rama (`git push origin feature/MejoraMaterial`)
5. Abre un Pull Request

**Ideas de contribución:**
- Ejercicios adicionales
- Traducciones
- Diagramas/visualizaciones
- Videos tutoriales
- Slides mejorados

---

## 📧 Contacto y Soporte

**Para Instructores:**
- 📧 Email: [tu-email]@empresa.com
- 💼 LinkedIn: [tu-perfil]
- 🐙 GitHub: Abre un issue

**Para Participantes:**
- 💬 Durante el taller: Chat de Zoom/Teams
- 🐙 Después del taller: GitHub Issues
- 📚 Documentación: Revisa primero el FAQ

---

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## ⭐ Agradecimientos

Si este material te fue útil:
- ⭐ Dale una estrella al repositorio
- 🔄 Compártelo con tu equipo
- 📝 Deja feedback en las Issues
- 🤝 Contribuye con mejoras

---

**¡Feliz Testing! 🧪🚀**

*"Código sin tests es código legacy desde el día 1"*
