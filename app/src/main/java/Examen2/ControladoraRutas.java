package Examen2;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Examen2.ClasesBase.Persona;
import Examen2.ClasesBase.UbicacionGeo;
import Examen2.ClasesBase.Usuario;
import Examen2.Services.PersonaServices;
import Examen2.Services.UbicacionServices;
import Examen2.Services.UsuarioServices;

import com.fasterxml.jackson.databind.ObjectMapper;
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
           /* ws("/websocket/algo", ws -> {
                ObjectMapper mapper = new ObjectMapper();
                ws.onConnect(ctx -> System.out.println("Connected"));
                ws.onMessage(ctx -> System.out.println("Enviaron un mensaje"));
            });*/
            get("/", ctx -> {//localhost:7000/ redirige al formulario de personas, que es donde está todo
                ctx.redirect("/app/personas/regist");
            });
            
            path("/app", () -> {
                ws("/synchronize/", ws -> {
                    ws.onConnect(ctx -> System.out.println("Connected"));
                    ws.onMessage(WsMessageContext -> {
                        List<String> JSONData = Arrays.asList(WsMessageContext.message().replace("},{", "}\n{").split("\n"));
                        //JSONData.set(0,JSONData.get(0)+"}");
                        //int i = -1;
                        for (String data :JSONData){
                            ObjectMapper mapper = new ObjectMapper();
                            HashMap<String,String> aux = mapper.readValue(data, HashMap.class);
                            Persona newPer = new Persona(aux.get("nombre"),aux.get("sector"),aux.get("nivelEscolar"),aux.get("latitud"),aux.get("longitud"),WsMessageContext.sessionAttribute("user"));
                            UbicacionGeo ubicacion = newPer.getUbicacion();
                            ubicacionServices.create(ubicacion);
                            personaServices.create(newPer);
                        }

                        //String nombre, String sector, String nivelEscolar, String latitud, String longitud, Usuario usuario
                       
                    });
                });
                /*post("/synchronize/",ctx -> {
                    System.out.println("Hola");
                    System.out.println(ctx.body());
                    System.out.println(ctx.url());
                    
                    System.out.println(ctx.attributeMap());
                    System.out.println(ctx.formParam("data"));
                    System.out.println(ctx.formParams("data"));
                    
                });*/
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
                        Usuario usr = ctx.sessionAttribute("user");
                        if(!usr.getRol().equals("Administrador")){
                            ctx.render("/templates/thymeleaf/error.html");
                        } else {
                        int id = Integer.valueOf(ctx.pathParam("id"));
                        Persona aux = personaServices.find(id);

                        HashMap<String, Object> modelo = new HashMap<>();
                        modelo.put("id", aux.getId());
                        modelo.put("nombre", aux.getNombre());
                        modelo.put("sector", aux.getSector());
                        modelo.put("nivelEscolar", aux.getNivelEscolar());
                        modelo.put("lat", aux.getUbicacion().getLatitud());
                        modelo.put("long", aux.getUbicacion().getLongitud());
                        modelo.put("editar", true);
                        modelo.put("user", ctx.sessionAttribute("user"));
                        ctx.render("/templates/thymeleaf/registrarPersona.html",modelo);
                        }

                    });
                    get("/remove/:id",ctx -> {//remover persona
                        Usuario usr = ctx.sessionAttribute("user");
                        if(!usr.getRol().equals("Administrador")){
                            ctx.render("/templates/thymeleaf/error.html");
                        } else {
                            int id = Integer.valueOf(ctx.pathParam("id"));
                            personaServices.delete(id);
                            ctx.redirect("/app/personas/list");
                        }
                    });
                    get("/ubicacion/:id",ctx -> {//remover usuario
                        int id = Integer.valueOf(ctx.pathParam("id"));
                        Persona aux = personaServices.find(id);
                        HashMap<String, Object> modelo = new HashMap<>();
                        modelo.put("persona", aux);
                        modelo.put("lat", aux.getUbicacion().getLatitud());
                        modelo.put("long", aux.getUbicacion().getLongitud());
                        modelo.put("user", ctx.sessionAttribute("user"));
                        ctx.render("/templates/thymeleaf/mapa.html",modelo);
                    });
                    post("/redit",ctx -> {//POST donde se procesan los datos que se obtienen, tanto para registrar como para editar
                        String nombre = ctx.formParam("nombre");
                        String sector = ctx.formParam("sector");
                        String nivelEscolar = ctx.formParam("nivelEscolar");
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
                            Persona person = new Persona(nombre, sector, nivelEscolar, latitud, longitud, ctx.sessionAttribute("user"));
                            UbicacionGeo ubicacion = person.getUbicacion();
                            ubicacionServices.create(ubicacion);
                            personaServices.create(person);
                            //System.out.println("Se ha creado 1 persona: "+person.getNombre());
                        }
                        ctx.redirect("/app/personas/regist");
                    });
                });


                path("/usuarios", () -> {
                    get("/list",ctx -> {//lista de usuarios
                        Usuario usr = ctx.sessionAttribute("user");
                        if(!usr.getRol().equals("Administrador")){
                            ctx.render("/templates/thymeleaf/error.html");
                        } else {
                            HashMap<String, Object> modelo = new HashMap<>();
                            
                            modelo.put("user", usr);
                            modelo.put("users", usuarioServices.findAll());
                            
                            // //prueba
                            // ObjectMapper mapper = new ObjectMapper();
                            // String JSON = mapper.writeValueAsString(usr);
                            // modelo.put("userJSON", JSON); //para pasar de objeto a JSON
                            // modelo.put("usr", mapper.readValue(JSON, Usuario.class)); //para pasar de JSON a objeto
                            // ///////


                            ctx.render("/templates/thymeleaf/listaUsuarios.html",modelo);
                        }
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
                        String status = ctx.formParam("status");
                        Usuario aux = null;
                        int id = -1;
                        try{
                            id = Integer.valueOf(ctx.formParam("id"));
                            aux = usuarioServices.find(id);
                            aux.setNombre(nombre);
                            aux.setContra(contra);
                            aux.setRol(rol);
                            aux.setActive(true);
                            usuarioServices.update(aux);
                        }catch(NumberFormatException e){
                            aux = new Usuario(nombre, contra, rol);
                            usuarioServices.create(aux);

                        }

                        // if(status == "online"){
                        //     if(id == -1){

                        //     }else{
                                
                        //     }
                        // }

                        ctx.redirect("/app/usuarios/list");

                    });
                });
            });

            path("/REST", () -> {
                after(ctx -> {
                    ctx.header("Content-Type", "application/json");
                });

                get("/", ctx ->{
                    //Listar personas
                    ctx.json(personaServices.findAll());
                });

                get("/:id", ctx -> {
                    //Listar una persona
                    int id = Integer.valueOf(ctx.pathParam("id"));
                    ctx.json(personaServices.find(id));
                });

                post("/", ctx -> {
                    //Crear persona
                    ObjectMapper mapper = new ObjectMapper();
                    HashMap<String,String> aux = mapper.readValue(ctx.body(), HashMap.class);
                    Persona newPer = new Persona(aux.get("nombre"),aux.get("sector"),aux.get("nivelEscolar"),aux.get("latitud"),aux.get("longitud"),usuarioServices.find(1));
                    UbicacionGeo ubicacion = newPer.getUbicacion();
                    ubicacionServices.create(ubicacion);
                    personaServices.create(newPer);
                });

                delete("/:id", ctx -> {
                    //Borrar persona
                    int id = Integer.valueOf(ctx.pathParam("id"));
                    ctx.json(personaServices.delete(id));
                });
            });


            get("/public/login", ctx -> {
                if(ctx.sessionAttribute("user")==null){
                    HashMap<String, Object> modelo = new HashMap<>();
                    modelo.put("user", ctx.sessionAttribute("user"));
                    ctx.render("/templates/thymeleaf/login.html",modelo);
                }else{
                    String previousPath = ctx.sessionAttribute("previousPath");
                    if(previousPath == null){
                        previousPath = "/app/personas/regist";
                    }
                    
                    ctx.redirect(previousPath);
                }
            });
            get("/public/logout", ctx -> {
                ctx.removeCookie("userID", "/");
                ctx.sessionAttribute("user",null);
                ctx.redirect("/public/login");
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
                        previousPath = "/app/personas/regist";
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
