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
                        HashMap<String, Object> modelo = new HashMap<>();
                        
                        modelo.put("editar", true);
                        ctx.render("/templates/thymeleaf/registrarUsuario.html",modelo);
                    });
                    get("/remove/:id",ctx -> {//remover usuario
                    });
                    post("/redit",ctx -> {//POST donde se procesan los datos que se obtienen, tanto para registrar como para editar
                        String nombre = ctx.formParam("nombre");
                        String contra = ctx.formParam("contra");
                        String rol = ctx.formParam("rol");
                        Usuario aux = new Usuario(nombre, contra, rol);
                        usuarioServices.create(aux);



                    });
                });
            });
            get("/public/login", ctx -> {
                
                
                HashMap<String, Object> modelo = new HashMap<>();
                ctx.render("/templates/thymeleaf/login.html",modelo);
            });
            get("/persona", ctx -> {//Es una prueba
                
                
                HashMap<String, Object> modelo = new HashMap<>();
                ctx.render("/templates/thymeleaf/registrarPersona.html",modelo);
            });
        });
        
    }
}
