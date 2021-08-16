package fr.ans.api.sign.esignsante.psc.storage.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.bson.BsonDocument;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of= {"preferred_username","timestamp"})
@ToString(of= {"id","subjectOrganization","preferred_username","given_name","family_name","timestamp"})
@Getter

/*
 * Classe ProofStorage: Modèle pour stokage de la preuve dans MongoDB
 */
@Document(collection = "archivesPreuves")
public class ProofStorage {
	
	public ProofStorage(String subjectOrganization, String preferred_username, String given_name, String family_name,
			Date timestamp , BsonDocument bsonProof) {
		super();
		this.subjectOrganization = subjectOrganization;
		this.preferred_username = preferred_username;
		this.given_name = given_name;
		this.family_name = family_name;
		this.timestamp = timestamp;
		this.bsonProof = bsonProof;
	}

	@Id
    private String id;
	
	@Setter
	@NotNull(message = "Persistence: le champ 'subjectOrganization' ne doit pas être nul")
    private String subjectOrganization;
	
	@Setter
	@NotNull(message = "Persistence: le champ 'preferred_username' ne doit pas être nul")
    private String preferred_username;
	
	@Setter
	@NotNull(message = "Persistence: le champ 'given_name' ne doit pas être nul")
    private String given_name;
	
	@Setter
	@NotNull(message = "Persistence: le champ 'family_name' ne doit pas être nul")
    private String family_name;
	
	@Setter
	@NotNull(message = "Persistence: le 'timestamp' ne doit pas être nul")
    private Date timestamp;
	
	@Setter
	@NotNull(message = "Persistence: le 'bsonProof' ne doit pas être nul")
    private BsonDocument bsonProof;
	
	
}
