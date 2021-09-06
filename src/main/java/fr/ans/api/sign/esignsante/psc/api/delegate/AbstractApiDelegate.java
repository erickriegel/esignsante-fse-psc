package fr.ans.api.sign.esignsante.psc.api.delegate;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * The Class ApiDelegate. Classe mère de tous les delegates. Traitement
 * générique des requêtes
 */
@Slf4j
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
	

	protected File multipartFileToFile(MultipartFile multipart /*,  Path dir*/) throws IOException {
		    
		    log.debug(" multipart.getOriginalFilename: {}", multipart.getOriginalFilename());
//		    //!!!!!!! OK WINdows mais KO sous Linux AccessDenied
//		    log.debug(" FileSystem.getDefault: {}", FileSystems.getDefault().getPath(".").toString());
//		    Path filepath = Paths.get(FileSystems.getDefault().getPath(".").toString(), multipart.getOriginalFilename());
//		    multipart.transferTo(filepath);
//		    return filepath.toFile();
		    
		    
		    
		    //KO -> FileNotFoundException  [C:\Users\cjuillard\eclipse-workspace\esignsante-psc\target\ipsfra.xml] cannot be resolved in the file system for checking its content length
//			File fileToSign = new File(multipart.getOriginalFilename());
//			multipart.transferTo(fileToSign);
//			return fileToSign;
		    
		    
		    Path tempFile = Files.createTempFile(null, null);
		    log.debug("tempFile {}" , tempFile.getFileName().toString() );
		    multipart.transferTo(tempFile);
		    return tempFile.toFile();
		}
	
//	public static File convert(MultipartFile file) throws IOException {
//		File convFile = new File(file.getOriginalFilename());
//		convFile.createNewFile();
//		FileOutputStream fos = new FileOutputStream(convFile);
//		fos.write(file.getBytes());
//		fos.close();
//		return convFile;
//	}
	
	
}
