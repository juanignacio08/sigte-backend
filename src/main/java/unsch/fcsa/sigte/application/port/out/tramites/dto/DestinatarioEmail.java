package unsch.fcsa.sigte.application.port.out.tramites.dto;

public class DestinatarioEmail {
    private String email;
    private String nombre;
    private String rol;

    public DestinatarioEmail(String email, String nombre, String rol) {
        this.email = email;
        this.nombre = nombre;
        this.rol = rol;
    }

    public DestinatarioEmail() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
