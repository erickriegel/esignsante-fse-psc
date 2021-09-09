package fr.ans.api.sign.esignsante.psc.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PSCTokenResult {

	private PSCTokenStatus status = PSCTokenStatus.TECH_ERROR;
	
	private String pscResponse = "";

	public PSCTokenResult(PSCTokenStatus status, String pscResponse) {
		super();
		this.status = status;
		this.pscResponse = pscResponse;
	}
	
}
