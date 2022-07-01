package fr.ans.api.sign.esignsante.psc.prosantedatas;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/*
 * facade
 * read PSC intropestion result and userInfo
 */

public interface PSCData {

	
	/*
	 * Get the PSC introspection response
	 */
	public String getIntrospectionResult(String accessToken);
	
	public Boolean isPSCvalidateToken(String introspectionResult);
	
	/*
	 * Get the userInfo
	 */
	public String getUserInfo(String accessToken);
	
	
}
