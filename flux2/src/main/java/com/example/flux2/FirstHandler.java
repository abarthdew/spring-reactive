package com.example.flux2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
@Slf4j
public class FirstHandler {
  private HashMap<Object, Object> result = new HashMap<>();
  private Mono<HashMap<Object, Object>> mapper = Mono.just(result);

  public Mono<ServerResponse> hello(ServerRequest request) {
    // 메서드 이름 hello가 RoutesConfig.java의 GET("/hello")이며, 둘은 반드시 같아야 함.
    result.put("number", 1234);
    result.put("text", "webFlux");
    Mono.just(result).subscribe((arg) -> {
      log.info("arg", arg);
    });
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromProducer(mapper, HashMap.class));
  }
}
