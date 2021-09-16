/**
 * (c) Copyright 1998-2021, ANS. All rights reserved.
 */
package fr.ans.api.sign.esignsante.psc.api.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

public class EsignPSCRequestException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private fr.ans.api.sign.esignsante.psc.model.Error erreur;

	@Getter
	@Setter
	private HttpStatus statusARetourner = HttpStatus.INTERNAL_SERVER_ERROR;

	public EsignPSCRequestException(fr.ans.api.sign.esignsante.psc.model.Error erreur, HttpStatus statusARetourner) {
		super();
		this.erreur = erreur;
		this.statusARetourner = statusARetourner;
	}
}
