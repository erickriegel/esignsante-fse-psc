/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.utils;

import java.util.Date;

import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametresSign {
	
	/*
	 * json du de la réponse PSC introspection (non encodé base 64)
	 */
	private String accessToken = null;
	
	/*
	 * json du de la réponse PSC introspection (non encodé base 64)
	 */
	private String jsonPscReponse = "reponse intropesction non connue";
	
	/*
	 * json du de la réponse PSC introspection encodé en base 64
	 */
//	private String jsonBase64PscReponse = null;
	
	/*
	 * json du userInfo (non encodé base 64)
	 */
	private String jsonUserInfo;
	
	/*
	 * json du userInfo encodé base 64
	 */
	//private String jsonBase64UserInfo = null;
	
	private UserInfo userinfo = null;
			
	private String requestID = "";
	
	//private File fileToSign = null;
	
	private Date date = null;


	public ParametresSign() {
		super();
	}
		
	
}
