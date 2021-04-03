package Examen2.ClasesBase;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class UbicacionGeo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String latitud;
    private String longitud;
    
    public UbicacionGeo(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;

    }

    public UbicacionGeo(){
        
    }
    
    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return this.latitud+"  "+this.longitud;
    }
}
