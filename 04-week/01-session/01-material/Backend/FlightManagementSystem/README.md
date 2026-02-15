# ✈️ Flight Management System - Backend

Sistema integral de gestión aeroportuaria construido con Spring Boot 3.5.5 y Java 17.

## 📋 Tabla de Contenido

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [Testing](#testing)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [API Documentation](#api-documentation)
- [Módulos](#módulos)
- [Buenas Prácticas](#buenas-prácticas)

## 🎯 Características

### Funcionalidades Principales

- ✅ **Autenticación y Autorización**: Spring Security + JWT
- ✅ **Gestión de Aeronaves**: CRUD completo con validaciones
- ✅ **Gestión de Vuelos**: Operaciones y programación
- ✅ **Recursos Humanos**: Empleados, contratos, certificaciones
- ✅ **Infraestructura**: Aeropuertos, terminales, puertas
- ✅ **Geolocalización**: Continentes, países, estados, ciudades
- ✅ **Notificaciones**: Sistema de mensajería
- ✅ **Auditoría**: Tracking completo de cambios

### Características Técnicas

- 🔐 **Seguridad robusta** con Spring Security y JWT
- 📊 **Auditoría automática** con Spring Data JPA Auditing
- 🗃️ **Soft Delete** implementado en todas las entidades
- 🔄 **Optimistic Locking** para prevenir conflictos de concurrencia
- 📝 **Bean Validation** para validación de datos
- 🗺️ **MapStruct** para mapeo Entity ↔ DTO
- 📚 **OpenAPI/Swagger** para documentación de API
- 🧪 **Tests** unitarios y de integración
- 📈 **Actuator** para monitoreo y métricas

## 🔧 Requisitos Previos

- **Java**: 17 o superior (OpenJDK recomendado)
- **Maven**: 3.9.x o superior
- **PostgreSQL**: 15.x o superior
- **Git**: Para control de versiones
- **IDE**: IntelliJ IDEA / Eclipse / VS Code (con extensiones Java)

## 📦 Instalación

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd Backend/FlightManagementSystem
```

### 2. Configurar PostgreSQL

```sql
-- Crear base de datos
CREATE DATABASE flight_management;

-- Crear usuario (opcional)
CREATE USER flight_admin WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE flight_management TO flight_admin;

-- Habilitar extensión UUID
\c flight_management
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
```

### 3. Instalar dependencias

```bash
mvn clean install
```

## ⚙️ Configuración

### Variables de Entorno

Crear archivo `.env` en el directorio raíz:

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/flight_management
DB_USERNAME=postgres
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_secret_key_min_256_bits
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# CORS
CORS_ORIGINS=http://localhost:3000,http://localhost:5173

# Profile
SPRING_PROFILES_ACTIVE=dev

# DDL Auto (update, validate, create, create-drop)
DDL_AUTO=update
```

### Perfiles de Spring

- **dev**: Desarrollo local (logs verbosos, Swagger habilitado)
- **qa**: Testing/QA (logs moderados, Swagger habilitado)
- **prod**: Producción (logs mínimos, Swagger deshabilitado)

Activar perfil:

```bash
# Opción 1: Variable de entorno
export SPRING_PROFILES_ACTIVE=dev

# Opción 2: Parámetro al ejecutar
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Opción 3: En IntelliJ IDEA
Run > Edit Configurations > Environment Variables > SPRING_PROFILES_ACTIVE=dev
```

## 🚀 Ejecución

### Desarrollo

```bash
# Con Maven
mvn spring-boot:run

# Con Maven Wrapper
./mvnw spring-boot:run

# Con JAR compilado
mvn clean package
java -jar target/FlightManagementSystem-0.0.1-SNAPSHOT.jar
```

### Con Docker

```bash
# Build
docker build -t flight-management-backend .

# Run
docker run -p 9000:9000 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/flight_management \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=password \
  flight-management-backend
```

### Con Docker Compose

```bash
docker-compose up -d
```

La aplicación estará disponible en: `http://localhost:9000`

## 🧪 Testing

### Ejecutar todos los tests

```bash
mvn test
```

### Ejecutar tests específicos

```bash
# Tests unitarios
mvn test -Dtest=AircraftServiceTest

# Tests de integración
mvn test -Dtest=AircraftControllerIntegrationTest

# Con cobertura (JaCoCo)
mvn clean test jacoco:report
```

### Ver reporte de cobertura

```bash
open target/site/jacoco/index.html
```

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/SENA/FlightManagementSystem/
│   │       ├── AircraftManagement/
│   │       │   ├── Controller/
│   │       │   ├── Service/
│   │       │   ├── Entity/
│   │       │   ├── Repository/
│   │       │   ├── DTO/
│   │       │   └── Utils/
│   │       ├── Security/
│   │       ├── Flight/
│   │       ├── Geolocation/
│   │       ├── HumanResources/
│   │       ├── Infrastructure/
│   │       ├── Notifications/
│   │       ├── Parameterization/
│   │       ├── PassengersServices/
│   │       └── Config/
│   │           ├── SecurityConfig.java
│   │           ├── JpaAuditingConfig.java
│   │           └── OpenApiConfig.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       ├── application-prod.properties
│       └── messages.properties
└── test/
    └── java/
        └── com/SENA/FlightManagementSystem/
            ├── unit/
            └── integration/
```

## 📚 API Documentation

### Swagger UI

Acceder a la documentación interactiva de la API:

```
http://localhost:9000/swagger-ui.html
```

### OpenAPI JSON

```
http://localhost:9000/api-docs
```

### Actuator Endpoints

```
http://localhost:9000/actuator/health
http://localhost:9000/actuator/metrics
http://localhost:9000/actuator/info
```

## 🧩 Módulos

### 1. Security
- Autenticación con JWT
- Control de acceso basado en roles
- Auditoría de acciones
- Gestión de usuarios y permisos

### 2. Geolocation
- Continentes, países, estados, ciudades
- Gestión jerárquica de ubicaciones

### 3. Infrastructure
- Aeropuertos
- Terminales
- Puertas de embarque

### 4. Aircraft Management
- Gestión de flota
- Mantenimiento programado
- Historial de vuelos
- Componentes y piezas

### 5. Flight Operations
- Programación de vuelos
- Asignación de tripulación
- Gestión de tickets
- Equipaje

### 6. Human Resources
- Empleados
- Contratos
- Certificaciones
- Capacitaciones
- Nómina

### 7. Passengers Services
- Gestión de pasajeros
- Programas de viajero frecuente

### 8. Notifications
- Plantillas de mensajes
- Envío de notificaciones

### 9. Parameterization
- Tipos de documentos
- Tipos de vuelos
- Tipos de aeronaves
- Roles de tripulación
- Clases de tickets

## ✅ Buenas Prácticas

### Código

- ✅ **SOLID Principles**
- ✅ **Clean Code**
- ✅ **DRY (Don't Repeat Yourself)**
- ✅ **KISS (Keep It Simple, Stupid)**
- ✅ **YAGNI (You Aren't Gonna Need It)**

### Arquitectura

- ✅ **Layered Architecture** (Controller → Service → Repository)
- ✅ **Separation of Concerns**
- ✅ **Dependency Injection**
- ✅ **DTO Pattern** para transferencia de datos
- ✅ **Repository Pattern** para acceso a datos

### Base de Datos

- ✅ **Migraciones controladas** con Hibernate
- ✅ **Índices** en campos frecuentemente consultados
- ✅ **Constraints** para integridad referencial
- ✅ **Soft Delete** para preservar histórico

### Seguridad

- ✅ **Passwords hasheadas** con BCrypt
- ✅ **JWT** con expiración
- ✅ **CORS** configurado
- ✅ **HTTPS** en producción
- ✅ **Variables de entorno** para secretos

### Testing

- ✅ **Tests unitarios** para lógica de negocio
- ✅ **Tests de integración** para API
- ✅ **Cobertura** mínima del 80%
- ✅ **Mocks** para dependencias externas

## 🤝 Contribución

1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📝 Licencia

Este proyecto es propiedad de SENA - ADSO 2899747.

## 👥 Equipo

- **Project Manager**: [Nombre]
- **Tech Lead**: [Nombre]
- **Backend Developers**: [Nombres]
- **Frontend Developers**: [Nombres]
- **QA Engineers**: [Nombres]

## 📞 Contacto

Para preguntas o soporte:
- Email: support@flightmanagement.com
- Slack: #flight-management-dev
- Jira: [Link al proyecto]

---

**Versión:** 2.0  
**Última actualización:** Octubre 2024

