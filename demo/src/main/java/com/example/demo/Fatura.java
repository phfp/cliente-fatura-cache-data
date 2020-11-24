package com.example.demo;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Fatura {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)

  @Column(name = "fatura_id")
  private Integer numeroFatura;

  @ManyToOne()
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "cliente_id")
  private Cliente cliente;

  @Column(name = "mes_faturamento")
  @Temporal(TemporalType.DATE)
  private Calendar mesFaturamento;

  @Column(name = "leitura_atual")
  private Integer leituraAtual;

  @Column(name = "leitura_anterior")
  private Integer leituraAnterior;

  private Integer consumo;

  public Integer getNumeroFatura() {
    return numeroFatura;
  }

  public void setNumeroFatura(Integer numeroFatura) {
    this.numeroFatura = numeroFatura;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Integer getLeituraAtual() {
    return leituraAtual;
  }

  public void setLeituraAtual(Integer leituraAtual) {
    this.leituraAtual = leituraAtual;
  }

  public Integer getLeituraAnterior() {
    return leituraAnterior;
  }

  public void setLeituraAnterior(Integer leituraAnterior) {
    this.leituraAnterior = leituraAnterior;
  }

  public void setConsumo() {
    this.consumo = this.leituraAtual - this.leituraAnterior;
  }

  public void setMesFaturamento(Calendar mesFaturamento) {
    this.mesFaturamento = mesFaturamento;
  }

  public Calendar getMesFaturamento() {
    return mesFaturamento;
  }

  public Integer getConsumo() {
    return this.consumo;
  }
  
}

