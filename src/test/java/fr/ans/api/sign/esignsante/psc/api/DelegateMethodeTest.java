/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.dump.DumpArchiveEntry.TYPE;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import fr.ans.api.sign.esignsante.psc.api.delegate.AsksignatureApiDelegateImpl;
import fr.ans.api.sign.esignsante.psc.api.delegate.ChecksignatureApiDelegateImpl;
import fr.ans.api.sign.esignsante.psc.model.Result;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.api.sign.esignsante.psc.utils.ParametresSign;
import fr.ans.api.sign.esignsante.psc.utils.TYPE_SIGNATURE;
import fr.ans.esignsante.model.ESignSanteValidationReport;
import fr.ans.esignsante.model.Erreur;

@SpringBootTest 
@AutoConfigureMockMvc

public class DelegateMethodeTest {

  private static final String SIGNER_XADES = "Délégataire de signature pour ";
	
	private static final String SIGNER_PADES = "Signé pour le compte de ";
	
	@InjectMocks
	ChecksignatureApiDelegateImpl delegate = new ChecksignatureApiDelegateImpl();
	
	@InjectMocks
	AsksignatureApiDelegateImpl askDelegate = new AsksignatureApiDelegateImpl();
	
	
	@Test
	public void checkErrorsToErreur() {
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
	
	/////////////////////////////////////////////
	@Test
	public void askSetSigners() {
		UserInfo  userinfo = new  UserInfo();
		userinfo.setFamilyName("FamilyName");
		userinfo.setSubjectNameID("SubjectNameId");
		List<String> signers = ReflectionTestUtils.invokeMethod(askDelegate, "setSigners", TYPE_SIGNATURE.XADES, userinfo);
		assertEquals(1,signers.size());
		assertTrue(signers.get(0).contains(SIGNER_XADES));
		assertTrue(signers.get(0).contains(userinfo.getFamilyName()));
		assertTrue(signers.get(0).contains(userinfo.getSubjectNameID()));
		
		signers = ReflectionTestUtils.invokeMethod(askDelegate, "setSigners", TYPE_SIGNATURE.PADES, userinfo);
		assertEquals(1,signers.size());
		assertTrue(signers.get(0).contains(SIGNER_PADES));
		assertTrue(signers.get(0).contains(userinfo.getFamilyName()));
		assertTrue(signers.get(0).contains(userinfo.getSubjectNameID()));
	}
	

}
