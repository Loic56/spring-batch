package com.loic.dev.spring.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.loic.dev.spring.batch.dto.ConvertedInputData;
import com.loic.dev.spring.batch.dto.InputData;
import com.loic.dev.spring.batch.entities.Acheteur;
import com.loic.dev.spring.batch.entities.Commande;
import com.loic.dev.spring.batch.entities.DateAchat;
import com.loic.dev.spring.batch.entities.Fournisseur;
import com.loic.dev.spring.batch.entities.Produit;
import com.loic.dev.spring.batch.repository.AcheteurRepository;
import com.loic.dev.spring.batch.repository.DateAchatRepository;
import com.loic.dev.spring.batch.repository.FournisseurRepository;
import com.loic.dev.spring.batch.repository.ProduitRepository;

public class BatchProcessor implements ItemProcessor<InputData, ConvertedInputData> {

	@Autowired
	private FournisseurRepository supplierRepository;

	@Autowired
	private AcheteurRepository purchaserRepository;

	@Autowired
	private ProduitRepository productRepository;

	@Autowired
	private DateAchatRepository purchaseDateRepository;

	@Override
	public ConvertedInputData process(InputData item) throws Exception {

		ConvertedInputData convertedInputData = new ConvertedInputData();
		Commande order = new Commande();

		Fournisseur fournisseur = supplierRepository.findByName(item.getSupplierName());
		Acheteur acheteur = purchaserRepository.findByEmail(item.getPurchaserEmail());
		Produit produit = productRepository.findByEanCode(item.getProductEanCode());
		DateAchat dateAchat = purchaseDateRepository.findByDate(item.getTransactionDate());

		if (fournisseur == null) {
			fournisseur = Fournisseur.of(null, item.getSupplierName(), item.getSupplierAddress());
			convertedInputData.setSupplier(fournisseur);
		} else {
			order.setSupplierId(fournisseur.getId());
		}

		if (acheteur == null) {
			acheteur = Acheteur.of(null, item.getPurchaserFirstName(), item.getPurchaserLastName(),
					item.getPurchaserEmail());
			convertedInputData.setPurchaser(acheteur);
		} else {
			order.setPurchaserId(acheteur.getId());
		}

		if (produit == null) {
			produit = Produit.of(null, item.getProductName(), item.getProductType(), item.getProductEanCode());
			convertedInputData.setProduct(produit);
			order.setQuantity(item.getProductQuantity());
			order.setAmount(item.getProductAmount() * item.getProductQuantity());
		} else {
			order.setProductId(produit.getId());
			order.setQuantity(item.getProductQuantity());
			order.setAmount(item.getProductAmount() * item.getProductQuantity());
		}

		if (dateAchat == null) {
			dateAchat = DateAchat.of(null, item.getTransactionDate());
			convertedInputData.setPurchaseDate(dateAchat);
		} else {
			order.setDateId(dateAchat.getId());
		}

		convertedInputData.setOrder(order);

		return convertedInputData;
	}

}
