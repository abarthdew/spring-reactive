package com.example.kstodoback.router;

import com.example.kstodoback.handler.KsTodoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class KsTodoRouter {
  @Bean
  public RouterFunction<ServerResponse> route(KsTodoHandler ksTodoHandler) {
    return RouterFunctions.route()
        .path("/api", builder -> builder
            .nest(RequestPredicates.contentType(MediaType.APPLICATION_JSON)
                    .or(RequestPredicates.contentType(MediaType.APPLICATION_OCTET_STREAM)), builder1 -> builder1
                    .before(request -> {
                      return request;
                    })
                    .GET("/readAll", ksTodoHandler::readAll)
                    .POST("/create", ksTodoHandler::create)
                    .PUT("/update/{id}", ksTodoHandler::update)
                    .DELETE("/deleteOne/{id}", ksTodoHandler::deleteOne)
                    .DELETE("/deleteAll", ksTodoHandler::deleteAll)
            ))
        .build();
  }
}
