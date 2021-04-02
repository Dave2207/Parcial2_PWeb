package Examen2;

public class UsuarioServices extends GestionDB<Usuario>{
    
    private static UsuarioServices usuarioService;

    private UsuarioServices() {
        super(Usuario.class);
        //TODO Auto-generated constructor stub
    }

    public static UsuarioServices getInstance(){
        if(usuarioService==null){
            usuarioService = new UsuarioServices();
        }
        return usuarioService;
    }
    
}
