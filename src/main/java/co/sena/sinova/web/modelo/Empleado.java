package co.sena.sinova.web.modelo;

import java.time.LocalDate;

/**
 * Representa a una persona vinculada a la empresa (tabla {@code empleado}).
 *
 * <p>Los atributos son privados (encapsulamiento) y se acceden mediante
 * métodos get y set, según el estándar definido en GA7-220501096-AA1-EV02.</p>
 *
 * @author Wilmar Andrés Matiz Rodas
 */
public class Empleado {

    private int idEmpleado;
    private Integer idBanco;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correo;
    private String numeroCuenta;
    private boolean activo = true;

    public Empleado() {
    }

    public Empleado(String tipoDocumento, String numeroDocumento, String nombres,
            String apellidos, String correo) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public Integer getIdBanco() { return idBanco; }
    public void setIdBanco(Integer idBanco) { this.idBanco = idBanco; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("%-4d %-3s %-12s %-32s %-26s %s",
                idEmpleado, tipoDocumento, numeroDocumento, getNombreCompleto(),
                correo == null ? "" : correo, activo ? "Activo" : "Inactivo");
    }
}
