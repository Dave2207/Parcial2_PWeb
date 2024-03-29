package Examen2;

import com.sun.net.httpserver.HttpContext;
import Examen2.Services.PersonaWebServices;
import io.javalin.Javalin;
import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.xml.ws.Endpoint;
import java.lang.reflect.Method;


/**
 * Clase para implementar JAX-WS
 */
public class SoapControlador {
    private Javalin app;

    public SoapControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {
        Server server = app.server().server();
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        
        //Contexto donde estoy agrupando.
        try {
            HttpContext context = build(server, "/ws");

            //El o los servicios que estoy agrupando en ese contexto
            PersonaWebServices wsa = new PersonaWebServices();
            Endpoint endpoint = Endpoint.create(wsa);
            endpoint.publish(context);
            // Para acceder al wsdl en http://localhost:7000/ws/PersonaWebServices?wsdl
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("bien");
    }

    /**
     *
     * @param server
     * @param contextString
     * @return
     * @throws Exception
     */
    private HttpContext build(Server server, String contextString) throws Exception {
        JettyHttpServer jettyHttpServer = new JettyHttpServer(server, true);
        JettyHttpContext ctx = (JettyHttpContext) jettyHttpServer.createContext(contextString);
        Method method = JettyHttpContext.class.getDeclaredMethod("getJettyContextHandler");
        method.setAccessible(true);
        HttpSpiContextHandler contextHandler = (HttpSpiContextHandler) method.invoke(ctx);
        contextHandler.start();
        return ctx;
    }
}
