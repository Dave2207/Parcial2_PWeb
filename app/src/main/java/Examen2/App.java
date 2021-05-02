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
            config.addStaticFiles("/publico");
            config.addStaticFiles("/templates"); // desde la carpeta de resources
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); // aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
            config.wsFactoryConfig(wsF -> {
                wsF.getPolicy().setMaxBinaryMessageBufferSize(10000000);

                wsF.getPolicy().setMaxBinaryMessageSize(10000000);

                wsF.getPolicy().setMaxTextMessageBufferSize(10000000);

                wsF.getPolicy().setMaxTextMessageSize(10000000);
            });
        });
        new SoapControlador(app).aplicarRutas();
        new ControladoraRutas(app).aplicarRutas();

        app.start(7000);
        if(UsuarioServices.getInstance().find(1) == null){
            UsuarioServices.getInstance().create(new Usuario("admin","admin","Administrador"));
        }
        app.after(ctx -> {
            //System.out.println("Enviando el header de seguridad para el Service Worker");
            ctx.header("Service-Worker-Allowed", "/");
        });
        

    }
}
