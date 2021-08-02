package fr.ans.api.sign.esignsante.psc.storage.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.mongodb.lang.NonNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


//@FieldDefaults(level=AccessLevel.PRIVATE)
//@NoArgsConstructor
//@AllArgsConstructor
@Getter
//@Setter
//@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode(of= {"name","request"})
@ToString(of= {"id","requestDate","request","name"})
public class ArchiveSignature {

	
	public ArchiveSignature(Date requestDate, String request, String name) {
		super();
		this.requestDate = requestDate;
		this.request = request;
		this.name = name;
	}

	@Id
    private String id;
	
	
	@Setter
    private Date requestDate;
	
	@Setter
	private String request;
	
	@Setter
	private String name;
//	@NonNull
//	final private String name;
	
}
