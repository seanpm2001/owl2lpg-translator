package edu.stanford.owl2lpg.client.bind.index;

import edu.stanford.bmir.protege.web.server.index.OntologySignatureByTypeIndex;
import edu.stanford.owl2lpg.client.DocumentIdMap;
import edu.stanford.owl2lpg.client.read.ontology.OntologyAccessor;
import edu.stanford.owl2lpg.model.BranchId;
import edu.stanford.owl2lpg.model.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jOntologySignatureByTypeIndex implements OntologySignatureByTypeIndex {

  @Nonnull
  private final ProjectId projectId;

  @Nonnull
  private final BranchId branchId;

  @Nonnull
  private final DocumentIdMap documentIdMap;

  @Nonnull
  private final OntologyAccessor ontologyAccessor;

  @Inject
  public Neo4jOntologySignatureByTypeIndex(@Nonnull ProjectId projectId,
                                           @Nonnull BranchId branchId,
                                           @Nonnull DocumentIdMap documentIdMap,
                                           @Nonnull OntologyAccessor ontologyAccessor) {
    this.projectId = checkNotNull(projectId);
    this.branchId = checkNotNull(branchId);
    this.documentIdMap = checkNotNull(documentIdMap);
    this.ontologyAccessor = checkNotNull(ontologyAccessor);
  }

  @Nonnull
  @Override
  public <E extends OWLEntity> Stream<E> getSignature(@Nonnull EntityType<E> entityType,
                                                      @Nonnull OWLOntologyID ontologyId) {
    var documentId = documentIdMap.get(projectId, ontologyId);
    return ontologyAccessor.getEntitiesByType(entityType, projectId, branchId, documentId).stream();
  }
}
