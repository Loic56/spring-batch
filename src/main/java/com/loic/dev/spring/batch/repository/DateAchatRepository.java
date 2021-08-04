package com.loic.dev.spring.batch.repository;

import java.util.Date;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.loic.dev.spring.batch.entities.DateAchat;

public interface DateAchatRepository extends CrudRepository<DateAchat, Long> {

	@Query("select * from DIM_DATE where DATE_TIME = :date ")
	public DateAchat findByDate(@Param("date") Date date);

}
