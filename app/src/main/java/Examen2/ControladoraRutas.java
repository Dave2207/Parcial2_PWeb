package Examen2;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.HashMap;

import Examen2.ClasesBase.Usuario;
import Examen2.Services.PersonaServices;
import Examen2.Services.UbicacionServices;
import Examen2.Services.UsuarioServices;


public class ControladoraRutas {
    Javalin app;
    private UsuarioServices usuarioServices = UsuarioServices.getInstance();
    private PersonaServices personaServices = PersonaServices.getInstance();
    private UbicacionServices ubicacionServices = UbicacionServices.getInstance();
    
    public ControladoraRutas(Javalin app){
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    public void aplicarRutas(){
        app.routes(() -> {
            get("/", ctx -> {//localhost:7000/ redirige al formulario de personas, que es donde está todo
                ctx.redirect("/app/personas/regist");
            });
            path("/app", () -> {
                before(ctx -> {//en CRUD hay un before, que comprueba que el usuario esté loggeado, sino lo manda al login. Si el usuario ya se encuentra loggeado, se procede a la página solicitada
                    ctx.sessionAttribute("user",true);
                    if(ctx.sessionAttribute("user") == null){
                        
                        ctx.redirect("/public/login/");

                    }

                });
                path("/personas", () -> {
                    get("/list",ctx -> {//lista de personas
                    });
                    get("/regist",ctx -> {//formulario de creación
                        HashMap<String, Object> modelo = new HashMap<>();

                        modelo.put("editar", null);
                        ctx.render("/templates/thymeleaf/registrarPersona.html",modelo);
                    });
                    get("/edit/:id",ctx -> {//formulario de edicion
                    });
                    get("/remove/:id",ctx -> {//remover persona
                    });
                    post("/redit",ctx -> {//POST donde se procesan los datos que se obtienen, tanto para registrar como para editar
                    });
                });


                path("/usuarios", () -> {
                    get("/list",ctx -> {//lista de usuarios
                        for (Usuario aux : usuarioServices.findAll()) {
                            System.out.println(aux.toString()   );
                        }
                    });
                    get("/regist",ctx -> {//formulario de creación
                        HashMap<String, Object> modelo = new HashMap<>();

                        modelo.put("editar", null);
                        ctx.render("/templates/thymeleaf/registrarUsuario.html",modelo);
                        
                    });
                    get("/edit/:id",ctx -> {//formulario de edicion
                        int id = Integer.valueOf(ctx.pathParam("id"));
                        Usuario aux = usuarioServices.find(id);

                        HashMap<String, Object> modelo = new HashMap<>();
                        modelo.put("id", aux.getId());
                        modelo.put("nombre", aux.getNombre());
                        modelo.put("contra", aux.getContra());
                        modelo.put("rol", aux.getRol());
                        modelo.put("editar", true);
                        ctx.render("/templates/thymeleaf/registrarUsuario.html",modelo);
                    });
                    get("/remove/:id",ctx -> {//remover usuario
                        int id = Integer.valueOf(ctx.pathParam("id"));
                        usuarioServices.delete(id);
                    });
                    post("/redit",ctx -> {//POST donde se procesan los datos que se obtienen, tanto para registrar como para editar
                        String nombre = ctx.formParam("nombre");
                        String contra = ctx.formParam("contra");
                        String rol = ctx.formParam("rol");
                        try{
                            int id = Integer.valueOf(ctx.formParam("id"));
                            Usuario entidad = usuarioServices.find(id);
                            entidad.setNombre(nombre);
                            entidad.setContra(contra);
                            entidad.setRol(rol);
                            entidad.setActive(true);
                            usuarioServices.update(entidad);
                        }catch(NumberFormatException e){
                            Usuario aux = new Usuario(nombre, contra, rol);
                            usuarioServices.create(aux);

                        }

                    });
                });
            });
            get("/public/login", ctx -> {
                
                
                HashMap<String, Object> modelo = new HashMap<>();
                ctx.render("/templates/thymeleaf/login.html",modelo);
            });
        });
        
    }
}
