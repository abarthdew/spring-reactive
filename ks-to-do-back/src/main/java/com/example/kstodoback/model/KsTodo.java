package com.example.kstodoback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ks_todo")
@AllArgsConstructor
@NoArgsConstructor
public class KsTodo {

  @Id
  @Column("id")
  Long id;

  @Column("todo")
  String todo;

}
