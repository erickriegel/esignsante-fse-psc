package fr.ans.api.sign.esignsante.psc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Activitie
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-08-06T10:38:02.434500100+02:00[Europe/Paris]")
public class Activitie   {
  @JsonProperty("codeModeExercice")
  private String codeModeExercice;

  @JsonProperty("codeSecteurDactivite")
  private String codeSecteurDactivite;

  @JsonProperty("codeSectionPharmacien")
  private String codeSectionPharmacien;

  @JsonProperty("codeRole")
  private String codeRole;

  @JsonProperty("numeroSiretSite")
  private String numeroSiretSite;

  @JsonProperty("numeroSirenSite")
  private String numeroSirenSite;

  @JsonProperty("numeroFinessSite")
  private String numeroFinessSite;

  @JsonProperty("numeroFinessetablissementJuridique")
  private String numeroFinessetablissementJuridique;

  @JsonProperty("identifiantTechniqueDeLaStructure")
  private String identifiantTechniqueDeLaStructure;

  @JsonProperty("raisonSocialeSite")
  private String raisonSocialeSite;

  @JsonProperty("enseigneCommercialeSite")
  private String enseigneCommercialeSite;

  @JsonProperty("complementDestinataire")
  private String complementDestinataire;

  @JsonProperty("complementPointGeographique")
  private String complementPointGeographique;

  @JsonProperty("numeroVoie")
  private String numeroVoie;

  @JsonProperty("indiceRepetitionVoie")
  private String indiceRepetitionVoie;

  @JsonProperty("codeTypeDeVoie")
  private String codeTypeDeVoie;

  @JsonProperty("libelleVoie")
  private String libelleVoie;

  @JsonProperty("mentionDistribution")
  private String mentionDistribution;

  @JsonProperty("bureauCedex")
  private String bureauCedex;

  @JsonProperty("codePostal")
  private String codePostal;

  @JsonProperty("codeCommune")
  private String codeCommune;

  @JsonProperty("codePays")
  private String codePays;

  @JsonProperty("telephone")
  private String telephone;

  @JsonProperty("telephone2")
  private String telephone2;

  @JsonProperty("telecopie")
  private String telecopie;

  @JsonProperty("adresseEMail")
  private String adresseEMail;

  @JsonProperty("codeDepartement")
  private String codeDepartement;

  @JsonProperty("ancienIdentifiantDeLaStructure")
  private String ancienIdentifiantDeLaStructure;

  @JsonProperty("autoriteDenregistrement")
  private String autoriteDenregistrement;

  public Activitie codeModeExercice(String codeModeExercice) {
    this.codeModeExercice = codeModeExercice;
    return this;
  }

  /**
   * Get codeModeExercice
   * @return codeModeExercice
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeModeExercice() {
    return codeModeExercice;
  }

  public void setCodeModeExercice(String codeModeExercice) {
    this.codeModeExercice = codeModeExercice;
  }

  public Activitie codeSecteurDactivite(String codeSecteurDactivite) {
    this.codeSecteurDactivite = codeSecteurDactivite;
    return this;
  }

  /**
   * Get codeSecteurDactivite
   * @return codeSecteurDactivite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeSecteurDactivite() {
    return codeSecteurDactivite;
  }

  public void setCodeSecteurDactivite(String codeSecteurDactivite) {
    this.codeSecteurDactivite = codeSecteurDactivite;
  }

  public Activitie codeSectionPharmacien(String codeSectionPharmacien) {
    this.codeSectionPharmacien = codeSectionPharmacien;
    return this;
  }

  /**
   * Get codeSectionPharmacien
   * @return codeSectionPharmacien
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeSectionPharmacien() {
    return codeSectionPharmacien;
  }

  public void setCodeSectionPharmacien(String codeSectionPharmacien) {
    this.codeSectionPharmacien = codeSectionPharmacien;
  }

  public Activitie codeRole(String codeRole) {
    this.codeRole = codeRole;
    return this;
  }

  /**
   * Get codeRole
   * @return codeRole
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeRole() {
    return codeRole;
  }

  public void setCodeRole(String codeRole) {
    this.codeRole = codeRole;
  }

  public Activitie numeroSiretSite(String numeroSiretSite) {
    this.numeroSiretSite = numeroSiretSite;
    return this;
  }

  /**
   * Get numeroSiretSite
   * @return numeroSiretSite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getNumeroSiretSite() {
    return numeroSiretSite;
  }

  public void setNumeroSiretSite(String numeroSiretSite) {
    this.numeroSiretSite = numeroSiretSite;
  }

  public Activitie numeroSirenSite(String numeroSirenSite) {
    this.numeroSirenSite = numeroSirenSite;
    return this;
  }

  /**
   * Get numeroSirenSite
   * @return numeroSirenSite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getNumeroSirenSite() {
    return numeroSirenSite;
  }

  public void setNumeroSirenSite(String numeroSirenSite) {
    this.numeroSirenSite = numeroSirenSite;
  }

  public Activitie numeroFinessSite(String numeroFinessSite) {
    this.numeroFinessSite = numeroFinessSite;
    return this;
  }

  /**
   * Get numeroFinessSite
   * @return numeroFinessSite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getNumeroFinessSite() {
    return numeroFinessSite;
  }

  public void setNumeroFinessSite(String numeroFinessSite) {
    this.numeroFinessSite = numeroFinessSite;
  }

  public Activitie numeroFinessetablissementJuridique(String numeroFinessetablissementJuridique) {
    this.numeroFinessetablissementJuridique = numeroFinessetablissementJuridique;
    return this;
  }

  /**
   * Get numeroFinessetablissementJuridique
   * @return numeroFinessetablissementJuridique
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getNumeroFinessetablissementJuridique() {
    return numeroFinessetablissementJuridique;
  }

  public void setNumeroFinessetablissementJuridique(String numeroFinessetablissementJuridique) {
    this.numeroFinessetablissementJuridique = numeroFinessetablissementJuridique;
  }

  public Activitie identifiantTechniqueDeLaStructure(String identifiantTechniqueDeLaStructure) {
    this.identifiantTechniqueDeLaStructure = identifiantTechniqueDeLaStructure;
    return this;
  }

  /**
   * Get identifiantTechniqueDeLaStructure
   * @return identifiantTechniqueDeLaStructure
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getIdentifiantTechniqueDeLaStructure() {
    return identifiantTechniqueDeLaStructure;
  }

  public void setIdentifiantTechniqueDeLaStructure(String identifiantTechniqueDeLaStructure) {
    this.identifiantTechniqueDeLaStructure = identifiantTechniqueDeLaStructure;
  }

  public Activitie raisonSocialeSite(String raisonSocialeSite) {
    this.raisonSocialeSite = raisonSocialeSite;
    return this;
  }

  /**
   * Get raisonSocialeSite
   * @return raisonSocialeSite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getRaisonSocialeSite() {
    return raisonSocialeSite;
  }

  public void setRaisonSocialeSite(String raisonSocialeSite) {
    this.raisonSocialeSite = raisonSocialeSite;
  }

  public Activitie enseigneCommercialeSite(String enseigneCommercialeSite) {
    this.enseigneCommercialeSite = enseigneCommercialeSite;
    return this;
  }

  /**
   * Get enseigneCommercialeSite
   * @return enseigneCommercialeSite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getEnseigneCommercialeSite() {
    return enseigneCommercialeSite;
  }

  public void setEnseigneCommercialeSite(String enseigneCommercialeSite) {
    this.enseigneCommercialeSite = enseigneCommercialeSite;
  }

  public Activitie complementDestinataire(String complementDestinataire) {
    this.complementDestinataire = complementDestinataire;
    return this;
  }

  /**
   * Get complementDestinataire
   * @return complementDestinataire
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getComplementDestinataire() {
    return complementDestinataire;
  }

  public void setComplementDestinataire(String complementDestinataire) {
    this.complementDestinataire = complementDestinataire;
  }

  public Activitie complementPointGeographique(String complementPointGeographique) {
    this.complementPointGeographique = complementPointGeographique;
    return this;
  }

  /**
   * Get complementPointGeographique
   * @return complementPointGeographique
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getComplementPointGeographique() {
    return complementPointGeographique;
  }

  public void setComplementPointGeographique(String complementPointGeographique) {
    this.complementPointGeographique = complementPointGeographique;
  }

  public Activitie numeroVoie(String numeroVoie) {
    this.numeroVoie = numeroVoie;
    return this;
  }

  /**
   * Get numeroVoie
   * @return numeroVoie
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getNumeroVoie() {
    return numeroVoie;
  }

  public void setNumeroVoie(String numeroVoie) {
    this.numeroVoie = numeroVoie;
  }

  public Activitie indiceRepetitionVoie(String indiceRepetitionVoie) {
    this.indiceRepetitionVoie = indiceRepetitionVoie;
    return this;
  }

  /**
   * Get indiceRepetitionVoie
   * @return indiceRepetitionVoie
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getIndiceRepetitionVoie() {
    return indiceRepetitionVoie;
  }

  public void setIndiceRepetitionVoie(String indiceRepetitionVoie) {
    this.indiceRepetitionVoie = indiceRepetitionVoie;
  }

  public Activitie codeTypeDeVoie(String codeTypeDeVoie) {
    this.codeTypeDeVoie = codeTypeDeVoie;
    return this;
  }

  /**
   * Get codeTypeDeVoie
   * @return codeTypeDeVoie
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeTypeDeVoie() {
    return codeTypeDeVoie;
  }

  public void setCodeTypeDeVoie(String codeTypeDeVoie) {
    this.codeTypeDeVoie = codeTypeDeVoie;
  }

  public Activitie libelleVoie(String libelleVoie) {
    this.libelleVoie = libelleVoie;
    return this;
  }

  /**
   * Get libelleVoie
   * @return libelleVoie
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getLibelleVoie() {
    return libelleVoie;
  }

  public void setLibelleVoie(String libelleVoie) {
    this.libelleVoie = libelleVoie;
  }

  public Activitie mentionDistribution(String mentionDistribution) {
    this.mentionDistribution = mentionDistribution;
    return this;
  }

  /**
   * Get mentionDistribution
   * @return mentionDistribution
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getMentionDistribution() {
    return mentionDistribution;
  }

  public void setMentionDistribution(String mentionDistribution) {
    this.mentionDistribution = mentionDistribution;
  }

  public Activitie bureauCedex(String bureauCedex) {
    this.bureauCedex = bureauCedex;
    return this;
  }

  /**
   * Get bureauCedex
   * @return bureauCedex
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getBureauCedex() {
    return bureauCedex;
  }

  public void setBureauCedex(String bureauCedex) {
    this.bureauCedex = bureauCedex;
  }

  public Activitie codePostal(String codePostal) {
    this.codePostal = codePostal;
    return this;
  }

  /**
   * Get codePostal
   * @return codePostal
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodePostal() {
    return codePostal;
  }

  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }

  public Activitie codeCommune(String codeCommune) {
    this.codeCommune = codeCommune;
    return this;
  }

  /**
   * Get codeCommune
   * @return codeCommune
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeCommune() {
    return codeCommune;
  }

  public void setCodeCommune(String codeCommune) {
    this.codeCommune = codeCommune;
  }

  public Activitie codePays(String codePays) {
    this.codePays = codePays;
    return this;
  }

  /**
   * Get codePays
   * @return codePays
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodePays() {
    return codePays;
  }

  public void setCodePays(String codePays) {
    this.codePays = codePays;
  }

  public Activitie telephone(String telephone) {
    this.telephone = telephone;
    return this;
  }

  /**
   * Get telephone
   * @return telephone
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public Activitie telephone2(String telephone2) {
    this.telephone2 = telephone2;
    return this;
  }

  /**
   * Get telephone2
   * @return telephone2
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getTelephone2() {
    return telephone2;
  }

  public void setTelephone2(String telephone2) {
    this.telephone2 = telephone2;
  }

  public Activitie telecopie(String telecopie) {
    this.telecopie = telecopie;
    return this;
  }

  /**
   * Get telecopie
   * @return telecopie
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getTelecopie() {
    return telecopie;
  }

  public void setTelecopie(String telecopie) {
    this.telecopie = telecopie;
  }

  public Activitie adresseEMail(String adresseEMail) {
    this.adresseEMail = adresseEMail;
    return this;
  }

  /**
   * Get adresseEMail
   * @return adresseEMail
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getAdresseEMail() {
    return adresseEMail;
  }

  public void setAdresseEMail(String adresseEMail) {
    this.adresseEMail = adresseEMail;
  }

  public Activitie codeDepartement(String codeDepartement) {
    this.codeDepartement = codeDepartement;
    return this;
  }

  /**
   * Get codeDepartement
   * @return codeDepartement
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeDepartement() {
    return codeDepartement;
  }

  public void setCodeDepartement(String codeDepartement) {
    this.codeDepartement = codeDepartement;
  }

  public Activitie ancienIdentifiantDeLaStructure(String ancienIdentifiantDeLaStructure) {
    this.ancienIdentifiantDeLaStructure = ancienIdentifiantDeLaStructure;
    return this;
  }

  /**
   * Get ancienIdentifiantDeLaStructure
   * @return ancienIdentifiantDeLaStructure
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getAncienIdentifiantDeLaStructure() {
    return ancienIdentifiantDeLaStructure;
  }

  public void setAncienIdentifiantDeLaStructure(String ancienIdentifiantDeLaStructure) {
    this.ancienIdentifiantDeLaStructure = ancienIdentifiantDeLaStructure;
  }

  public Activitie autoriteDenregistrement(String autoriteDenregistrement) {
    this.autoriteDenregistrement = autoriteDenregistrement;
    return this;
  }

  /**
   * Get autoriteDenregistrement
   * @return autoriteDenregistrement
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getAutoriteDenregistrement() {
    return autoriteDenregistrement;
  }

  public void setAutoriteDenregistrement(String autoriteDenregistrement) {
    this.autoriteDenregistrement = autoriteDenregistrement;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Activitie activitie = (Activitie) o;
    return Objects.equals(this.codeModeExercice, activitie.codeModeExercice) &&
        Objects.equals(this.codeSecteurDactivite, activitie.codeSecteurDactivite) &&
        Objects.equals(this.codeSectionPharmacien, activitie.codeSectionPharmacien) &&
        Objects.equals(this.codeRole, activitie.codeRole) &&
        Objects.equals(this.numeroSiretSite, activitie.numeroSiretSite) &&
        Objects.equals(this.numeroSirenSite, activitie.numeroSirenSite) &&
        Objects.equals(this.numeroFinessSite, activitie.numeroFinessSite) &&
        Objects.equals(this.numeroFinessetablissementJuridique, activitie.numeroFinessetablissementJuridique) &&
        Objects.equals(this.identifiantTechniqueDeLaStructure, activitie.identifiantTechniqueDeLaStructure) &&
        Objects.equals(this.raisonSocialeSite, activitie.raisonSocialeSite) &&
        Objects.equals(this.enseigneCommercialeSite, activitie.enseigneCommercialeSite) &&
        Objects.equals(this.complementDestinataire, activitie.complementDestinataire) &&
        Objects.equals(this.complementPointGeographique, activitie.complementPointGeographique) &&
        Objects.equals(this.numeroVoie, activitie.numeroVoie) &&
        Objects.equals(this.indiceRepetitionVoie, activitie.indiceRepetitionVoie) &&
        Objects.equals(this.codeTypeDeVoie, activitie.codeTypeDeVoie) &&
        Objects.equals(this.libelleVoie, activitie.libelleVoie) &&
        Objects.equals(this.mentionDistribution, activitie.mentionDistribution) &&
        Objects.equals(this.bureauCedex, activitie.bureauCedex) &&
        Objects.equals(this.codePostal, activitie.codePostal) &&
        Objects.equals(this.codeCommune, activitie.codeCommune) &&
        Objects.equals(this.codePays, activitie.codePays) &&
        Objects.equals(this.telephone, activitie.telephone) &&
        Objects.equals(this.telephone2, activitie.telephone2) &&
        Objects.equals(this.telecopie, activitie.telecopie) &&
        Objects.equals(this.adresseEMail, activitie.adresseEMail) &&
        Objects.equals(this.codeDepartement, activitie.codeDepartement) &&
        Objects.equals(this.ancienIdentifiantDeLaStructure, activitie.ancienIdentifiantDeLaStructure) &&
        Objects.equals(this.autoriteDenregistrement, activitie.autoriteDenregistrement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeModeExercice, codeSecteurDactivite, codeSectionPharmacien, codeRole, numeroSiretSite, numeroSirenSite, numeroFinessSite, numeroFinessetablissementJuridique, identifiantTechniqueDeLaStructure, raisonSocialeSite, enseigneCommercialeSite, complementDestinataire, complementPointGeographique, numeroVoie, indiceRepetitionVoie, codeTypeDeVoie, libelleVoie, mentionDistribution, bureauCedex, codePostal, codeCommune, codePays, telephone, telephone2, telecopie, adresseEMail, codeDepartement, ancienIdentifiantDeLaStructure, autoriteDenregistrement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Activitie {\n");
    
    sb.append("    codeModeExercice: ").append(toIndentedString(codeModeExercice)).append("\n");
    sb.append("    codeSecteurDactivite: ").append(toIndentedString(codeSecteurDactivite)).append("\n");
    sb.append("    codeSectionPharmacien: ").append(toIndentedString(codeSectionPharmacien)).append("\n");
    sb.append("    codeRole: ").append(toIndentedString(codeRole)).append("\n");
    sb.append("    numeroSiretSite: ").append(toIndentedString(numeroSiretSite)).append("\n");
    sb.append("    numeroSirenSite: ").append(toIndentedString(numeroSirenSite)).append("\n");
    sb.append("    numeroFinessSite: ").append(toIndentedString(numeroFinessSite)).append("\n");
    sb.append("    numeroFinessetablissementJuridique: ").append(toIndentedString(numeroFinessetablissementJuridique)).append("\n");
    sb.append("    identifiantTechniqueDeLaStructure: ").append(toIndentedString(identifiantTechniqueDeLaStructure)).append("\n");
    sb.append("    raisonSocialeSite: ").append(toIndentedString(raisonSocialeSite)).append("\n");
    sb.append("    enseigneCommercialeSite: ").append(toIndentedString(enseigneCommercialeSite)).append("\n");
    sb.append("    complementDestinataire: ").append(toIndentedString(complementDestinataire)).append("\n");
    sb.append("    complementPointGeographique: ").append(toIndentedString(complementPointGeographique)).append("\n");
    sb.append("    numeroVoie: ").append(toIndentedString(numeroVoie)).append("\n");
    sb.append("    indiceRepetitionVoie: ").append(toIndentedString(indiceRepetitionVoie)).append("\n");
    sb.append("    codeTypeDeVoie: ").append(toIndentedString(codeTypeDeVoie)).append("\n");
    sb.append("    libelleVoie: ").append(toIndentedString(libelleVoie)).append("\n");
    sb.append("    mentionDistribution: ").append(toIndentedString(mentionDistribution)).append("\n");
    sb.append("    bureauCedex: ").append(toIndentedString(bureauCedex)).append("\n");
    sb.append("    codePostal: ").append(toIndentedString(codePostal)).append("\n");
    sb.append("    codeCommune: ").append(toIndentedString(codeCommune)).append("\n");
    sb.append("    codePays: ").append(toIndentedString(codePays)).append("\n");
    sb.append("    telephone: ").append(toIndentedString(telephone)).append("\n");
    sb.append("    telephone2: ").append(toIndentedString(telephone2)).append("\n");
    sb.append("    telecopie: ").append(toIndentedString(telecopie)).append("\n");
    sb.append("    adresseEMail: ").append(toIndentedString(adresseEMail)).append("\n");
    sb.append("    codeDepartement: ").append(toIndentedString(codeDepartement)).append("\n");
    sb.append("    ancienIdentifiantDeLaStructure: ").append(toIndentedString(ancienIdentifiantDeLaStructure)).append("\n");
    sb.append("    autoriteDenregistrement: ").append(toIndentedString(autoriteDenregistrement)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

