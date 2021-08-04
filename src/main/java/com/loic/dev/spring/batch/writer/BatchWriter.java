package com.loic.dev.spring.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.loic.dev.spring.batch.dto.ConvertedInputData;
import com.loic.dev.spring.batch.entities.Acheteur;
import com.loic.dev.spring.batch.entities.DateAchat;
import com.loic.dev.spring.batch.entities.Fournisseur;
import com.loic.dev.spring.batch.entities.Produit;
import com.loic.dev.spring.batch.repository.AcheteurRepository;
import com.loic.dev.spring.batch.repository.CommandeRepository;
import com.loic.dev.spring.batch.repository.DateAchatRepository;
import com.loic.dev.spring.batch.repository.FournisseurRepository;
import com.loic.dev.spring.batch.repository.ProduitRepository;

public class BatchWriter implements ItemWriter<ConvertedInputData> {

	@Autowired
	private FournisseurRepository supplierRepository;

	@Autowired
	private AcheteurRepository purchaserRepository;

	@Autowired
	private ProduitRepository productRepository;

	@Autowired
	private DateAchatRepository purchaseDateRepository;

	@Autowired
	private CommandeRepository commandRepository;

	@Override
	public void write(List<? extends ConvertedInputData> items) throws Exception {

		items.stream().forEach(item -> {
			Fournisseur fournisseur = null;
			Acheteur acheteur = null;
			Produit produit = null;
			DateAchat dateAchat = null;

			if (item.getOrder().getSupplierId() == null) {
				fournisseur = supplierRepository.save(item.getSupplier());
				item.getOrder().setSupplierId(fournisseur.getId());
			}
			if (item.getOrder().getPurchaserId() == null) {
				acheteur = purchaserRepository.save(item.getPurchaser());
				item.getOrder().setPurchaserId(acheteur.getId());
			}
			if (item.getOrder().getProductId() == null) {
				produit = productRepository.save(item.getProduct());
				item.getOrder().setProductId(produit.getId());
			}
			if (item.getOrder().getDateId() == null) {
				dateAchat = purchaseDateRepository.save(item.getPurchaseDate());
				item.getOrder().setDateId(dateAchat.getId());
			}
			commandRepository.save(item.getOrder());
		});
	}

}
