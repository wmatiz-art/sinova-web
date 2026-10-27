package co.sena.sinova.web.servlet;

import co.sena.sinova.web.dao.UsuarioDAO;
import co.sena.sinova.web.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Procesa el formulario de inicio de sesión de {@code index.html}.
 *
 * <ul>
 *   <li>POST: recibe correo y contraseña, valida contra la base de datos y
 *       redirige al panel o de vuelta al login con un mensaje de error.</li>
 *   <li>GET: no procesa credenciales (no deben viajar en la URL); redirige al login.</li>
 * </ul>
 *
 * @author Wilmar Andrés Matiz Rodas
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/index.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String correo = valorLimpio(request.getParameter("correo"));
        String contrasena = valorLimpio(request.getParameter("contrasena"));

        if (correo.isEmpty() || contrasena.isEmpty()) {
            redirigirConError(request, response, "campos");
            return;
        }
        try {
            Usuario usuario = usuarioDAO.validarCredenciales(correo, contrasena);
            if (usuario == null) {
                redirigirConError(request, response, "credenciales");
                return;
            }
            HttpSession sesion = request.getSession(true);
            sesion.setAttribute("usuario", usuario);
            sesion.setMaxInactiveInterval(30 * 60);
            response.sendRedirect(request.getContextPath() + "/panel");
        } catch (SQLException error) {
            log("Error al validar credenciales", error);
            redirigirConError(request, response, "conexion");
        }
    }

    private void redirigirConError(HttpServletRequest request, HttpServletResponse response,
            String codigo) throws IOException {
        response.sendRedirect(request.getContextPath() + "/index.html?error=" + codigo);
    }

    private String valorLimpio(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
