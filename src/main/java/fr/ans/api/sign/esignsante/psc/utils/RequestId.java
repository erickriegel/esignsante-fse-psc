package fr.ans.api.sign.esignsante.psc.utils;

public enum RequestId {
	
	
	SIGN_XADES_WITH_PROOF ("Demande de signature XADES avec preuve"),
	
	SIGN_PADES_WITH_PROOF ("Demande de signature PADES avec preuve"),
	
    CHECK_XADES ("Contrôle de signature XADES"),
	
	CHECK_PADES ("Contrôle de signature PADES");

	private final String requestId;
	
	RequestId(final String reqId) {
	        this.requestId = reqId;
	    }
	
	public String getRequestId() {
        return requestId;
    }
}
