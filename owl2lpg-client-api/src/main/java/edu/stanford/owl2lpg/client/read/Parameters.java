package edu.stanford.owl2lpg.client.read;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.owl2lpg.client.read.axiom.AxiomContext;
import edu.stanford.owl2lpg.model.BranchId;
import edu.stanford.owl2lpg.model.ProjectId;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.ListValue;
import org.neo4j.driver.internal.value.MapValue;
import org.neo4j.driver.internal.value.StringValue;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Parameters {

  public static Value forShortFormsDictionary(@Nonnull ProjectId projectId,
                                              @Nonnull BranchId branchId,
                                              @Nonnull IRI entityIri) {
    return new MapValue(Map.of(
        "projectId", new StringValue(projectId.getIdentifier()),
        "branchId", new StringValue(branchId.getIdentifier()),
        "entityIri", new StringValue(entityIri.toString())));
  }

  public static Value forShortFormsIndex(@Nonnull ProjectId projectId,
                                         @Nonnull BranchId branchId,
                                         @Nonnull String entityName) {
    return new MapValue(Map.of(
        "projectId", new StringValue(projectId.getIdentifier()),
        "branchId", new StringValue(branchId.getIdentifier()),
        "entityName", new StringValue(entityName)));
  }

  public static Value forShortFormsContaining(ProjectId projectId,
                                              BranchId branchId,
                                              List<SearchString> searchStrings) {
    return new MapValue(Map.of(
        "projectId", new StringValue(projectId.getIdentifier()),
        "branchId", new StringValue(branchId.getIdentifier()),
        "searchString", new StringValue(searchStrings.stream()
            .map(SearchString::getSearchString)
            .map(s -> s + "*")
            .collect(joining(" AND ")))));
  }

  public static Value forEntityIri(@Nonnull AxiomContext context, @Nonnull IRI entityIri) {
    return new MapValue(Map.of(
        "projectId", new StringValue(context.getProjectId().getIdentifier()),
        "branchId", new StringValue(context.getBranchId().getIdentifier()),
        "ontoDocId", new StringValue(context.getOntologyDocumentId().getIdentifier()),
        "entityIri", new StringValue(entityIri.toString())));
  }

  public static Value forEntity(@Nonnull AxiomContext context, @Nonnull OWLEntity entity) {
    return forEntityIri(context, entity.getIRI());
  }

  public static MapValue forShortForm(@Nonnull ImmutableList<IRI> entities,
                                      @Nonnull ImmutableList<IRI> annotationProperties) {
    return new MapValue(Map.of(
        "entityIriList", toListValue(entities),
        "annotationPropertyIriList", toListValue(annotationProperties)));
  }

  public static MapValue forShortForm(@Nonnull IRI entity,
                                      @Nonnull ImmutableList<IRI> annotationProperties) {
    return forShortForm(ImmutableList.of(entity), annotationProperties);
  }

  private static ListValue toListValue(ImmutableList<IRI> list) {
    var values = list
        .stream()
        .map(IRI::toString)
        .map(StringValue::new)
        .toArray(Value[]::new);
    return new ListValue(values);
  }
}
