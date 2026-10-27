package co.sena.sinova.web.servlet;

import co.sena.sinova.web.dao.EmpleadoDAO;
import co.sena.sinova.web.modelo.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Prepara los datos del panel principal y los envía a {@code panel.jsp}.
 *
 * <p>GET: lista los empleados; si llega el parámetro {@code documento}
 * (formulario de búsqueda con método GET) muestra solo ese empleado.
 * Solo atiende a usuarios con sesión iniciada.</p>
 *
 * @author Wilmar Andrés Matiz Rodas
 */
@WebServlet(name = "PanelServlet", urlPatterns = {"/panel"})
public class PanelServlet extends HttpServlet {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=sesion");
            return;
        }
        String documento = request.getParameter("documento");
        try {
            List<Empleado> empleados;
            if (documento != null && !documento.isBlank()) {
                Empleado encontrado = empleadoDAO.consultarPorDocumento(documento.trim());
                empleados = encontrado == null
                        ? Collections.emptyList() : List.of(encontrado);
                request.setAttribute("documentoBuscado", documento.trim());
            } else {
                empleados = empleadoDAO.consultarTodos();
            }
            request.setAttribute("empleados", empleados);
        } catch (SQLException error) {
            log("Error al consultar empleados", error);
            request.setAttribute("mensajeError", "No fue posible consultar los empleados.");
            request.setAttribute("empleados", Collections.emptyList());
        }
        request.getRequestDispatcher("/panel.jsp").forward(request, response);
    }
}
