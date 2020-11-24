package com.example.demo;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RepositoryFatura extends CrudRepository<Fatura, Integer> {
    
    //Optional<Fatura> findByMesFaturamento(Integer mesFaturamento);

    Optional<Fatura> findByMesFaturamento(Calendar mesFaturamento);

    Iterable<Fatura> findAllByCliente(Cliente cliente);
}
