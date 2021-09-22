/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import fr.ans.api.sign.esignsante.psc.api.delegate.ChecksignatureApiDelegateImpl;
import fr.ans.api.sign.esignsante.psc.esignsantewebservices.call.EsignsanteCall;
import fr.ans.api.sign.esignsante.psc.model.Result;
import fr.ans.api.sign.esignsante.psc.prosantecall.ProsanteConnectCalls;
import fr.ans.api.sign.esignsante.psc.utils.TYPE_SIGNATURE;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
public class DelegateMethodeInternalTest {

//	@MockBean
//	EsignsanteCall esignWS;
//	
//	@MockBean
//	ProsanteConnectCalls pscApi;

	final ChecksignatureApiDelegateImpl checkDelegate = Mockito.spy( new ChecksignatureApiDelegateImpl());

	
	@Test
	public void checkExecuteFileKOXADESTest() throws IOException  {

		//erreur de lecture sur le fichier reçu
		
		List<String> headerAccept = new ArrayList<String>();
		headerAccept.add("application/json");
		
		MockMultipartFile fileXML = null;
		try {
			fileXML = new MockMultipartFile("file", "ipsfra.xml", null,
					Files.readAllBytes(new ClassPathResource("EsignSanteWS/Xades/ipsfra.xml").getFile().toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Mockito.when(checkDelegate.getAcceptHeaders()).thenReturn(headerAccept);
		Mockito.doThrow(new IOException()).when(checkDelegate).multipartFileToFile(Mockito.any(MultipartFile.class));

		try {
			assertTrue(fileXML.getSize()>0);
			assertEquals("ipsfra.xml", fileXML.getOriginalFilename());
		ReflectionTestUtils.invokeMethod(checkDelegate, "execute",
				TYPE_SIGNATURE.XADES, fileXML);
		assertTrue(false);
		}
		catch (Exception e) {
			assertEquals("fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException",
					e.getClass().getCanonicalName());		
		}
	}
	
	@Test
	public void checkExecuteFileKOPADESTest() throws IOException  {

		//erreur de lecture sur le fichier reçu
		
		List<String> headerAccept = new ArrayList<String>();
		headerAccept.add("application/json");
		
		MockMultipartFile filePDF = null;
		try {
			filePDF = new MockMultipartFile("file", "ANS.pdf", null,
					Files.readAllBytes(new ClassPathResource("EsignSanteWS/Pades/ANS.pdf").getFile().toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Mockito.when(checkDelegate.getAcceptHeaders()).thenReturn(headerAccept);
		Mockito.doThrow(new IOException()).when(checkDelegate).multipartFileToFile(Mockito.any(MultipartFile.class));

		try {
			assertTrue(filePDF.getSize()>0);
			assertEquals("ANS.pdf", filePDF.getOriginalFilename());
		ReflectionTestUtils.invokeMethod(checkDelegate, "execute",
				TYPE_SIGNATURE.PADES, filePDF);
		assertTrue(false);
		}
		catch (Exception e) {
			assertEquals("fr.ans.api.sign.esignsante.psc.api.exception.EsignPSCRequestException",
					e.getClass().getCanonicalName());		
		}
	}
	
//	@Test
//	public void checkExecuteBadFileTest()  {
//		
//		MockMultipartFile fileXML = null;
//		try {
//			fileXML = new MockMultipartFile("file", "signedipsfra.xml", null,
//					Files.readAllBytes(new ClassPathResource("EsignSanteWS/Xades/signedipsfra.xml").getFile().toPath()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		String reponsePSCActif = Files.readString(
//				new ClassPathResource("PSC/PSC200_activetrue_medecin_899700218896.json").getFile().toPath());
//	
//		
//		List<String> headerAccept = new ArrayList<String>();
//		headerAccept.add("application/json");
//		
//		Mockito.when(checkDelegate.getAcceptHeaders()).thenReturn(headerAccept);
//		//intro PSC
//    	Mockito.doReturn(reponsePSCActif).when(pscApi).isTokenActive(any());
//    	
//     	 Mockito.doReturn(report).when(esignWS).signatureXades(any(File.class),any(List.class),any(),any(List.class) );
//  
//		ResponseEntity<Result> re = ReflectionTestUtils.invokeMethod(checkDelegate, "execute",
//				TYPE_SIGNATURE.XADES, fileXML);
//		
//		assertTrue(re.hasBody());
//		assertTrue(re.getBody().getValid());
//	}
}
