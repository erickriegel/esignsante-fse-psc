package fr.ans.api.sign.esignsante.psc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fr.ans.api.sign.esignsante.psc.model.Exercice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SubjectRefPro
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-08-06T10:38:02.434500100+02:00[Europe/Paris]")
public class SubjectRefPro   {
  @JsonProperty("codeCivilite")
  private String codeCivilite;

  @JsonProperty("exercices")
  @Valid
  private Set<Exercice> exercices = new LinkedHashSet<Exercice>();

  public SubjectRefPro codeCivilite(String codeCivilite) {
    this.codeCivilite = codeCivilite;
    return this;
  }

  /**
   * Get codeCivilite
   * @return codeCivilite
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=1) 
  public String getCodeCivilite() {
    return codeCivilite;
  }

  public void setCodeCivilite(String codeCivilite) {
    this.codeCivilite = codeCivilite;
  }

  public SubjectRefPro exercices(Set<Exercice> exercices) {
    this.exercices = exercices;
    return this;
  }

  public SubjectRefPro addExercicesItem(Exercice exercicesItem) {
    this.exercices.add(exercicesItem);
    return this;
  }

  /**
   * Get exercices
   * @return exercices
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
@Size(min=1) 
  public Set<Exercice> getExercices() {
    return exercices;
  }

  public void setExercices(Set<Exercice> exercices) {
    this.exercices = exercices;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubjectRefPro subjectRefPro = (SubjectRefPro) o;
    return Objects.equals(this.codeCivilite, subjectRefPro.codeCivilite) &&
        Objects.equals(this.exercices, subjectRefPro.exercices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeCivilite, exercices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubjectRefPro {\n");
    
    sb.append("    codeCivilite: ").append(toIndentedString(codeCivilite)).append("\n");
    sb.append("    exercices: ").append(toIndentedString(exercices)).append("\n");
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

