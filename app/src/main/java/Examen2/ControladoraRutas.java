package Examen2;

import io.javalin.Javalin;

public class ControladoraRutas {
    Javalin app;

    public ControladoraRutas(Javalin app){
        this.app = app;
    }

    public void aplicarRutas(){
        
        app.get("/", ctx -> ctx.result("Hola Mundo"));
    }
}
