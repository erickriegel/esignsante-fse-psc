package fr.ans.api.sign.esignsante.psc.utils;

import java.io.File;
import java.util.Date;

import org.springframework.http.HttpStatus;

import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametresSign {

	//private HttpStatus status = HttpStatus.OK;
	
	//private String msg = "";
	
	private UserInfo userinfo = null;
	
	private String pscReponse = "reponse intropesction non connue";
		
	private String requestID = "";
	
	private File fileToSign = null;
	
	private Date date = null;

//	public ParametresSign(UserInfo userinfo, PSCTokenStatus pscReponse, String requestID, File fileToSign) {
//		super();
//		this.userinfo = userinfo;
//		this.pscReponse = pscReponse;
//		this.requestID = requestID;
//	}

	public ParametresSign() {
		super();
	}
	
	
	
}
