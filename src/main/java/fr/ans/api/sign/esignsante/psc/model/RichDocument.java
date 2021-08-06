package fr.ans.api.sign.esignsante.psc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fr.ans.api.sign.esignsante.psc.model.Document;
import fr.ans.api.sign.esignsante.psc.model.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * RichDocument
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-08-06T10:33:44.280737500+02:00[Europe/Paris]")
public class RichDocument   {
  @JsonProperty("document")
  private Document document;

  @JsonProperty("userinfo")
  private UserInfo userinfo;

  public RichDocument document(Document document) {
    this.document = document;
    return this;
  }

  /**
   * Get document
   * @return document
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public RichDocument userinfo(UserInfo userinfo) {
    this.userinfo = userinfo;
    return this;
  }

  /**
   * Get userinfo
   * @return userinfo
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public UserInfo getUserinfo() {
    return userinfo;
  }

  public void setUserinfo(UserInfo userinfo) {
    this.userinfo = userinfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RichDocument richDocument = (RichDocument) o;
    return Objects.equals(this.document, richDocument.document) &&
        Objects.equals(this.userinfo, richDocument.userinfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(document, userinfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RichDocument {\n");
    
    sb.append("    document: ").append(toIndentedString(document)).append("\n");
    sb.append("    userinfo: ").append(toIndentedString(userinfo)).append("\n");
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

