package com.mobimeo.verspaetung.resource;

import com.mobimeo.verspaetung.exception.NotFoundException;
import com.mobimeo.verspaetung.service.LineService;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class LineResource {

    private final static String PATH = "/lines";

    private final LineService service;

    private LineResource(final LineService service) {
        this.service = service;
    }

    public static void register(final Router router, final LineService service) {
        LineResource controller = new LineResource(service);
        router.get(PATH + "/:name").handler(controller::byName);
        router.get(PATH).handler(controller::search);
    }

    private void search(RoutingContext context) {
        final String query = context.request().getParam("query");
        final String x = context.request().getParam("x");
        final String y = context.request().getParam("y");

        if (query == null) {
            context.response().setStatusCode(BAD_REQUEST.code()).end("query parameter is required");
            return;
        }

        if (x == null) {
            context.response().setStatusCode(BAD_REQUEST.code()).end("x parameter is required");
            return;
        }

        if (y == null) {
            context.response().setStatusCode(BAD_REQUEST.code()).end("y parameter is required");
            return;
        }

        final LocalTime time = LocalTime.parse(query, DateTimeFormatter.ISO_TIME);

        service
            .search(time, Integer.valueOf(x), Integer.valueOf(y))
            .toList()
            .subscribe(
                results -> handleSuccess(context, results),
                error -> handleError(context, error)
            );
    }

    private void byName(RoutingContext context) {
        service
            .byName(context.request().getParam("name"))
            .subscribe(
                results -> handleSuccess(context, results),
                error -> handleError(context, error)
            );
    }

    private <T> void handleSuccess(final RoutingContext context, final T data) {
        context
            .response()
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end(Json.encodePrettily(data));
    }

    private void handleError(final RoutingContext context, final Throwable error) {
        Integer status = INTERNAL_SERVER_ERROR.code();
        if (error instanceof NotFoundException) {
            status = NOT_FOUND.code();
        }

        context
            .response()
            .setStatusCode(status)
            .end(error.getLocalizedMessage());
    }
}
