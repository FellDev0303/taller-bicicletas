# Taller de Bicicletas — API REST

Sistema de gestión de servicios de mantenimiento y reparación de bicicletas para un taller en Cali.  
Proyecto final de **Bases de Datos 2** — Ingeniería Informática, Universidad Autónoma de Occidente.

## Integrantes

| Nombre | Usuario de GitHub | Módulos a cargo |
| :--- | :--- | :--- |
| José Alejandro Alzate Arévalo | [@FellDev0303](https://github.com/FellDev0303) | Usuarios, Catálogo (Servicios y Repuestos), Citas, Órdenes de Servicio, Garantías, Valoración y Diagnóstico, Documentación Wiki |
| Verónica Núñez Zorrilla | [@veronz07](https://github.com/veronz07) | Bicicletas, Trabajos de Orden y Autorización, Revisión y Entrega, y Estadísticas, Documentación Wiki |

## Descripción

El taller atiende bicicletas urbanas, de ruta, de montaña y eléctricas. El sistema centraliza:
- Gestión de clientes, técnicos, coordinadores y bicicletas.
- Programación, confirmación, reprogramación y cancelación de citas.
- Creación de órdenes de servicio (con o sin cita previa) y trazabilidad de estados.
- Diagnóstico técnico con hallazgos, prioridades y evidencias adjuntas.
- Propuesta de intervención, autorización del cliente por trabajo y ejecución con repuestos aplicados.
- Revisión de entrega, recepción del cliente, garantías, valoración del servicio y estadísticas para el coordinador.

> **Alcance:** No incluye gestión de inventario, facturación ni procesamiento de pagos.

## Tecnologías

- **Java 21**
- **Spring Boot 4.2.0** (Spring Web MVC, Spring Data MongoDB, Bean Validation)
- **MongoDB** (Atlas / Local)
- **Maven** (incluido mediante `mvnw`)
- **Hackolade** (modelado documental)
- **Postman** (pruebas funcionales)

## Estructura del repositorio

```text
taller-bicicletas/
├── src/main/java/co/edu/uao/tallerbicicletas/
│   ├── model/        # Documentos y subdocumentos de cada colección
│   ├── dto/          # Objetos de transferencia de datos y validaciones de entrada
│   ├── repository/   # Interfaces de acceso a MongoDB
│   ├── service/      # Lógica y reglas de negocio
│   ├── controller/   # Endpoints REST (/UAO/apirest/...)
│   └── exception/    # Manejo global de excepciones HTTP
├── scripts/          # Creación de colecciones, validaciones $jsonSchema e índices
├── postman/          # Colecciones de pruebas funcionales exportadas
└── README.md

