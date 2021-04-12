package com.example.kstodoback.repository;

import com.example.kstodoback.model.KsTodo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KsTodoRepository extends R2dbcRepository<KsTodo, Long> {
}
