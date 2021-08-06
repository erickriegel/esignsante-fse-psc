package fr.ans.api.sign.esignsante.psc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fr.ans.api.sign.esignsante.psc.model.SubjectRefPro;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-08-06T10:37:07.695444100+02:00[Europe/Paris]")
public class UserInfo   {
  @JsonProperty("Secteur_Activite")
  private String secteurActivite;

  @JsonProperty("sub")
  private String sub;

  @JsonProperty("email_verified")
  private Boolean emailVerified;

  @JsonProperty("SubjectOrganization")
  private String subjectOrganization;

  @JsonProperty("Mode_Acces_Raison")
  private String modeAccesRaison;

  @JsonProperty("preferred_username")
  private String preferredUsername;

  @JsonProperty("given_name")
  private String givenName;

  @JsonProperty("Acces_Regulation_Medicale")
  private String accesRegulationMedicale;

  @JsonProperty("UITVersion")
  private String uiTVersion;

  @JsonProperty("Palier_Authentification")
  private String palierAuthentification;

  @JsonProperty("SubjectRefPro")
  private SubjectRefPro subjectRefPro;

  @JsonProperty("SubjectOrganizationID")
  private String subjectOrganizationID;

  @JsonProperty("SubjectRole")
  @Valid
  private List<String> subjectRole = new ArrayList<String>();

  @JsonProperty("PSI_Locale")
  private String psILocale;

  @JsonProperty("SubjectNameID")
  private String subjectNameID;

  @JsonProperty("family_name")
  private String familyName;

  public UserInfo secteurActivite(String secteurActivite) {
    this.secteurActivite = secteurActivite;
    return this;
  }

  /**
   * Get secteurActivite
   * @return secteurActivite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getSecteurActivite() {
    return secteurActivite;
  }

  public void setSecteurActivite(String secteurActivite) {
    this.secteurActivite = secteurActivite;
  }

  public UserInfo sub(String sub) {
    this.sub = sub;
    return this;
  }

  /**
   * Get sub
   * @return sub
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getSub() {
    return sub;
  }

  public void setSub(String sub) {
    this.sub = sub;
  }

  public UserInfo emailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
    return this;
  }

  /**
   * Get emailVerified
   * @return emailVerified
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Boolean getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public UserInfo subjectOrganization(String subjectOrganization) {
    this.subjectOrganization = subjectOrganization;
    return this;
  }

  /**
   * Get subjectOrganization
   * @return subjectOrganization
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getSubjectOrganization() {
    return subjectOrganization;
  }

  public void setSubjectOrganization(String subjectOrganization) {
    this.subjectOrganization = subjectOrganization;
  }

  public UserInfo modeAccesRaison(String modeAccesRaison) {
    this.modeAccesRaison = modeAccesRaison;
    return this;
  }

  /**
   * Get modeAccesRaison
   * @return modeAccesRaison
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getModeAccesRaison() {
    return modeAccesRaison;
  }

  public void setModeAccesRaison(String modeAccesRaison) {
    this.modeAccesRaison = modeAccesRaison;
  }

  public UserInfo preferredUsername(String preferredUsername) {
    this.preferredUsername = preferredUsername;
    return this;
  }

  /**
   * Get preferredUsername
   * @return preferredUsername
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getPreferredUsername() {
    return preferredUsername;
  }

  public void setPreferredUsername(String preferredUsername) {
    this.preferredUsername = preferredUsername;
  }

  public UserInfo givenName(String givenName) {
    this.givenName = givenName;
    return this;
  }

  /**
   * Get givenName
   * @return givenName
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public UserInfo accesRegulationMedicale(String accesRegulationMedicale) {
    this.accesRegulationMedicale = accesRegulationMedicale;
    return this;
  }

  /**
   * Get accesRegulationMedicale
   * @return accesRegulationMedicale
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getAccesRegulationMedicale() {
    return accesRegulationMedicale;
  }

  public void setAccesRegulationMedicale(String accesRegulationMedicale) {
    this.accesRegulationMedicale = accesRegulationMedicale;
  }

  public UserInfo uiTVersion(String uiTVersion) {
    this.uiTVersion = uiTVersion;
    return this;
  }

  /**
   * Get uiTVersion
   * @return uiTVersion
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getUiTVersion() {
    return uiTVersion;
  }

  public void setUiTVersion(String uiTVersion) {
    this.uiTVersion = uiTVersion;
  }

  public UserInfo palierAuthentification(String palierAuthentification) {
    this.palierAuthentification = palierAuthentification;
    return this;
  }

  /**
   * Get palierAuthentification
   * @return palierAuthentification
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getPalierAuthentification() {
    return palierAuthentification;
  }

  public void setPalierAuthentification(String palierAuthentification) {
    this.palierAuthentification = palierAuthentification;
  }

  public UserInfo subjectRefPro(SubjectRefPro subjectRefPro) {
    this.subjectRefPro = subjectRefPro;
    return this;
  }

  /**
   * Get subjectRefPro
   * @return subjectRefPro
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public SubjectRefPro getSubjectRefPro() {
    return subjectRefPro;
  }

  public void setSubjectRefPro(SubjectRefPro subjectRefPro) {
    this.subjectRefPro = subjectRefPro;
  }

  public UserInfo subjectOrganizationID(String subjectOrganizationID) {
    this.subjectOrganizationID = subjectOrganizationID;
    return this;
  }

  /**
   * Get subjectOrganizationID
   * @return subjectOrganizationID
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getSubjectOrganizationID() {
    return subjectOrganizationID;
  }

  public void setSubjectOrganizationID(String subjectOrganizationID) {
    this.subjectOrganizationID = subjectOrganizationID;
  }

  public UserInfo subjectRole(List<String> subjectRole) {
    this.subjectRole = subjectRole;
    return this;
  }

  public UserInfo addSubjectRoleItem(String subjectRoleItem) {
    this.subjectRole.add(subjectRoleItem);
    return this;
  }

  /**
   * Get subjectRole
   * @return subjectRole
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public List<String> getSubjectRole() {
    return subjectRole;
  }

  public void setSubjectRole(List<String> subjectRole) {
    this.subjectRole = subjectRole;
  }

  public UserInfo psILocale(String psILocale) {
    this.psILocale = psILocale;
    return this;
  }

  /**
   * Get psILocale
   * @return psILocale
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getPsILocale() {
    return psILocale;
  }

  public void setPsILocale(String psILocale) {
    this.psILocale = psILocale;
  }

  public UserInfo subjectNameID(String subjectNameID) {
    this.subjectNameID = subjectNameID;
    return this;
  }

  /**
   * Get subjectNameID
   * @return subjectNameID
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getSubjectNameID() {
    return subjectNameID;
  }

  public void setSubjectNameID(String subjectNameID) {
    this.subjectNameID = subjectNameID;
  }

  public UserInfo familyName(String familyName) {
    this.familyName = familyName;
    return this;
  }

  /**
   * Get familyName
   * @return familyName
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(this.secteurActivite, userInfo.secteurActivite) &&
        Objects.equals(this.sub, userInfo.sub) &&
        Objects.equals(this.emailVerified, userInfo.emailVerified) &&
        Objects.equals(this.subjectOrganization, userInfo.subjectOrganization) &&
        Objects.equals(this.modeAccesRaison, userInfo.modeAccesRaison) &&
        Objects.equals(this.preferredUsername, userInfo.preferredUsername) &&
        Objects.equals(this.givenName, userInfo.givenName) &&
        Objects.equals(this.accesRegulationMedicale, userInfo.accesRegulationMedicale) &&
        Objects.equals(this.uiTVersion, userInfo.uiTVersion) &&
        Objects.equals(this.palierAuthentification, userInfo.palierAuthentification) &&
        Objects.equals(this.subjectRefPro, userInfo.subjectRefPro) &&
        Objects.equals(this.subjectOrganizationID, userInfo.subjectOrganizationID) &&
        Objects.equals(this.subjectRole, userInfo.subjectRole) &&
        Objects.equals(this.psILocale, userInfo.psILocale) &&
        Objects.equals(this.subjectNameID, userInfo.subjectNameID) &&
        Objects.equals(this.familyName, userInfo.familyName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(secteurActivite, sub, emailVerified, subjectOrganization, modeAccesRaison, preferredUsername, givenName, accesRegulationMedicale, uiTVersion, palierAuthentification, subjectRefPro, subjectOrganizationID, subjectRole, psILocale, subjectNameID, familyName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("    secteurActivite: ").append(toIndentedString(secteurActivite)).append("\n");
    sb.append("    sub: ").append(toIndentedString(sub)).append("\n");
    sb.append("    emailVerified: ").append(toIndentedString(emailVerified)).append("\n");
    sb.append("    subjectOrganization: ").append(toIndentedString(subjectOrganization)).append("\n");
    sb.append("    modeAccesRaison: ").append(toIndentedString(modeAccesRaison)).append("\n");
    sb.append("    preferredUsername: ").append(toIndentedString(preferredUsername)).append("\n");
    sb.append("    givenName: ").append(toIndentedString(givenName)).append("\n");
    sb.append("    accesRegulationMedicale: ").append(toIndentedString(accesRegulationMedicale)).append("\n");
    sb.append("    uiTVersion: ").append(toIndentedString(uiTVersion)).append("\n");
    sb.append("    palierAuthentification: ").append(toIndentedString(palierAuthentification)).append("\n");
    sb.append("    subjectRefPro: ").append(toIndentedString(subjectRefPro)).append("\n");
    sb.append("    subjectOrganizationID: ").append(toIndentedString(subjectOrganizationID)).append("\n");
    sb.append("    subjectRole: ").append(toIndentedString(subjectRole)).append("\n");
    sb.append("    psILocale: ").append(toIndentedString(psILocale)).append("\n");
    sb.append("    subjectNameID: ").append(toIndentedString(subjectNameID)).append("\n");
    sb.append("    familyName: ").append(toIndentedString(familyName)).append("\n");
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

