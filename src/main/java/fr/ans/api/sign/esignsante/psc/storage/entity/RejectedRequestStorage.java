package fr.ans.api.sign.esignsante.psc.storage.entity;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Getter

/*
 *  Persistence des requêtes rejetées
 */
@Document(collection = "requetesRejetees")
public class RejectedRequestStorage {

}
