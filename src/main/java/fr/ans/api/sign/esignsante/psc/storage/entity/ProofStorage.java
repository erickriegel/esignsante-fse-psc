package fr.ans.api.sign.esignsante.psc.storage.entity;

import java.util.Date;

import org.bson.BsonDocument;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@EqualsAndHashCode(of= {"name","request"})
@ToString(of= {"id","subjectOrganization","preferred_username","given_name","family_name","timestamp"})
@Getter
@Document(collection = "archives")
public class ProofStorage {
	@Id
    private String id;
	
	@Setter
    private String subjectOrganization;
	
	@Setter
    private String preferred_username;
	
	@Setter
    private String given_name;
	
	@Setter
    private String family_name;
	
	@Setter
//	@CreatedDate
    private Date timestamp;
	
	@Setter
    private BsonDocument bsonProof;
	
	
}
