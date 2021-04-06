package Examen2;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.HashMap;

import Examen2.ClasesBase.Persona;
import Examen2.ClasesBase.UbicacionGeo;
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
                    
                    try{
                        int id = Integer.valueOf(ctx.cookie("userID"));
                        Usuario aux = usuarioServices.find(id);
                        ctx.sessionAttribute("user",aux);
                    }catch(Exception e){
                    }

                    if(ctx.sessionAttribute("user") == null){
                        ctx.sessionAttribute("previousPath", ctx.path());
                        ctx.redirect("/public/login/");
                    }

                });
                path("/personas", () -> {
                    get("/list",ctx -> {//lista de personas
                        HashMap<String, Object> modelo = new HashMap<>();

                        modelo.put("user", ctx.sessionAttribute("user"));
                        modelo.put("personas", personaServices.findAll());
                        ctx.render("/templates/thymeleaf/listaPersonas.html",modelo);
                    });
                    get("/regist",ctx -> {//formulario de creación
                        HashMap<String, Object> modelo = new HashMap<>();

                        modelo.put("editar", null);
                        modelo.put("user", ctx.sessionAttribute("user"));
                        ctx.render("/templates/thymeleaf/registrarPersona.html",modelo);
                    });
                    get("/edit/:id",ctx -> {//formulario de edicion
                        int id = Integer.valueOf(ctx.formParam("id"));
                        Persona aux = personaServices.find(id);

                        HashMap<String, Object> modelo = new HashMap<>();
                        modelo.put("id", aux.getId());
                        modelo.put("nombre", aux.getNombre());
                        modelo.put("sector", aux.getSector());
                        modelo.put("nivel", aux.getNivelEscolar());
                        modelo.put("lat", aux.getUbicacion().getLatitud());
                        modelo.put("long", aux.getUbicacion().getLongitud());
                        modelo.put("editar", true);
                        modelo.put("user", ctx.sessionAttribute("user"));
                        ctx.render("/templates/thymeleaf/registrarPersona.html",modelo);

                    });
                    get("/remove/:id",ctx -> {//remover persona
                        int id = Integer.valueOf(ctx.formParam("id"));
                        personaServices.delete(id);
                        ctx.redirect("/app/personas/list");
                    });
                    post("/redit",ctx -> {//POST donde se procesan los datos que se obtienen, tanto para registrar como para editar
                        String nombre = ctx.formParam("nombre");
                        String sector = ctx.formParam("sector");
                        String nivelEscolar = ctx.formParam("nivel");
                        String latitud = ctx.formParam("lat");
                        String longitud = ctx.formParam("long");
                        
                        try {
                            int id = Integer.valueOf(ctx.formParam("id"));
                            UbicacionGeo ubi = ubicacionServices.find(id);
                            Persona person = personaServices.find(id);
                            ubi.setLatitud(latitud);
                            ubi.setLongitud(longitud);
                            ubicacionServices.update(ubi);
                            person.setNombre(nombre);
                            person.setSector(sector);
                            person.setNivelEscolar(nivelEscolar);
                            person.setUbicacion(ubi);
                            person.setUsuario(ctx.sessionAttribute("user"));
                            personaServices.update(person);
                        } catch (NumberFormatException e) {
                            UbicacionGeo ubicacion = new UbicacionGeo(latitud,longitud);
                            ubicacionServices.create(ubicacion);
                            Persona person = new Persona(nombre, sector, nivelEscolar, latitud, longitud, ctx.sessionAttribute("user"));
                            personaServices.create(person);
                            System.out.println("Se ha creado 1 persona: "+person.getNombre());
                        }
                        ctx.redirect("/app/personas/regist");
                    });
                });


                path("/usuarios", () -> {
                    get("/list",ctx -> {//lista de usuarios
                        HashMap<String, Object> modelo = new HashMap<>();

                        modelo.put("user", ctx.sessionAttribute("user"));
                        modelo.put("users", usuarioServices.findAll());
                        ctx.render("/templates/thymeleaf/listaUsuarios.html",modelo);
                    });
                    get("/regist",ctx -> {//formulario de creación
                        HashMap<String, Object> modelo = new HashMap<>();

                        modelo.put("editar", null);
                        modelo.put("user", ctx.sessionAttribute("user"));
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
                        modelo.put("user", ctx.sessionAttribute("user"));
                        ctx.render("/templates/thymeleaf/registrarUsuario.html",modelo);
                    });
                    get("/remove/:id",ctx -> {//remover usuario
                        int id = Integer.valueOf(ctx.pathParam("id"));
                        usuarioServices.delete(id);
                        ctx.redirect("/app/usuarios/list");

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
                        ctx.redirect("/app/usuarios/list");

                    });
                });
            });
            get("/public/login", ctx -> {
                HashMap<String, Object> modelo = new HashMap<>();
                modelo.put("user", ctx.sessionAttribute("user"));
                ctx.render("/templates/thymeleaf/login.html",modelo);
            });
            get("/public/logout", ctx -> {
                ctx.removeCookie("userID", "/");
                ctx.sessionAttribute("user",null);
                ctx.redirect("/app/usuarios/list");
            });
            post("/public/login", ctx -> {
                boolean validated;
                int id = 0;
                try {
                    id = Integer.valueOf(ctx.formParam("id"));
                    String contra = ctx.formParam("contra");
                    validated = usuarioServices.validate(id, contra);
                }catch (NumberFormatException e){
                    validated = false;
                }

                if(validated){
                    ctx.sessionAttribute("user",usuarioServices.find(id));
                    if(ctx.formParam("rememberMe") != null){
                        ctx.cookie("userID", Integer.valueOf(id).toString(), 60*60*24*7);//Se guarda el id en una cookie. Al entrar en otra sesión se busca el usuario con ese id.
                    }
                    
                    String previousPath = ctx.sessionAttribute("previousPath");
                    if(previousPath == null){
                        previousPath = "/app/usuarios/list";
                    }
                    
                    ctx.redirect(previousPath);

                }else{
                    HashMap<String, Object> modelo = new HashMap<>();
                    modelo.put("error", true);
                    ctx.render("/templates/thymeleaf/login.html",modelo);

                }
                
                
            });
        });
        
    }
}
