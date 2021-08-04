package com.loic.dev.spring.batch.dto;

import com.loic.dev.spring.batch.entities.Acheteur;
import com.loic.dev.spring.batch.entities.Commande;
import com.loic.dev.spring.batch.entities.DateAchat;
import com.loic.dev.spring.batch.entities.Fournisseur;
import com.loic.dev.spring.batch.entities.Produit;

import lombok.Data;

@Data
public class ConvertedInputData {

	private Fournisseur supplier;
	private Acheteur purchaser;
	private Produit product;
	private DateAchat purchaseDate;
	private Commande order;

}
