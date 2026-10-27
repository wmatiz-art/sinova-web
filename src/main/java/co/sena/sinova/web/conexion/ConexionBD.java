package co.sena.sinova.web.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión JDBC con la base de datos MySQL de SINOVA.
 *
 * <p>Las credenciales se leen de {@code db.properties}, ubicado en
 * {@code src/main/resources}. Ese archivo no se versiona en Git; en el
 * repositorio solo existe {@code db.properties.example}.</p>
 *
 * @author Wilmar Andrés Matiz Rodas
 */
public final class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "/db.properties";
    private static final String DRIVER_MYSQL = "com.mysql.cj.jdbc.Driver";
    private static final Properties CONFIGURACION = new Properties();

    private ConexionBD() {
    }

    /**
     * Abre una conexión nueva con la base de datos.
     *
     * @return conexión activa; quien la usa debe cerrarla
     * @throws SQLException si el driver no carga o las credenciales fallan
     */
    public static synchronized Connection obtenerConexion() throws SQLException {
        if (CONFIGURACION.isEmpty()) {
            cargarConfiguracion();
        }
        return DriverManager.getConnection(
                CONFIGURACION.getProperty("db.url"),
                CONFIGURACION.getProperty("db.usuario"),
                CONFIGURACION.getProperty("db.contrasena"));
    }

    private static void cargarConfiguracion() throws SQLException {
        try (InputStream entrada = ConexionBD.class.getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                throw new SQLException("No se encontró db.properties en src/main/resources.");
            }
            CONFIGURACION.load(entrada);
            Class.forName(DRIVER_MYSQL);
        } catch (IOException | ClassNotFoundException error) {
            throw new SQLException("No se pudo cargar la configuración de la base de datos.", error);
        }
    }
}
