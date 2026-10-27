<%-- Panel principal de SINOVA: página JSP que recibe los datos del PanelServlet --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="co.sena.sinova.web.modelo.Empleado"%>
<%@page import="co.sena.sinova.web.modelo.Usuario"%>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/index.html?error=sesion");
        return;
    }
    @SuppressWarnings("unchecked")
    List<Empleado> empleados = (List<Empleado>) request.getAttribute("empleados");
    if (empleados == null) {
        response.sendRedirect(request.getContextPath() + "/panel");
        return;
    }
    String documentoBuscado = (String) request.getAttribute("documentoBuscado");
    String mensajeError = (String) request.getAttribute("mensajeError");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SINOVA - Panel</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css">
    <link rel="stylesheet" href="css/sinova.css">
</head>
<body>
    <header class="barra">
        <strong>SINOVA</strong>
        <span><%= usuario.getNombreCompleto() %> (<%= usuario.getRol() %>) ·
            <a href="logout">Cerrar sesión</a></span>
    </header>
    <main class="container">
        <h2>Bienvenido, <%= usuario.getNombreCompleto() %></h2>
        <p>Sesión iniciada con <strong><%= usuario.getCorreo() %></strong></p>

        <!-- Formulario de búsqueda: envía el documento al PanelServlet con el método GET -->
        <form action="panel" method="get" role="search">
            <input type="search" name="documento" placeholder="Buscar empleado por número de documento"
                   value="<%= documentoBuscado == null ? "" : documentoBuscado %>">
            <button type="submit">Buscar</button>
        </form>
        <% if (documentoBuscado != null) { %>
            <p><a href="panel">Ver todos los empleados</a></p>
        <% } %>
        <% if (mensajeError != null) { %>
            <p class="alerta"><%= mensajeError %></p>
        <% } %>

        <h3>Empleados registrados (<%= empleados.size() %>)</h3>
        <% if (empleados.isEmpty()) { %>
            <p class="aviso">No se encontraron empleados.</p>
        <% } else { %>
        <div class="overflow-auto">
            <table class="striped">
                <thead>
                    <tr><th>ID</th><th>Tipo</th><th>Documento</th><th>Nombre completo</th>
                        <th>Correo</th><th>Teléfono</th><th>Estado</th></tr>
                </thead>
                <tbody>
                <% for (Empleado empleado : empleados) { %>
                    <tr>
                        <td><%= empleado.getIdEmpleado() %></td>
                        <td><%= empleado.getTipoDocumento() %></td>
                        <td><%= empleado.getNumeroDocumento() %></td>
                        <td><%= empleado.getNombreCompleto() %></td>
                        <td><%= empleado.getCorreo() == null ? "" : empleado.getCorreo() %></td>
                        <td><%= empleado.getTelefono() == null ? "" : empleado.getTelefono() %></td>
                        <td><%= empleado.isActivo() ? "Activo" : "Inactivo" %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <% } %>
    </main>
</body>
</html>
