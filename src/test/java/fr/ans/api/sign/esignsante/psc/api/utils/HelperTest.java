package fr.ans.api.sign.esignsante.psc.api.utils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.ans.api.sign.esignsante.psc.utils.Helper;
import fr.ans.esignsante.model.Erreur;
import lombok.extern.slf4j.Slf4j;
import fr.ans.api.sign.esignsante.psc.model.Error;

@Slf4j
public class HelperTest {

	final public String STRING_TO_ENCODE ="Chaîne de caractère UTF8 &é\"(,;!";
	final public String STRING_TO_DECODE ="Q2hhw65uZSBkZSBjYXJhY3TDqHJlIFVURjggJsOpIigsOyE=";
	
//	@Test
	public void encodeBase64() {
	   String test = Helper.encodeBase64(STRING_TO_ENCODE);
	   log.debug("encodebase64");
	   log.debug(test);
	   assertEquals(test.compareTo(STRING_TO_DECODE),0);
	}
	
//	@Test
	public void decodeBase64() {
	   String test = Helper.decodeBase64(STRING_TO_DECODE);
	   log.debug("decodebase64");
	   log.debug(test);
	   assertEquals(test.compareTo(STRING_TO_ENCODE),0);
	}
	
	@Test
	public void converterErrorErreurTest() {
		log.debug("test conversion Erreur(esignsante) en Error (esigsante-psc");
		final String codeErreur1 = "codeErreur1";
		final String codeErreur2 = "codeErreur2";
		final String message1 = "message1";
		final String message2 = "message2";
		List<Erreur> erreurs = new ArrayList<Erreur>();
		Erreur erreur1= new Erreur();
		erreur1.setCodeErreur(codeErreur1);
		erreur1.setMessage(message1);
		Erreur erreur2= new Erreur();
		erreur2.setCodeErreur(codeErreur2);
		erreur2.setMessage(message2);
		erreurs.add(erreur1);
		erreurs.add(erreur2);
		
		Error error1= new Error();
		error1.setCode(codeErreur1);
		error1.setMessage(message1);
		Error error2= new Error();
		error2.setCode(codeErreur2);
		error2.setMessage(message2);
		
		List<Error> errors = Helper.mapListErreurToListError(erreurs);
		assertTrue(errors.size() == erreurs.size());
		
		errors.stream()
        .forEach(error -> {
        	Erreur er= new Erreur();
        	er.setMessage(error.getMessage());
        	er.setCodeErreur(error.getCode());
        	 	assertTrue(erreurs.contains(er));
        });
		

        errors.stream()
.forEach(error -> log.debug("code :" + error.getCode() + " msg :" + error.getMessage()));

		
	}
}
