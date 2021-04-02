package Examen2;

public class UbicacionServices extends GestionDB<UbicacionGeo>{
    
    private static UbicacionServices ubicacionService;

    private UbicacionServices() {
        super(UbicacionGeo.class);
        //TODO Auto-generated constructor stub
    }

    public static UbicacionServices getInstance(){
        if(ubicacionService==null){
            ubicacionService = new UbicacionServices();
        }
        return ubicacionService;
    }
    
}
