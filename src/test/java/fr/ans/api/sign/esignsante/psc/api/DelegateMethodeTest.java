/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import fr.ans.api.sign.esignsante.psc.api.delegate.ChecksignatureApiDelegateImpl;
import fr.ans.api.sign.esignsante.psc.model.Result;
import fr.ans.esignsante.model.ESignSanteValidationReport;
import fr.ans.esignsante.model.Erreur;

@SpringBootTest 
@AutoConfigureMockMvc

public class DelegateMethodeTest {

	@InjectMocks
	ChecksignatureApiDelegateImpl delegate = new ChecksignatureApiDelegateImpl();
	
	@Test
	public void checkExecuteTest() {
		ESignSanteValidationReport report = new ESignSanteValidationReport();
		report.setValide(false);
		List<Erreur> erreurs = new ArrayList<>();
		Erreur erreur1 = new Erreur();
		erreur1.setCodeErreur("1");
		erreur1.setMessage("erreur1");
		erreurs.add(erreur1);
		Erreur erreur2 = new Erreur();
		erreur2.setCodeErreur("2");
		erreur2.setMessage("erreur2");
		erreurs.add(erreur1);
		erreurs.add(erreur2);
		report.setErreurs(erreurs);
		
		Result result = ReflectionTestUtils.invokeMethod(delegate, "esignsanteReportToResult", report);
		assertEquals(report.isValide(), result.getValid());
		assertEquals(report.getErreurs().size(), result.getErrors().size());
		assertEquals(report.getErreurs().get(0).getCodeErreur(), result.getErrors().get(0).getCode());
		assertEquals(report.getErreurs().get(0).getMessage(), result.getErrors().get(0).getMessage());
		assertEquals(report.getErreurs().get(1).getCodeErreur(), result.getErrors().get(1).getCode());
		assertEquals(report.getErreurs().get(1).getMessage(), result.getErrors().get(1).getMessage());


	}
	
}
