package fr.ans.api.sign.esignsante.psc.storage.repository;

import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import fr.ans.esignsante.model.ESignSanteSignatureReport;

public interface PreuveDAO {

	
	public void archivePreuve(UserInfo userInfo, ESignSanteSignatureReport report);
}
