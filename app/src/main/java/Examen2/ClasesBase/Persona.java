package Examen2.ClasesBase;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Persona implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String sector;
    private String nivelEscolar;
    private UbicacionGeo ubicacion;
    @ManyToOne
    private Usuario usuario;

    public Persona(String nombre, String sector, String nivelEscolar, String latitud, String longitud, Usuario usuario) {
        this.nombre = nombre;
        this.sector = sector;
        this.nivelEscolar = nivelEscolar;
        this.ubicacion = new UbicacionGeo(latitud, longitud);
        this.usuario = usuario;
    }
    public Persona(Persona aux, Usuario usuario) {
        this.nombre = aux.getNombre();
        this.sector = aux.getSector();
        this.nivelEscolar = aux.getNivelEscolar();
        this.ubicacion = new UbicacionGeo(aux.getUbicacion().getLatitud(), aux.getUbicacion().getLongitud());
        this.usuario = usuario;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return this.nombre+"  "+this.sector+"  "+this.nivelEscolar+"  "+this.usuario.getNombre();
    }
}
