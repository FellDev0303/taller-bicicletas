# Taller de Bicicletas — API REST

Sistema de gestión de servicios de mantenimiento y reparación de bicicletas para un taller en Cali.
Proyecto final de **Bases de Datos 2** — Ingeniería Informática, Universidad Autónoma de Occidente.

## Integrantes

| Nombre | Usuario de GitHub | Módulos a cargo |
|---|---|---|
| _José Alejandro Alzate Arévalo_| _@FellDev0303_ | _por definir_ |
| _Verónica Núñez Zorrilla_ | _@veronz07_ | _por definir_ |

## Descripción

El taller atiende bicicletas urbanas, de ruta, de montaña y eléctricas. El sistema centraliza:

- Gestión de clientes, técnicos, coordinadores y bicicletas.
- Programación de citas y creación de órdenes de servicio.
- Diagnóstico, propuesta de intervención y autorización del cliente por trabajo.
- Ejecución de trabajos, revisión y entrega.
- Garantías, valoración del servicio y estadísticas para el coordinador.

No incluye inventario, facturación ni pagos.

## Tecnologías

- Java 21
- Spring Boot 4.2.0 (Spring Web, Spring Data MongoDB, Validation)
- MongoDB
- Maven (incluido mediante `mvnw`)
- Hackolade (modelado de documentos)
- Postman (pruebas funcionales)

## Estructura del repositorio

```
taller-bicicletas/
├── src/main/java/co/edu/uao/tallerbicicletas/
│   ├── model/        # Documentos de cada colección
│   ├── repository/   # Acceso a MongoDB
│   ├── service/      # Reglas de negocio
│   ├── controller/   # Endpoints REST
│   └── exception/    # Manejo de errores
├── docs/             # Modelado y esquema de la base de datos
├── scripts/          # Creación de colecciones, índices y datos de prueba
├── postman/          # Colección de pruebas exportada
└── README.md
```

## Requisitos previos

- JDK 21 (`java -version`)
- MongoDB en ejecución (local en `localhost:27017` o un cluster de MongoDB Atlas)
- Git

## Configuración

La conexión a MongoDB se define en `src/main/resources/application.properties`:

```properties
spring.application.name=taller-bicicletas
spring.mongodb.uri=mongodb://localhost:27017/taller
```



## Cómo ejecutar

```bash
git clone <https://github.com/FellDev0303/taller-bicicletas.git>
cd taller-bicicletas
./mvnw spring-boot:run        # en Windows PowerShell: .\mvnw spring-boot:run
```

El API queda disponible en `http://localhost:8080`.

## Base de datos

- Motor: MongoDB (base de datos de documentos), esquema moderado con validación `$jsonSchema`.
- Modelo de documentos: carpeta [`docs/`](docs/) (archivo de Hackolade).
- Colecciones: `usuarios`, `bicicletas`, `servicios`, `repuestos`, `citas`, `ordenesServicio`, `trabajosOrden`, `solicitudesGarantia`.
- Script de creación de colecciones, validaciones e índices: carpeta [`scripts/`](scripts/).

