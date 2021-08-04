package com.loic.dev.spring.batch.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.loic.dev.spring.batch.entities.Produit;

public interface ProduitRepository extends CrudRepository<Produit, Long> {

	@Query("select p.id, p.name, p.prdt_type, p.ean_code from DIM_PRODUCT p where EAN_CODE = :eanCode ")
	public Produit findByEanCode(@Param("eanCode") String eanCode);

}
