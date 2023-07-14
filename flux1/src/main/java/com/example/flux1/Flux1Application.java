package com.example.flux1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class Flux1Application {

  public static void main(String[] args) {

    // Mono Test
    // 1번
    String text = "abcd";
    Mono<String> mono = Mono.just(text);
    mono.subscribe(str -> { // 변수 text를 살펴보고, 그에 대한 콜백행위는 아래 출력
      System.out.println("1번 : " + str);
    });

    // 2번
    Mono<Object> function = Mono.create(sink -> { // javascript의 Promise 같은 느낌
//            sink.error(new Exception("error")); // 에러를 발생시킴
      sink.success("success"); // success 값을 전달
    });
    function.subscribe(arg -> {
      System.out.println("2번 : " + arg);
    });

    // 3번
    mono.doOnNext(str -> { // 맨 처음 구독한 행위에 대한 지정
      System.out.println("3번 : " + str);
    }).subscribe();

    // 4번
    CompletableFuture<?> future = CompletableFuture.supplyAsync(() -> {
      System.out.println("4번");
      return "PARAM";
    });

    // 5번
    Mono<Object> mono2 = Mono.fromFuture(future); // 비동기에 대한 구독
    mono2.subscribe((param) -> {
      System.out.println("5번 : " + param);
    });

    SpringApplication.run(Flux1Application.class, args);
  }

}
