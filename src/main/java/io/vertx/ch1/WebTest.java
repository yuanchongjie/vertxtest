package io.vertx.ch1;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * Created by yuanchongjie on 2017/5/3.
 */
public class WebTest {
    public static void main(String[] args) {
        Vertx vertx = Begin.getInstance(null);
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

       /* router.route().handler(routingContext -> {

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");
        });*/

        Route route1 = router.route("/some/path/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            // enable chunked responses because we will be adding data as
            // we execute over other handlers. This is only required once and
            // only if several handlers do output.
            response.setChunked(true);

            response.write("route1\n");

            // Call the next matching route after a 5 second delay
            routingContext.vertx().setTimer(5000, tid -> routingContext.next());
        });

        Route route2 = router.route("/some/path/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.write("route2\n");

            // Call the next matching route after a 5 second delay
            routingContext.vertx().setTimer(5000, tid ->  routingContext.next());
        });

        Route route3 = router.route("/some/path/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.write("route3");

            // Now end the response
            routingContext.response().end();
        });


        server.requestHandler(router::accept).listen(8080);
    }

    public static  Vertx getInstanceTest2(VertxOptions vertxOptions){

        if(vertxOptions!=null){
            return Vertx.vertx(vertxOptions);
        }
        return null;

    }
}
