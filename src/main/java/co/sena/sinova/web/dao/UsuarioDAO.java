package co.sena.sinova.web.dao;

import co.sena.sinova.web.conexion.ConexionBD;
import co.sena.sinova.web.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Acceso a datos de la tabla {@code usuario} para la autenticación.
 *
 * <p>La contraseña nunca se compara en texto plano: MySQL calcula el hash
 * SHA-256 de la contraseña digitada y lo compara con {@code contrasena_hash}.</p>
 *
 * @author Wilmar Andrés Matiz Rodas
 */
public class UsuarioDAO {

    private static final String SQL_VALIDAR = "SELECT u.id_usuario, u.correo, r.nombre_rol, "
            + "CONCAT(e.nombres, ' ', e.apellidos) AS nombre_completo "
            + "FROM usuario u "
            + "JOIN rol r ON r.id_rol = u.id_rol "
            + "LEFT JOIN empleado e ON e.id_empleado = u.id_empleado "
            + "WHERE u.correo = ? AND u.contrasena_hash = SHA2(?, 256) AND u.estado = 1";
    private static final String SQL_REGISTRAR_ACCESO =
            "UPDATE usuario SET ultimo_acceso = NOW() WHERE id_usuario = ?";

    /**
     * Valida las credenciales de un usuario activo.
     *
     * @param correo     correo institucional
     * @param contrasena contraseña digitada
     * @return el usuario autenticado, o {@code null} si las credenciales no coinciden
     * @throws SQLException si falla la consulta
     */
    public Usuario validarCredenciales(String correo, String contrasena) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_VALIDAR)) {
            sentencia.setString(1, correo);
            sentencia.setString(2, contrasena);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("id_usuario"));
                usuario.setCorreo(resultado.getString("correo"));
                usuario.setNombreCompleto(resultado.getString("nombre_completo"));
                usuario.setRol(resultado.getString("nombre_rol"));
                registrarAcceso(conexion, usuario.getIdUsuario());
                return usuario;
            }
        }
    }

    private void registrarAcceso(Connection conexion, int idUsuario) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_REGISTRAR_ACCESO)) {
            sentencia.setInt(1, idUsuario);
            sentencia.executeUpdate();
        }
    }
}
