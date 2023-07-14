package com.example.kstodoback.handler;

import com.example.kstodoback.model.KsTodo;
import com.example.kstodoback.repository.KsTodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class KsTodoHandler {

  private final KsTodoRepository ksTodoRepository;

  public KsTodoHandler(KsTodoRepository ksTodoRepository) {
    this.ksTodoRepository = ksTodoRepository;
  }

  public Mono<ServerResponse> readAll(ServerRequest request) {
    Flux<KsTodo> ksTodo = ksTodoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(ksTodo, KsTodo.class);
  }

  public Mono<ServerResponse> create(ServerRequest request) {
    Mono<KsTodo> ksTodo = request.bodyToMono(KsTodo.class)
      .flatMap(ksTodoData -> ksTodoRepository.save(ksTodoData).log());
    return ServerResponse.status(HttpStatus.CREATED)
      .contentType(MediaType.APPLICATION_JSON)
      .body(ksTodo, KsTodo.class);
  }

  public Mono<ServerResponse> update(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<KsTodo> ksTodo = request.bodyToMono(KsTodo.class);
    return ksTodoRepository.findById(Long.valueOf(id))
      .flatMap(item -> ksTodo.flatMap(item1 -> {
        item.setId(item1.getId());
        item.setTodo(item1.getTodo());
        Mono<KsTodo> updatedKsTodo = ksTodoRepository.save(item);
        return ServerResponse.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(updatedKsTodo, KsTodo.class);
      }));

  }

  public Mono<ServerResponse> deleteOne(ServerRequest request) {
    String id = request.pathVariable("name");
    return ksTodoRepository.findById(Long.valueOf(id))
      .flatMap(item -> ksTodoRepository
        .delete(item)
        .then(ServerResponse.ok().build()))
      .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteAll(ServerRequest request) {
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(ksTodoRepository.deleteAll(), KsTodo.class);
  }

}
