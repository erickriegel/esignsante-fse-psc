package fr.ans.api.sign.esignsante.psc.utils;

public enum PSCTokenStatus {
    VALID ("Réponse PSC 200: Token actif"),
	
    INVALID ("Réponse PSC 200: Token non actif"),
    
    FIELD_NOT_FOUND ("Réponse PSC 200: champ 'active' non trouvé dans la réponse"),
    
    TECH_ERROR ("Erreur technique"),
	
    NO_RESPONSE ("Exception lors instropection PSC");

	//Exceptions:
	//JsonProcessingException
	//UnsupportedEncodingException
	//URISyntaxException
	
	private final String pscTokenStatus;
	
	PSCTokenStatus(final String pscTokenStatus) {
	        this.pscTokenStatus = pscTokenStatus;
	    }
	
	public String getPSCTokenStatus() {
        return pscTokenStatus;
    }
}

