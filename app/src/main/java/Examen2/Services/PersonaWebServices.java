package Examen2.Services;


import javax.jws.WebMethod;
import javax.jws.WebService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import Examen2.ClasesBase.Foto;
import Examen2.ClasesBase.Persona;
import Examen2.ClasesBase.UbicacionGeo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@WebService
public class PersonaWebServices {
    private PersonaServices personaServices = PersonaServices.getInstance();
    private ObjectMapper mapper = new ObjectMapper();
    @WebMethod
    public String holaMundo(String hola){
        System.out.println("Ejecuntado en el servidor.");
        return "Hola Mundo "+hola+", :-D";
    }   

    @WebMethod
    public String otroMetodo(String hola){
        System.out.println("Ejecuntado en el servidor.");
        return "Hola Mundo "+hola+", :-D";
    }

    @WebMethod
    public List<String> getListaPersona(){
        List<String> fin = new ArrayList<String>();
        List<Persona> aux = personaServices.findAll();
        for(Persona p : aux){
            p.getFoto().setBase64("");
            try {
                fin.add(mapper.writeValueAsString(p));
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fin; 
    }

    @WebMethod
    public List<String> getPersonasCreadasUsuario(int usuarioId){

        List<String> fin = new ArrayList<String>();
        List<Persona> aux = personaServices.findAll();

        for (Persona p : aux){
            if(p.getUsuario().getId() == usuarioId){
                p.getFoto().setBase64("");
                try {
                    fin.add(mapper.writeValueAsString(p));
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return fin; 
    }

    @WebMethod
    public Persona getPersona(int matricula){
        return personaServices.find(matricula);
    }

    @WebMethod
    public Persona crearPersona(String PersonaJSON){
        HashMap<String, String> aux = null;
        try {
            aux = mapper.readValue(PersonaJSON, HashMap.class);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Persona newPer = new Persona(aux.get("nombre"),aux.get("sector"),aux.get("nivelEscolar"),aux.get("latitud"),aux.get("longitud"),UsuarioServices.getInstance().find(1), new Foto("image/png",aux.get("picture-in")));

        UbicacionGeo ubicacion = newPer.getUbicacion();
        Foto foto = newPer.getFoto();
        UbicacionServices.getInstance().create(ubicacion);
        FotoServices.getInstance().create(foto);
        personaServices.create(newPer);
        return newPer;
    }
}
