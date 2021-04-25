package Examen2.Services;

import Examen2.ClasesBase.Foto;

public class FotoServices extends GestionDB<Foto>{
    
    private static FotoServices fotoService;

    private FotoServices() {
        super(Foto.class);
        //TODO Auto-generated constructor stub
    }

    public static FotoServices getInstance(){
        if(fotoService==null){
            fotoService = new FotoServices();
        }
        return fotoService;
    }
    
}
