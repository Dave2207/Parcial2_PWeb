/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Examen2;

import Examen2.ClasesBase.Usuario;
import Examen2.Services.UsuarioServices;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/publico"); // desde la carpeta de resources
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); // aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
        });
        new ControladoraRutas(app).aplicarRutas();

        app.start(7000);
        if(UsuarioServices.getInstance().find(1) == null){
            UsuarioServices.getInstance().create(new Usuario("admin","admin","Administrador"));
        }
        //app.get("/", ctx -> ctx.redirect("/"));
        

    }
}
