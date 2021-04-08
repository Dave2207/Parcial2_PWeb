package Examen2.ClasesBase;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Usuario implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String contra;
    private String rol;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario") 
    private List<Persona> personasCreadas;
    private boolean active;
    
    public Usuario(String nombre, String contra, String rol) {
        this.nombre = nombre;
        this.contra = contra;
        this.rol = rol;
        this.active = true;

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

    public List<Persona> getPersonasCreadas() {
        return personasCreadas;
    }

    public void setPersonasCreadas(List<Persona> personasCreadas) {
        this.personasCreadas = personasCreadas;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String toString() {
        return this.id+"  "+this.nombre+"  "+this.contra+"  "+this.rol;
    }
}
