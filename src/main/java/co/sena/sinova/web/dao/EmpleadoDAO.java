package co.sena.sinova.web.dao;

import co.sena.sinova.web.conexion.ConexionBD;
import co.sena.sinova.web.modelo.Empleado;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de la tabla {@code empleado}.
 *
 * <p>Implementa las cuatro operaciones CRUD con {@link PreparedStatement},
 * que separa la sentencia SQL de los datos y evita la inyección SQL.</p>
 *
 * @author Wilmar Andrés Matiz Rodas
 */
public class EmpleadoDAO {

    private static final String SQL_INSERTAR = "INSERT INTO empleado "
            + "(id_banco, tipo_documento, numero_documento, nombres, apellidos, "
            + "fecha_nacimiento, direccion, telefono, correo, numero_cuenta, estado) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_CONSULTAR_TODOS =
            "SELECT * FROM empleado ORDER BY id_empleado";
    private static final String SQL_CONSULTAR_POR_DOCUMENTO =
            "SELECT * FROM empleado WHERE numero_documento = ?";
    private static final String SQL_ACTUALIZAR = "UPDATE empleado SET "
            + "nombres = ?, apellidos = ?, direccion = ?, telefono = ?, correo = ?, "
            + "estado = ? WHERE id_empleado = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM empleado WHERE id_empleado = ?";

    /**
     * Inserta un empleado y le asigna el identificador generado por MySQL.
     *
     * @param empleado datos del nuevo empleado
     * @return identificador asignado
     * @throws SQLException si el documento ya existe o falla la conexión
     */
    public int insertar(Empleado empleado) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(
                        SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            if (empleado.getIdBanco() == null) {
                sentencia.setNull(1, Types.SMALLINT);
            } else {
                sentencia.setInt(1, empleado.getIdBanco());
            }
            sentencia.setString(2, empleado.getTipoDocumento());
            sentencia.setString(3, empleado.getNumeroDocumento());
            sentencia.setString(4, empleado.getNombres());
            sentencia.setString(5, empleado.getApellidos());
            sentencia.setDate(6, empleado.getFechaNacimiento() == null
                    ? null : Date.valueOf(empleado.getFechaNacimiento()));
            sentencia.setString(7, empleado.getDireccion());
            sentencia.setString(8, empleado.getTelefono());
            sentencia.setString(9, empleado.getCorreo());
            sentencia.setString(10, empleado.getNumeroCuenta());
            sentencia.setBoolean(11, empleado.isActivo());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    empleado.setIdEmpleado(llaves.getInt(1));
                }
            }
            return empleado.getIdEmpleado();
        }
    }

    /**
     * Consulta todos los empleados.
     *
     * @return lista ordenada por identificador
     * @throws SQLException si falla la consulta
     */
    public List<Empleado> consultarTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_TODOS);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                empleados.add(mapearEmpleado(resultado));
            }
        }
        return empleados;
    }

    /**
     * Busca un empleado por su número de documento.
     *
     * @param numeroDocumento documento a buscar
     * @return el empleado, o {@code null} si no existe
     * @throws SQLException si falla la consulta
     */
    public Empleado consultarPorDocumento(String numeroDocumento) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(
                        SQL_CONSULTAR_POR_DOCUMENTO)) {
            sentencia.setString(1, numeroDocumento);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapearEmpleado(resultado) : null;
            }
        }
    }

    /**
     * Actualiza los datos de contacto y el estado de un empleado.
     *
     * @param empleado empleado con los datos nuevos (requiere idEmpleado)
     * @return {@code true} si se actualizó un registro
     * @throws SQLException si falla la actualización
     */
    public boolean actualizar(Empleado empleado) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            sentencia.setString(1, empleado.getNombres());
            sentencia.setString(2, empleado.getApellidos());
            sentencia.setString(3, empleado.getDireccion());
            sentencia.setString(4, empleado.getTelefono());
            sentencia.setString(5, empleado.getCorreo());
            sentencia.setBoolean(6, empleado.isActivo());
            sentencia.setInt(7, empleado.getIdEmpleado());
            return sentencia.executeUpdate() == 1;
        }
    }

    /**
     * Elimina un empleado.
     *
     * <p>Si el empleado ya tiene contratos, la llave foránea lo impide
     * (ON DELETE RESTRICT); en ese caso se recomienda inactivarlo.</p>
     *
     * @param idEmpleado identificador del empleado
     * @return {@code true} si se eliminó un registro
     * @throws SQLException si la eliminación viola una restricción
     */
    public boolean eliminar(int idEmpleado) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setInt(1, idEmpleado);
            return sentencia.executeUpdate() == 1;
        }
    }

    private Empleado mapearEmpleado(ResultSet resultado) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(resultado.getInt("id_empleado"));
        int idBanco = resultado.getInt("id_banco");
        empleado.setIdBanco(resultado.wasNull() ? null : idBanco);
        empleado.setTipoDocumento(resultado.getString("tipo_documento"));
        empleado.setNumeroDocumento(resultado.getString("numero_documento"));
        empleado.setNombres(resultado.getString("nombres"));
        empleado.setApellidos(resultado.getString("apellidos"));
        Date fechaNacimiento = resultado.getDate("fecha_nacimiento");
        empleado.setFechaNacimiento(fechaNacimiento == null ? null : fechaNacimiento.toLocalDate());
        empleado.setDireccion(resultado.getString("direccion"));
        empleado.setTelefono(resultado.getString("telefono"));
        empleado.setCorreo(resultado.getString("correo"));
        empleado.setNumeroCuenta(resultado.getString("numero_cuenta"));
        empleado.setActivo(resultado.getBoolean("estado"));
        return empleado;
    }
}
