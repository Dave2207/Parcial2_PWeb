package Examen2.Services;


import javax.jws.WebMethod;
import javax.jws.WebService;

import Examen2.ClasesBase.Persona;

import java.util.ArrayList;
import java.util.List;


@WebService
public class PersonaWebServices {
    private PersonaServices personaServices = PersonaServices.getInstance();

    @WebMethod
    public List<Persona> getListaPersona(){
        return personaServices.findAll();
    }

    public List<Persona> getPersonasCreadasUsuario(int usuarioId){

        List<Persona> personasUsuario = new ArrayList<Persona>();
        for (Persona p : this.personaServices.findAll()){
            if(p.getUsuario().getId() == usuarioId){
                personasUsuario.add(p);
            }
        }
        return personasUsuario;
    }

    @WebMethod
    public Persona getPersona(int matricula){
        return personaServices.find(matricula);
    }

    @WebMethod
    public Persona crearPersona(Persona Persona){
        return personaServices.create(Persona);
    }

    @WebMethod
    public Persona actualizarPersona(Persona Persona){
        return personaServices.update(Persona);
    }
}
