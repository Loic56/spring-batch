package com.loic.dev.spring.batch.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.loic.dev.spring.batch.entities.Acheteur;

public interface AcheteurRepository extends CrudRepository<Acheteur, Long> {

	@Query("select * from DIM_EMPLOYEE_PURCHASER where EMAIL = :email")
	public Acheteur findByEmail(@Param("email") String email);

}
