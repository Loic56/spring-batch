package com.loic.dev.spring.batch.repository;

import org.springframework.data.repository.CrudRepository;

import com.loic.dev.spring.batch.entities.Commande;

public interface CommandeRepository extends CrudRepository<Commande, Long> {

}
