package fr.ans.api.sign.esignsante.psc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fr.ans.api.sign.esignsante.psc.model.Activitie;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Exercice
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-08-06T10:38:02.434500100+02:00[Europe/Paris]")
public class Exercice   {
  @JsonProperty("codeProfession")
  private String codeProfession;

  @JsonProperty("codeCategorieProfessionnelle")
  private String codeCategorieProfessionnelle;

  @JsonProperty("codeCiviliteDexercice")
  private String codeCiviliteDexercice;

  @JsonProperty("nomDexercice")
  private String nomDexercice;

  @JsonProperty("prenomDexercice")
  private String prenomDexercice;

  @JsonProperty("codeTypeSavoirFaire")
  private String codeTypeSavoirFaire;

  @JsonProperty("codeSavoirFaire")
  private String codeSavoirFaire;

  @JsonProperty("activities")
  @Valid
  private Set<Activitie> activities = new LinkedHashSet<Activitie>();

  public Exercice codeProfession(String codeProfession) {
    this.codeProfession = codeProfession;
    return this;
  }

  /**
   * Get codeProfession
   * @return codeProfession
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeProfession() {
    return codeProfession;
  }

  public void setCodeProfession(String codeProfession) {
    this.codeProfession = codeProfession;
  }

  public Exercice codeCategorieProfessionnelle(String codeCategorieProfessionnelle) {
    this.codeCategorieProfessionnelle = codeCategorieProfessionnelle;
    return this;
  }

  /**
   * Get codeCategorieProfessionnelle
   * @return codeCategorieProfessionnelle
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeCategorieProfessionnelle() {
    return codeCategorieProfessionnelle;
  }

  public void setCodeCategorieProfessionnelle(String codeCategorieProfessionnelle) {
    this.codeCategorieProfessionnelle = codeCategorieProfessionnelle;
  }

  public Exercice codeCiviliteDexercice(String codeCiviliteDexercice) {
    this.codeCiviliteDexercice = codeCiviliteDexercice;
    return this;
  }

  /**
   * Get codeCiviliteDexercice
   * @return codeCiviliteDexercice
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeCiviliteDexercice() {
    return codeCiviliteDexercice;
  }

  public void setCodeCiviliteDexercice(String codeCiviliteDexercice) {
    this.codeCiviliteDexercice = codeCiviliteDexercice;
  }

  public Exercice nomDexercice(String nomDexercice) {
    this.nomDexercice = nomDexercice;
    return this;
  }

  /**
   * Get nomDexercice
   * @return nomDexercice
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getNomDexercice() {
    return nomDexercice;
  }

  public void setNomDexercice(String nomDexercice) {
    this.nomDexercice = nomDexercice;
  }

  public Exercice prenomDexercice(String prenomDexercice) {
    this.prenomDexercice = prenomDexercice;
    return this;
  }

  /**
   * Get prenomDexercice
   * @return prenomDexercice
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getPrenomDexercice() {
    return prenomDexercice;
  }

  public void setPrenomDexercice(String prenomDexercice) {
    this.prenomDexercice = prenomDexercice;
  }

  public Exercice codeTypeSavoirFaire(String codeTypeSavoirFaire) {
    this.codeTypeSavoirFaire = codeTypeSavoirFaire;
    return this;
  }

  /**
   * Get codeTypeSavoirFaire
   * @return codeTypeSavoirFaire
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeTypeSavoirFaire() {
    return codeTypeSavoirFaire;
  }

  public void setCodeTypeSavoirFaire(String codeTypeSavoirFaire) {
    this.codeTypeSavoirFaire = codeTypeSavoirFaire;
  }

  public Exercice codeSavoirFaire(String codeSavoirFaire) {
    this.codeSavoirFaire = codeSavoirFaire;
    return this;
  }

  /**
   * Get codeSavoirFaire
   * @return codeSavoirFaire
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeSavoirFaire() {
    return codeSavoirFaire;
  }

  public void setCodeSavoirFaire(String codeSavoirFaire) {
    this.codeSavoirFaire = codeSavoirFaire;
  }

  public Exercice activities(Set<Activitie> activities) {
    this.activities = activities;
    return this;
  }

  public Exercice addActivitiesItem(Activitie activitiesItem) {
    this.activities.add(activitiesItem);
    return this;
  }

  /**
   * Get activities
   * @return activities
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
@Size(min=1) 
  public Set<Activitie> getActivities() {
    return activities;
  }

  public void setActivities(Set<Activitie> activities) {
    this.activities = activities;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Exercice exercice = (Exercice) o;
    return Objects.equals(this.codeProfession, exercice.codeProfession) &&
        Objects.equals(this.codeCategorieProfessionnelle, exercice.codeCategorieProfessionnelle) &&
        Objects.equals(this.codeCiviliteDexercice, exercice.codeCiviliteDexercice) &&
        Objects.equals(this.nomDexercice, exercice.nomDexercice) &&
        Objects.equals(this.prenomDexercice, exercice.prenomDexercice) &&
        Objects.equals(this.codeTypeSavoirFaire, exercice.codeTypeSavoirFaire) &&
        Objects.equals(this.codeSavoirFaire, exercice.codeSavoirFaire) &&
        Objects.equals(this.activities, exercice.activities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeProfession, codeCategorieProfessionnelle, codeCiviliteDexercice, nomDexercice, prenomDexercice, codeTypeSavoirFaire, codeSavoirFaire, activities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Exercice {\n");
    
    sb.append("    codeProfession: ").append(toIndentedString(codeProfession)).append("\n");
    sb.append("    codeCategorieProfessionnelle: ").append(toIndentedString(codeCategorieProfessionnelle)).append("\n");
    sb.append("    codeCiviliteDexercice: ").append(toIndentedString(codeCiviliteDexercice)).append("\n");
    sb.append("    nomDexercice: ").append(toIndentedString(nomDexercice)).append("\n");
    sb.append("    prenomDexercice: ").append(toIndentedString(prenomDexercice)).append("\n");
    sb.append("    codeTypeSavoirFaire: ").append(toIndentedString(codeTypeSavoirFaire)).append("\n");
    sb.append("    codeSavoirFaire: ").append(toIndentedString(codeSavoirFaire)).append("\n");
    sb.append("    activities: ").append(toIndentedString(activities)).append("\n");
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

