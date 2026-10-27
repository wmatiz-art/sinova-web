# SINOVA Web – Login y panel (GA7-220501096-AA2-EV02)

Módulo web de SINOVA con **formularios HTML, Servlets (GET y POST) y JSP**, desplegado en **Apache Tomcat 11** y conectado por **JDBC** a la base de datos MySQL `sinova`.

## Funcionalidad
| Recurso | Método | Descripción |
|---|---|---|
| `index.html` | — | Formulario de inicio de sesión (envía por POST) |
| `/login` (`LoginServlet`) | POST | Valida correo y contraseña (hash SHA-256) contra la tabla `usuario` y crea la sesión |
| `/panel` (`PanelServlet`) | GET | Lista empleados o busca por documento (`?documento=`) y reenvía a `panel.jsp` |
| `panel.jsp` | — | Página JSP que muestra el usuario en sesión y la tabla de empleados |
| `/logout` (`LogoutServlet`) | GET | Cierra la sesión |

## Tecnologías
Java 25 · Apache NetBeans 31 (Maven Web Application) · Jakarta EE 11 · Tomcat 11.0.27 · MySQL 8.0 · Connector/J 9.4.0 · Pico CSS con la paleta de SINOVA.

## Ejecución
1. Copiar `src/main/resources/db.properties.example` como `db.properties` y escribir la contraseña de MySQL.
2. Abrir en NetBeans y ejecutar (F6). URL: `http://localhost:8080/SinovaWeb/`

Autor: Wilmar Andrés Matiz Rodas – Ficha 3336196 – SENA
