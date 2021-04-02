package Examen2;

public class PersonaServices extends GestionDB<Persona>{
    
    private static PersonaServices personaService;

    private PersonaServices() {
        super(Persona.class);
        //TODO Auto-generated constructor stub
    }

    public static PersonaServices getInstance(){
        if(personaService==null){
            personaService = new PersonaServices();
        }
        return personaService;
    }
    
}
