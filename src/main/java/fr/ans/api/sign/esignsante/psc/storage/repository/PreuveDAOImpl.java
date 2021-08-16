package fr.ans.api.sign.esignsante.psc.storage.repository;

import java.util.Date;

import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.api.sign.esignsante.psc.storage.entity.ProofStorage;
import fr.ans.esignsante.model.ESignSanteSignatureReport;

public class PreuveDAOImpl implements PreuveDAO {

	@Override
	public void archivePreuve(UserInfo userInfo, ESignSanteSignatureReport report) {
		ProofStorage proof = new ProofStorage(
				userInfo.getSubjectOrganization(),
				userInfo.getPreferredUsername(),
				userInfo.getGivenName(),
				userInfo.getFamilyName(),
				new Date(),
				null);// TODO Auto-generated method stub
		
	}
	
//	private ProofStorage UserInfoToProofStorage(UserInfo userInfo, ) {
//		
//	}

}
