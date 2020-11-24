package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class Cliente {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)

  @Column(name="cliente_id")
  private Integer id;

  @Column(name="nome_cliente")
  private String nome;

  @Column(name="cpf_cliente", unique=true)
  private String cpf;

  public Integer getId() {
    return id;
  }

  public String getCpf() {
    return cpf;
  }

  public String getNome() {
    return nome;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
  
}
