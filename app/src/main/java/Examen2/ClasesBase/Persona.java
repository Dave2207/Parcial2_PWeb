package Examen2.ClasesBase;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Persona implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String nombre;
    private String sector;
    private String nivelEscolar;
    private UbicacionGeo ubicacion;
    @ManyToOne
    private Usuario usuario;
    private boolean active;

    public Persona(String nombre, String sector, String nivelEscolar, String latitud, String longitud, Usuario usuario) {
        this.nombre = nombre;
        this.sector = sector;
        this.nivelEscolar = nivelEscolar;
        this.ubicacion = new UbicacionGeo(latitud, longitud);
        this.usuario = usuario;
        this.active = true;
    }
    
    public Persona(){ 
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
    public String getNivelEscolar() {
        return nivelEscolar;
    }
    public void setNivelEscolar(String nivelEscolar) {
        this.nivelEscolar = nivelEscolar;
    }
    public UbicacionGeo getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(UbicacionGeo ubicacion) {
        this.ubicacion = ubicacion;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
