package fr.ans.api.sign.esignsante.psc.api.requests;

import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;

public class SnippetDefinition {

	private SnippetDefinition() {};
	
	private static final Snippet RESPONSE_HEADERS_STATUS_OK = HeaderDocumentation.responseHeaders(headerWithName("Status").description("status de la réponse 200"));
	
}
