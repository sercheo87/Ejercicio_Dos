# Configuración de Java para el Proyecto

## ✅ Estado Actual

Tu proyecto ahora está configurado para usar **Java 21** ☕

## 📋 Versiones de Java Instaladas

Tienes las siguientes versiones de Java instaladas vía Homebrew:
- ✅ OpenJDK 17
- ✅ OpenJDK 21 (Actualmente en uso)

## 🔄 Cambiar entre Versiones de Java

### Opción 1: Cambio Temporal (Solo para la sesión actual)

#### Para usar Java 21:
```bash
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
java -version
```

#### Para usar Java 17:
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
java -version
```

### Opción 2: Cambio Permanente (Recomendado)

Agrega una de estas configuraciones a tu archivo `~/.zshrc`:

#### Para Java 21 por defecto:
```bash
# Java 21 Configuration
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
```

#### Para Java 17 por defecto:
```bash
# Java 17 Configuration
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
```

Después de editar el archivo, ejecuta:
```bash
source ~/.zshrc
```

### Opción 3: Usar jenv (Gestión Avanzada)

Si quieres cambiar fácilmente entre versiones, puedes instalar `jenv`:

```bash
# Instalar jenv
brew install jenv

# Agregar a tu .zshrc
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
echo 'eval "$(jenv init -)"' >> ~/.zshrc
source ~/.zshrc

# Agregar las versiones de Java a jenv
jenv add /opt/homebrew/opt/openjdk@17
jenv add /opt/homebrew/opt/openjdk@21

# Ver versiones disponibles
jenv versions

# Configurar Java 21 globalmente
jenv global 21

# Configurar Java 21 solo para este proyecto
cd /path/to/your/project
jenv local 21
```

## 🚀 Comandos de Maven

### Compilar el proyecto:
```bash
mvn clean compile
```

### Ejecutar tests:
```bash
# Todos los tests
mvn test

# Solo tests unitarios
mvn test -Dtest=ClienteServiceTest

# Solo tests de integración
mvn test -Dtest=ClienteControllerIT

# Tests E2E
mvn verify -Dtest=ClienteE2ETest
```

### Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

## 📊 Verificar Versión Actual

```bash
# Verificar Java
java -version

# Verificar JAVA_HOME
echo $JAVA_HOME

# Verificar Maven usa la versión correcta
mvn -version
```

## ⚠️ Importante

- Este proyecto está configurado para **Java 21**
- Si cambias a Java 17, debes actualizar el `pom.xml`:
  - Cambiar `<java.version>21</java.version>` a `17`
  - Cambiar `<source>21</source>` y `<target>21</target>` a `17`
- Después de cambiar la versión de Java, ejecuta `mvn clean compile` para recompilar

## 🔍 Troubleshooting

### Si Maven no reconoce Java 21:
```bash
# Verifica JAVA_HOME
echo $JAVA_HOME

# Debe apuntar a: /opt/homebrew/opt/openjdk@21

# Si no es correcto, configúralo:
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
```

### Si aparecen advertencias sobre "Dynamic loading of agents":
Esto es normal en Java 21. Son advertencias de Mockito/ByteBuddy y no afectan la funcionalidad.
Para ocultarlas, puedes agregar esta opción a Maven:
```bash
export MAVEN_OPTS="-XX:+EnableDynamicAgentLoading"
```

## 📝 Resumen

✅ **Java 21 instalado y funcionando**
✅ **Proyecto compilado exitosamente con Java 21**
✅ **Tests ejecutándose correctamente**
✅ **Listo para desarrollo**
