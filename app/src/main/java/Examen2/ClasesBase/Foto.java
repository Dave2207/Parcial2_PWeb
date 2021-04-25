package Examen2.ClasesBase;

import javax.persistence.*;

import java.io.Serializable;

@Entity
public class Foto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String mime;
    @Lob
    @Column(name="base64",columnDefinition="LONGTEXT")
    private String base64;

    public Foto() {
    }

    public Foto(String mime, String base64){
        this.mime = mime;
        this.base64 = base64;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}