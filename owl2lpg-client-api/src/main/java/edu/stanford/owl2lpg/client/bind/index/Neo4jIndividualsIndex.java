package edu.stanford.owl2lpg.client.bind.index;

import edu.stanford.bmir.protege.web.server.index.IndividualsIndex;
import edu.stanford.bmir.protege.web.server.index.IndividualsQueryResult;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.owl2lpg.client.read.axiom.AssertionAxiomBySubjectAccessor;
import edu.stanford.owl2lpg.client.read.axiom.AxiomContext;
import edu.stanford.owl2lpg.model.BranchId;
import edu.stanford.owl2lpg.model.OntologyDocumentId;
import edu.stanford.owl2lpg.model.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jIndividualsIndex implements IndividualsIndex {

  @Nonnull
  private final ProjectId projectId;

  @Nonnull
  private final BranchId branchId;

  @Nonnull
  private final OntologyDocumentId ontoDocId;

  @Nonnull
  private final AssertionAxiomBySubjectAccessor assertionAxiomBySubjectAccessor;

  @Nonnull
  private final Neo4jIndividualsByNameIndex individualsByNameIndex;

  @Nonnull
  private final Neo4jIndividualsBySubjectIndex individualsBySubjectIndex;

  @Inject
  public Neo4jIndividualsIndex(@Nonnull ProjectId projectId,
                               @Nonnull BranchId branchId,
                               @Nonnull OntologyDocumentId ontoDocId,
                               @Nonnull Neo4jIndividualsByNameIndex individualsByNameIndex,
                               @Nonnull Neo4jIndividualsBySubjectIndex individualsBySubjectIndex,
                               @Nonnull AssertionAxiomBySubjectAccessor assertionAxiomBySubjectAccessor) {
    this.projectId = checkNotNull(projectId);
    this.branchId = checkNotNull(branchId);
    this.ontoDocId = checkNotNull(ontoDocId);
    this.individualsByNameIndex = checkNotNull(individualsByNameIndex);
    this.individualsBySubjectIndex = checkNotNull(individualsBySubjectIndex);
    this.assertionAxiomBySubjectAccessor = checkNotNull(assertionAxiomBySubjectAccessor);
  }

  @Nonnull
  @Override
  public IndividualsQueryResult getIndividuals(@Nonnull String searchString,
                                               @Nonnull PageRequest pageRequest) {
    return individualsByNameIndex.getIndividuals(searchString, pageRequest);
  }

  @Nonnull
  @Override
  public IndividualsQueryResult getIndividuals(@Nonnull OWLClass owlClass,
                                               @Nonnull InstanceRetrievalMode instanceRetrievalMode,
                                               @Nonnull String searchString,
                                               @Nonnull PageRequest pageRequest) {
    return individualsByNameIndex.getIndividuals(owlClass, instanceRetrievalMode, searchString, pageRequest);
  }

  @Nonnull
  @Override
  public IndividualsQueryResult getIndividualsPageContaining(@Nonnull OWLNamedIndividual individual,
                                                             @Nonnull Optional<OWLClass> owlClass,
                                                             @Nonnull InstanceRetrievalMode instanceRetrievalMode,
                                                             int pageSize) {
    return individualsBySubjectIndex.getIndividualsPageContaining(individual, owlClass, instanceRetrievalMode, pageSize);
  }

  @Nonnull
  @Override
  public Stream<OWLClass> getTypes(@Nonnull OWLNamedIndividual owlNamedIndividual) {
    return assertionAxiomBySubjectAccessor.getClassAssertionsForSubject(owlNamedIndividual, AxiomContext.create(projectId, branchId, ontoDocId))
        .stream()
        .map(OWLClassAssertionAxiom::getClassExpression)
        .filter(OWLClassExpression::isNamed)
        .map(OWLClassExpression::asOWLClass);
  }
}
