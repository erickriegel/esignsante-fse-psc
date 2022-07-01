package fr.ans.api.sign.esignsante.psc.prosantedatas;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException.throwExceptionRequestError;

@Slf4j
@Service
@ConditionalOnProperty(name = "with.gravitee", havingValue="true", matchIfMissing=false)
public class PSCDataFromHeadersImpl implements PSCData{

	@Override
	public String getIntrospectionResult(String accessToken) {
		bouchon();
		return null;
	}

	@Override
	public Boolean isPSCvalidateToken(String introspectionResult) {
		bouchon();
		return null;
	}

	@Override
	public String getUserInfo(String accessToken) {
		bouchon();
		return null;
	}
	
	private void bouchon() {
		log.error("Les méthodes de PSCDataFromHeadersImpl ne doivent pas être appelées ..");
		throwExceptionRequestError(" Les méthodes de PSCDataFromHeadersImpl ne doivent pas être appelées .." ,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
