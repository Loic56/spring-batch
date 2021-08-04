package com.loic.dev.spring.batch.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.loic.dev.spring.batch.entities.Fournisseur;

public interface FournisseurRepository extends CrudRepository<Fournisseur, Long> {

	@Query("select c.id, c.name, c.address from DIM_SUPPLIER c where name = :name ")
	public Fournisseur findByName(@Param("name") String name);

}
