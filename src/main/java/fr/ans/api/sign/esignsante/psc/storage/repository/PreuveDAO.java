package fr.ans.api.sign.esignsante.psc.storage.repository;

import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.esignsante.model.ESignSanteSignatureReport;

public interface PreuveDAO {

	//public String archiveRequeteEntrantePreuve(ProofStorage proofIn);
	
	//public boolean archivePreuve(String idMongoDB,ProofStorage proof );
	
	public boolean archivePreuve(ProofStorage proof );
}
