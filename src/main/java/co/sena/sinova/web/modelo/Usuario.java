package co.sena.sinova.web.modelo;

/**
 * Usuario del sistema que inicia sesión (tabla {@code usuario}).
 *
 * @author Wilmar Andrés Matiz Rodas
 */
public class Usuario {

    private int idUsuario;
    private String correo;
    private String nombreCompleto;
    private String rol;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
