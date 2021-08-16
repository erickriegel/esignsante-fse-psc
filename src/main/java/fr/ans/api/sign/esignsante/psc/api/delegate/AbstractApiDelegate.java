package fr.ans.api.sign.esignsante.psc.api.delegate;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * The Class ApiDelegate. Classe mère de tous les delegates. Traitement
 * générique des requêtes
 */
abstract public class AbstractApiDelegate {
	
	final protected String HEADER_TYPE = "application/json";

	/**
	 * Gets the request
	 *
	 * @return the request
	 */
	public Optional<NativeWebRequest> getRequest() {
		Optional<NativeWebRequest> request = Optional.empty();
		final ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();

		if (attrs != null) {
			request = Optional.of(new ServletWebRequest(attrs.getRequest()));
		}
		return request;
	}

	/**
	 * Gets the accept header.
	 *
	 * @return the accept header
	 */
	public Optional<String> getAcceptHeader() {
		return getRequest().map(r -> r.getHeader("Accept"));
	}
	
	protected Path multipartFileToFile(
		    MultipartFile multipart, 
		    Path dir
		) throws IOException {
		    Path filepath = Paths.get(dir.toString(), multipart.getOriginalFilename());
		    multipart.transferTo(filepath);
		    return filepath;
		}
}
