package Examen2.ClasesBase;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario") 
    private List<Persona> personasCreadas;
    
    public Usuario(String nombre, String contra, String rol) {
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
