package fr.ans.sign.esignsante.psc.api.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fr.ans.api.sign.esignsante.psc.api.DefaultApiDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Class DefaultApiDelegateImpl.
 * Implementation du EndPopint racine
 */

@Service
public class DefaultApiDelegateImpl extends ApiDelegate implements DefaultApiDelegate  {
	
	final String HEADER_TYPE="application/json";
	
	 /**
     * The log.
     */
    Logger log = LoggerFactory.getLogger(DefaultApiDelegateImpl.class);
	
	    /**
	     * Gets the operations.
	     *
	     * @return the operations
	     */
	    @Override
	    public ResponseEntity<List<String>> getOperations() {
	        final Optional<String> acceptHeader = getAcceptHeader();
	        ResponseEntity<List<String>> re = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	        if (acceptHeader.isPresent() && acceptHeader.get().contains(HEADER_TYPE)) {
	            final List<String> methods = new ArrayList<>();
	            methods.add("/");
	         //   methods.add("/notexist");
	            re = new ResponseEntity<>(methods, HttpStatus.OK);
	        } else {
	            log.warn("ObjectMapper or HttpServletRequest not configured in default DefaultApi interface,"
	                    + " so no example is generated");
	           // System.err.println("coucou");
	        }
	        return re;
	    }
	}


