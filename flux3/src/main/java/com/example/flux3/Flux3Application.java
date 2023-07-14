package com.example.flux3;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Flux3Application {
  private static Publisher<String> PBS = new Publisher<String>() { // 퍼블리셔
    @Override
    public void subscribe(Subscriber<? super String> sbs) {
      Subscription subscript = new Subscription() { // 신청
        @Override
        public void request(long n) {
          sbs.onNext("abcd");
        }

        @Override
        public void cancel() {
        }
      };
      sbs.onSubscribe(subscript);
    }
  };

  public static void one() {
    Mono.from(PBS).map(arg -> arg.length()).subscribe(arg -> {
      System.out.println("Mono : " + arg);
    });
  }

  public static void main(String[] args) {

    String text = "String Text";
    Mono<String> mono = Mono.just(text); // 단지!
    mono.subscribe(str -> { // 구독!
      System.out.println(str);
    });

    one();

    SpringApplication.run(Flux3Application.class, args);
  }

}
