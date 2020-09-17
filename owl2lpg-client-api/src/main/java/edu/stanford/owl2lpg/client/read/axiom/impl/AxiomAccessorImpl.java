package edu.stanford.owl2lpg.client.read.axiom.impl;

import com.google.common.collect.ImmutableSet;
import edu.stanford.owl2lpg.client.read.GraphReader;
import edu.stanford.owl2lpg.client.read.NodeMapper;
import edu.stanford.owl2lpg.client.read.Parameters;
import edu.stanford.owl2lpg.client.read.axiom.AxiomAccessor;
import edu.stanford.owl2lpg.model.BranchId;
import edu.stanford.owl2lpg.model.OntologyDocumentId;
import edu.stanford.owl2lpg.model.ProjectId;
import org.neo4j.driver.Value;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.owl2lpg.client.util.Resources.read;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.ANNOTATION_AXIOM;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.AXIOM;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.SUB_ANNOTATION_PROPERTY_OF;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class AxiomAccessorImpl implements AxiomAccessor {

  private static final String ALL_AXIOM_QUERY_FILE = "axioms/all-axioms.cpy";
  private static final String AXIOM_BY_TYPE_QUERY_FILE = "axioms/axiom-by-type.cpy";
  private static final String AXIOM_BY_SUBJECT_CLASS_QUERY_FILE = "axioms/axiom-by-subject-class.cpy";
  private static final String AXIOM_BY_SUBJECT_DATA_PROPERTY_QUERY_FILE = "axioms/axiom-by-subject-data-property.cpy";
  private static final String AXIOM_BY_SUBJECT_OBJECT_PROPERTY_QUERY_FILE = "axioms/axiom-by-subject-object-property.cpy";
  private static final String AXIOM_BY_SUBJECT_ANNOTATION_PROPERTY_QUERY_FILE = "axioms/axiom-by-subject-annotation-property.cpy";
  private static final String AXIOM_BY_SUBJECT_NAMED_INDIVIDUAL_QUERY_FILE = "axioms/axiom-by-subject-named-individual.cpy";
  private static final String AXIOM_BY_SUBJECT_DATATYPE_QUERY_FILE = "axioms/axiom-by-subject-datatype.cpy";
  private static final String ANNOTATION_AXIOM_BY_SUBJECT_IRI_QUERY_FILE = "axioms/annotation-axiom-by-subject-iri.cpy";
  private static final String SUB_ANNOTATION_PROPERTY_OF_AXIOMS_BY_SUPER_PROPERTY_QUERY_FILE =
      "axioms/sub-annotation-property-of-axiom-by-super-property.cpy";

  private static final String ALL_AXIOM_QUERY = read(ALL_AXIOM_QUERY_FILE);
  private static final String AXIOM_BY_TYPE_QUERY = read(AXIOM_BY_TYPE_QUERY_FILE);
  private static final String AXIOM_BY_SUBJECT_CLASS_QUERY = read(AXIOM_BY_SUBJECT_CLASS_QUERY_FILE);
  private static final String AXIOM_BY_SUBJECT_DATA_PROPERTY_QUERY = read(AXIOM_BY_SUBJECT_DATA_PROPERTY_QUERY_FILE);
  private static final String AXIOM_BY_SUBJECT_OBJECT_PROPERTY_QUERY = read(AXIOM_BY_SUBJECT_OBJECT_PROPERTY_QUERY_FILE);
  private static final String AXIOM_BY_SUBJECT_ANNOTATION_PROPERTY_QUERY = read(AXIOM_BY_SUBJECT_ANNOTATION_PROPERTY_QUERY_FILE);
  private static final String AXIOM_BY_SUBJECT_NAMED_INDIVIDUAL_QUERY = read(AXIOM_BY_SUBJECT_NAMED_INDIVIDUAL_QUERY_FILE);
  private static final String AXIOM_BY_SUBJECT_DATATYPE_QUERY = read(AXIOM_BY_SUBJECT_DATATYPE_QUERY_FILE);
  private static final String ANNOTATION_AXIOM_BY_SUBJECT_IRI_QUERY = read(ANNOTATION_AXIOM_BY_SUBJECT_IRI_QUERY_FILE);
  private static final String SUB_ANNOTATION_PROPERTY_OF_AXIOMS_BY_SUPER_PROPERTY_QUERY =
      read(SUB_ANNOTATION_PROPERTY_OF_AXIOMS_BY_SUPER_PROPERTY_QUERY_FILE);

  @Nonnull
  private final GraphReader graphReader;

  @Nonnull
  private final NodeMapper nodeMapper;

  @Inject
  public AxiomAccessorImpl(@Nonnull GraphReader graphReader,
                           @Nonnull NodeMapper nodeMapper) {
    this.graphReader = checkNotNull(graphReader);
    this.nodeMapper = checkNotNull(nodeMapper);
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom> getAllAxioms(@Nonnull ProjectId projectId,
                                             @Nonnull BranchId branchId,
                                             @Nonnull OntologyDocumentId ontoDocId) {
    var inputParams = Parameters.forContext(projectId, branchId, ontoDocId);
    var nodeIndex = graphReader.getNodeIndex(ALL_AXIOM_QUERY, inputParams);
    return nodeIndex.getNodes(AXIOM.getMainLabel())
        .stream()
        .map(axiomNode -> nodeMapper.toObject(axiomNode, nodeIndex, OWLAxiom.class))
        .collect(ImmutableSet.toImmutableSet());
  }

  @Nonnull
  @Override
  public <T extends OWLAxiom> ImmutableSet<T> getAxiomsByType(@Nonnull AxiomType<T> axiomType,
                                                              @Nonnull ProjectId projectId,
                                                              @Nonnull BranchId branchId,
                                                              @Nonnull OntologyDocumentId ontoDocId) {
    var inputParams = Parameters.forAxiomType(axiomType, projectId, branchId, ontoDocId);
    var nodeIndex = graphReader.getNodeIndex(AXIOM_BY_TYPE_QUERY, inputParams);
    return nodeIndex.getNodes(axiomType.getName())
        .stream()
        .map(axiomNode -> nodeMapper.toObject(axiomNode, nodeIndex, axiomType.getActualClass()))
        .collect(ImmutableSet.toImmutableSet());
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubject(@Nonnull OWLClass subject,
                     @Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId ontoDocId) {
    return getAxiomsBySubject(AXIOM_BY_SUBJECT_CLASS_QUERY, subject, projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubject(@Nonnull OWLDataProperty subject,
                     @Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId ontoDocId) {
    return getAxiomsBySubject(AXIOM_BY_SUBJECT_DATA_PROPERTY_QUERY, subject, projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubject(@Nonnull OWLObjectProperty subject,
                     @Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId ontoDocId) {
    return getAxiomsBySubject(AXIOM_BY_SUBJECT_OBJECT_PROPERTY_QUERY, subject, projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubject(@Nonnull OWLAnnotationProperty subject,
                     @Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId ontoDocId) {
    return getAxiomsBySubject(AXIOM_BY_SUBJECT_ANNOTATION_PROPERTY_QUERY, subject, projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubject(@Nonnull OWLNamedIndividual subject,
                     @Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId ontoDocId) {
    return getAxiomsBySubject(AXIOM_BY_SUBJECT_NAMED_INDIVIDUAL_QUERY, subject, projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubject(@Nonnull OWLDatatype subject,
                     @Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId ontoDocId) {
    return getAxiomsBySubject(AXIOM_BY_SUBJECT_DATATYPE_QUERY, subject, projectId, branchId, ontoDocId);
  }

  @Nonnull
  private ImmutableSet<OWLAxiom> getAxiomsBySubject(String queryString, OWLEntity subject,
                                                    ProjectId projectId,
                                                    BranchId branchId,
                                                    OntologyDocumentId ontoDocId) {
    var inputParams = createInputParams(subject, projectId, branchId, ontoDocId);
    var nodeIndex = graphReader.getNodeIndex(queryString, inputParams);
    return nodeIndex.getNodes(AXIOM.getMainLabel())
        .stream()
        .map(axiomNode -> nodeMapper.toObject(axiomNode, nodeIndex, OWLAxiom.class))
        .collect(ImmutableSet.toImmutableSet());
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubject(@Nonnull OWLEntity subject,
                     @Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId ontoDocId) {
    if (subject.isOWLClass()) {
      return getAxiomsBySubject(subject.asOWLClass(), projectId, branchId, ontoDocId);
    } else if (subject.isOWLDataProperty()) {
      return getAxiomsBySubject(subject.asOWLDataProperty(), projectId, branchId, ontoDocId);
    } else if (subject.isOWLObjectProperty()) {
      return getAxiomsBySubject(subject.asOWLObjectProperty(), projectId, branchId, ontoDocId);
    } else if (subject.isOWLAnnotationProperty()) {
      return getAxiomsBySubject(subject.asOWLAnnotationProperty(), projectId, branchId, ontoDocId);
    } else if (subject.isOWLNamedIndividual()) {
      return getAxiomsBySubject(subject.asOWLNamedIndividual(), projectId, branchId, ontoDocId);
    } else { // must be a datatype
      return getAxiomsBySubject(subject.asOWLDatatype(), projectId, branchId, ontoDocId);
    }
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAxiom>
  getAxiomsBySubjects(@Nonnull Collection<OWLEntity> entities,
                      @Nonnull ProjectId projectId,
                      @Nonnull BranchId branchId,
                      @Nonnull OntologyDocumentId ontoDocId) {
    return entities.stream()
        .flatMap(entity -> getAxiomsBySubject(entity, projectId, branchId, ontoDocId).stream())
        .collect(ImmutableSet.toImmutableSet());
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLAnnotationAxiom>
  getAnnotationAxioms(@Nonnull IRI entityIri,
                      @Nonnull ProjectId projectId,
                      @Nonnull BranchId branchId,
                      @Nonnull OntologyDocumentId ontoDocId) {
    var inputParams = createInputParams(entityIri, projectId, branchId, ontoDocId);
    var nodeIndex = graphReader.getNodeIndex(ANNOTATION_AXIOM_BY_SUBJECT_IRI_QUERY, inputParams);
    return nodeIndex.getNodes(ANNOTATION_AXIOM.getMainLabel())
        .stream()
        .map(axiomNode -> nodeMapper.toObject(axiomNode, nodeIndex, OWLAnnotationAxiom.class))
        .collect(ImmutableSet.toImmutableSet());
  }

  @Nonnull
  @Override
  public ImmutableSet<OWLSubAnnotationPropertyOfAxiom>
  getSubAnnotationPropertyOfAxiomsBySuperProperty(@Nonnull OWLAnnotationProperty superProperty,
                                                  @Nonnull ProjectId projectId,
                                                  @Nonnull BranchId branchId,
                                                  @Nonnull OntologyDocumentId ontoDocId) {
    var inputParams = createInputParams(superProperty, projectId, branchId, ontoDocId);
    var nodeIndex = graphReader.getNodeIndex(SUB_ANNOTATION_PROPERTY_OF_AXIOMS_BY_SUPER_PROPERTY_QUERY, inputParams);
    return nodeIndex.getNodes(SUB_ANNOTATION_PROPERTY_OF.getMainLabel())
        .stream()
        .map(axiomNode -> nodeMapper.toObject(axiomNode, nodeIndex, OWLSubAnnotationPropertyOfAxiom.class))
        .collect(ImmutableSet.toImmutableSet());
  }

  @Nonnull
  private static Value createInputParams(OWLEntity entity, ProjectId projectId, BranchId branchId, OntologyDocumentId ontoDocId) {
    return createInputParams(entity.getIRI(), projectId, branchId, ontoDocId);
  }

  @Nonnull
  private static Value createInputParams(IRI entityIri, ProjectId projectId, BranchId branchId, OntologyDocumentId ontoDocId) {
    return Parameters.forEntityIri(entityIri, projectId, branchId, ontoDocId);
  }
}
