package Examen2;

import java.util.List;
import javax.persistence.*;

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String contra;
    private String rol;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario") 
    private List<Persona> personasCreadas;
    
    public Usuario(int id, String nombre, String contra, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.contra = contra;
        this.rol = rol;
    }

    public Usuario(){
        
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getContra() {
        return contra;
    }
    public void setContra(String contra) {
        this.contra = contra;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
}
