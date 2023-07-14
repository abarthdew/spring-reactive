package com.example.flux2;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.HashMap;

@Configuration
public class RoutesConfig {

//    @Autowired
//    private FirstHandler handler;

  // route -> FristHandler.jave의 hello()를 실행
  @Bean
  public RouterFunction<ServerResponse> route(FirstHandler handler) {
    return RouterFunctions
      .route(RequestPredicates.GET("/hello")
        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), handler::hello);
  }

  // routeUnPacked() 메서드에서 모두 한꺼번에 실행
  @Bean
  public RouterFunction<ServerResponse> routeUnPacked() {
    return RouterFunctions
      .route(RequestPredicates.GET("/hello2")
          .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
        arg -> {
          HashMap<Object, Object> result = new HashMap<>();
          result.put("2번 number", 5678);
          result.put("2번 text", "webFlux2");
          System.out.println("2번 : " + result);
          Mono<HashMap<Object, Object>> mapper = Mono.just(result);
          Mono<ServerResponse> response
            = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromProducer(mapper, HashMap.class));
          return response;
        });
  }

  // routeUnPacked()를 풀어씀
  // http://localhost:7006/request?&param1=1234&param2=abcd
  @Bean
  public RouterFunction<ServerResponse> requestGetParam() {
    RequestPredicate predicate = RequestPredicates.GET("/request")
      .and(RequestPredicates.accept(MediaType.TEXT_PLAIN));
    // 1. Request + Reducates(요청 + ~이다) : 사용자의 요청 종류 및 형태

    // 2. 웹플럭스에게 전달할 RouterFunction 클래스를 제작, 함수 형태로 전달
    RouterFunction<ServerResponse> response = RouterFunctions.route(predicate, (request) -> {
      // 3. 내부 람다식은 HandlerFunction 인터페이스의 모양
      System.out.println(request.queryParams());
      String justText = "3번";
      Mono<String> mapper = Mono.just(justText); // 4. Mono 클래스를 통해 전달할 내용을 만들고
      Mono<ServerResponse> res = ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
        .body(BodyInserters.fromProducer(mapper, String.class)); // 5. 어떤 형태로 전달할지 작성 후
      return res; // 6. 전달
    });
    return response; // 7. 해당 함수를 웹플럭스에게 전달
  }

  // [숫자 증가] : http://localhost:7006/path/aaa?&param1=abcd
  @Bean
  public RouterFunction<ServerResponse> requestPathAndParam() {
    System.out.println("Method Start!!!");
    final int number[] = {10};
    final RequestPredicate predicate = RequestPredicates.GET("/path/{name}")
      .and(RequestPredicates.accept(MediaType.TEXT_PLAIN));

    RouterFunction<ServerResponse> response = RouterFunctions.route(predicate, (request) -> {
      System.out.println(request.queryParams());
      System.out.println(request.pathVariables());

      String justText = "path and param";
      Mono<String> mapper = Mono.just(justText);
      Mono<ServerResponse> res = ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
        .body(BodyInserters.fromProducer(mapper, String.class));
      number[0] = number[0] + 1;
      System.out.printf("number : %d\n", number[0]);
      return res;
    });
    System.out.println("Method End!!!");
    return response;
  }

  // [파일 다운로드]
  @Bean
  public RouterFunction<ServerResponse> fileDownload() {
    RequestPredicate predicate = RequestPredicates.GET("/file")
      .and(RequestPredicates.accept(MediaType.TEXT_PLAIN));
    RouterFunction<ServerResponse> response = RouterFunctions.route(predicate, (request) -> {
      Resource resource = new FileSystemResource("C:/Users/auswo/Downloads/node/text.csv");
      Mono<Resource> mapper = Mono.just(resource); // Mono의 제네릭에 의해 다양한 클래스를 받을 수 있음

      // 헤더라는 메서드와 바디라는 메서드는 이름만 봐도 어떤 역할을 하는 지 알 것 같다
      Mono<ServerResponse> res = ServerResponse.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.csv\"")
        .body(BodyInserters.fromProducer(mapper, Resource.class));
      return res;
    });
    return response;
  }

  // [파일 업로드]
  @Bean
  public RouterFunction<ServerResponse> fileUpload() {
    RequestPredicate predicate = RequestPredicates.POST("/fileUpload")
      .and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA));
    RouterFunction<ServerResponse> response = RouterFunctions.route(predicate, (request) -> {
      Mono<String> mapper = request.multipartData().map(it -> it.get("files"))
        .flatMapMany(Flux::fromIterable)
        .cast(FilePart.class)
        .flatMap(it -> it.transferTo(Paths.get("C:/Users/auswo/Downloads/node" + it.filename())))
        .then(Mono.just("OK"));
      Mono<ServerResponse> res = ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
        .body(BodyInserters.fromProducer(mapper, String.class));
      return res;
    });
    return response;
  }

}
