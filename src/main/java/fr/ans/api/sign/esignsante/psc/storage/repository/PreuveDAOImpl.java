package fr.ans.api.sign.esignsante.psc.storage.repository;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.esignsante.model.ESignSanteSignatureReport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PreuveDAOImpl implements PreuveDAO {

	@Autowired
	MongoTemplate mongoTemplate;
	
	 @Autowired
     ProofMongoRepository repo;

	 
	
//	@Override
	/* sauvegarde de la requete entrante -> création d'un ID MongoDB */
//	public ObjectId archiveRequeteEntrantePreuve(ProofStorage proofIn) {
//		proofIn.setBsonProof(null);
//		mongoTemplate.save(proofIn);
//		log.debug("persistence d'une reqête entrante:\n " 
//		   + "idMongoDB: " +proofIn.get_id() + "\n" 
//		   + "SubjectOrganization: " +proofIn.getSubjectOrganization() + "\n" 
//		   + "Preferred_username(): " +proofIn.getPreferred_username() + "\n" 
//		   + "Given_name: " +proofIn.getGiven_name() + "\n" 
//		   + "Family_name(): " +proofIn.getFamily_name() + "\n"
//		   + "Timestamp(): " +proofIn.getTimestamp() + "\n"
//			);
//	
//		return proofIn.get_id();
//	}

//	@Override
	/* update de la sauvegarde de la requete entrante à partir de l'IdMongoDB  
	-> persistence de la preuve ernvoyé par esignsatewebservices en base */
	public boolean archivePreuve(String idMongoDB, ProofStorage proof) {
		log.debug("demande persistence de la preuve d'une demande de signature: \n"
      	   + "idMongoDB: " +proof.get_id() + "\n" 
		   + "SubjectOrganization: " +proof.getSubjectOrganization() + "\n" 
		   + "Preferred_username(): " +proof.getPreferred_username() + "\n" 
		   + "Given_name: " +proof.getGiven_name() + "\n" 
		   + "Family_name: " +proof.getFamily_name() + "\n"
		   + "Timestamp (acceptation de la reqête): " +proof.getTimestamp() + "\n"
		   + "proof " + proof
		   );
		Query query = Query.query(Criteria.where("id").is(idMongoDB));
		Update update = new Update();
		update.set("proof", proof);
		mongoTemplate.updateFirst( query, update, ProofStorage.class);
		return false;
	}  
	 
 	public boolean archivePreuve(ProofStorage proof) {
		log.debug("demande persistence de la preuve d'une demande de signature: \r\n "
		    +" SubjectOrganization: {} \r\n  Preferred_username: {} \r\n Given_name: {} \r\n"
		    + "Family_name:{}  \r\n Timestamp (acceptation de la reqête): {}  \r\n  preuve  {} ",
		   proof.getSubjectOrganization(), proof.getPreferred_username() ,proof.getGiven_name(),
		   proof.getFamily_name() , proof.getTimestamp(), proof.getProof());
		mongoTemplate.save(proof);
		
		return true;
	}  

	

}
