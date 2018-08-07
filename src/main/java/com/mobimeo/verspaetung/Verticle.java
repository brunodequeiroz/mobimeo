package com.mobimeo.verspaetung;

import com.mobimeo.verspaetung.csv.CSVConnection;
import com.mobimeo.verspaetung.mapper.DelayMapper;
import com.mobimeo.verspaetung.mapper.LineMapper;
import com.mobimeo.verspaetung.mapper.StopMapper;
import com.mobimeo.verspaetung.mapper.TimeMapper;
import com.mobimeo.verspaetung.repository.csv.DelayRepositoryImpl;
import com.mobimeo.verspaetung.repository.csv.LineRepositoryImpl;
import com.mobimeo.verspaetung.repository.csv.StopRepositoryImpl;
import com.mobimeo.verspaetung.repository.csv.TimeRepositoryImpl;
import com.mobimeo.verspaetung.resource.LineResource;
import com.mobimeo.verspaetung.service.LineServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.LoggerHandler;

public class Verticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        final Integer port = config().getInteger("port");
        final Router router = Router.router(vertx);

        router.route().handler(LoggerHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().handler(
            CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Content-Type")
        );

        // TODO use a IoC framework
        final LineMapper lineMapper = new LineMapper();
        final DelayMapper delayMapper = new DelayMapper();
        final StopMapper stopMapper = new StopMapper();
        final TimeMapper timeMapper = new TimeMapper();

        LineResource.register(
            router,
            new LineServiceImpl(
                new LineRepositoryImpl(new CSVConnection<>("db/lines.csv", lineMapper)),
                new DelayRepositoryImpl(new CSVConnection<>("db/delays.csv", delayMapper)),
                new StopRepositoryImpl(new CSVConnection<>("db/stops.csv", stopMapper)),
                new TimeRepositoryImpl(new CSVConnection<>("db/times.csv", timeMapper)),
                lineMapper
            )
        );

        vertx
            .createHttpServer()
            .requestHandler(router::accept)
            .listen(port, ar -> {
                if (ar.failed()) {
                    startFuture.fail(ar.cause());
                } else {
                    startFuture.complete();
                }
            });
    }
}
