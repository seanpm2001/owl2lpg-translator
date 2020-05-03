package edu.stanford.owl2lpg.translator.visitors;

import com.google.common.collect.ImmutableList;
import edu.stanford.owl2lpg.model.Node;
import edu.stanford.owl2lpg.translator.Translation;
import edu.stanford.owl2lpg.translator.vocab.EdgeLabels;
import edu.stanford.owl2lpg.translator.vocab.NodeLabels;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A visitor that contains the implementation to translate the OWL 2 literals.
 *
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class ClassExpressionVisitor extends VisitorBase
    implements OWLClassExpressionVisitorEx<Translation> {

  private Node mainNode;

  private final VisitorFactory visitorFactory;

  @Inject
  public ClassExpressionVisitor(@Nonnull VisitorFactory visitorFactory) {
    super(visitorFactory.getNodeIdMapper());
    this.visitorFactory = checkNotNull(visitorFactory);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLClass c) {
    return visitorFactory.createEntityVisitor().visit(c);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectIntersectionOf ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_INTERSECTION_OF);
    var classExprEdges = createEdges(ce.getOperands(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslations = createNestedTranslations(ce.getOperands());
    return Translation.create(mainNode,
        ImmutableList.copyOf(classExprEdges),
        ImmutableList.copyOf(classExprTranslations));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectUnionOf ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_UNION_OF);
    var classExprEdges = createEdges(ce.getOperands(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslations = createNestedTranslations(ce.getOperands());
    return Translation.create(mainNode,
        ImmutableList.copyOf(classExprEdges),
        ImmutableList.copyOf(classExprTranslations));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectComplementOf ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_COMPLEMENT_OF);
    var classExprEdge = createEdge(ce.getOperand(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslation = createNestedTranslation(ce.getOperand());
    return Translation.create(mainNode,
        ImmutableList.of(classExprEdge),
        ImmutableList.of(classExprTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectSomeValuesFrom ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_SOME_VALUES_FROM);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.OBJECT_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var classExprEdge = createEdge(ce.getFiller(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, classExprEdge),
        ImmutableList.of(propertyExprTranslation, classExprTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectAllValuesFrom ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_ALL_VALUES_FROM);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.OBJECT_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var classExprEdge = createEdge(ce.getFiller(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, classExprEdge),
        ImmutableList.of(propertyExprTranslation, classExprTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectHasValue ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_HAS_VALUE);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.OBJECT_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var individualEdge = createEdge(ce.getFiller(), EdgeLabels.INDIVIDUAL);
    var individualTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, individualEdge),
        ImmutableList.of(propertyExprTranslation, individualTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectMinCardinality ce) {
    mainNode = createCardinalityNode(ce, NodeLabels.OBJECT_MIN_CARDINALITY);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.OBJECT_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var classExprEdge = createEdge(ce.getFiller(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, classExprEdge),
        ImmutableList.of(propertyExprTranslation, classExprTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectExactCardinality ce) {
    mainNode = createCardinalityNode(ce, NodeLabels.OBJECT_EXACT_CARDINALITY);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.OBJECT_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var classExprEdge = createEdge(ce.getFiller(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, classExprEdge),
        ImmutableList.of(propertyExprTranslation, classExprTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectMaxCardinality ce) {
    mainNode = createCardinalityNode(ce, NodeLabels.OBJECT_MAX_CARDINALITY);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.OBJECT_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var classExprEdge = createEdge(ce.getFiller(), EdgeLabels.CLASS_EXPRESSION);
    var classExprTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, classExprEdge),
        ImmutableList.of(propertyExprTranslation, classExprTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectHasSelf ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_HAS_SELF);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.OBJECT_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge),
        ImmutableList.of(propertyExprTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLObjectOneOf ce) {
    mainNode = createNode(ce, NodeLabels.OBJECT_ONE_OF);
    var individualEdges = createEdges(ce.getIndividuals(), EdgeLabels.INDIVIDUAL);
    var individualTranslations = createNestedTranslations(ce.getIndividuals());
    return Translation.create(mainNode,
        ImmutableList.copyOf(individualEdges),
        ImmutableList.copyOf(individualTranslations));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLDataSomeValuesFrom ce) {
    mainNode = createNode(ce, NodeLabels.DATA_SOME_VALUES_FROM);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.DATA_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var dataRangeEdge = createEdge(ce.getFiller(), EdgeLabels.DATA_RANGE);
    var dataRangeTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, dataRangeEdge),
        ImmutableList.of(propertyExprTranslation, dataRangeTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLDataAllValuesFrom ce) {
    mainNode = createNode(ce, NodeLabels.DATA_ALL_VALUES_FROM);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.DATA_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var dataRangeEdge = createEdge(ce.getFiller(), EdgeLabels.DATA_RANGE);
    var dataRangeTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, dataRangeEdge),
        ImmutableList.of(propertyExprTranslation, dataRangeTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLDataHasValue ce) {
    mainNode = createNode(ce, NodeLabels.DATA_HAS_VALUE);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.DATA_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var literalEdge = createEdge(ce.getFiller(), EdgeLabels.LITERAL);
    var literalTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, literalEdge),
        ImmutableList.of(propertyExprTranslation, literalTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLDataMinCardinality ce) {
    mainNode = createCardinalityNode(ce, NodeLabels.DATA_MIN_CARDINALITY);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.DATA_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var dataRangeEdge = createEdge(ce.getFiller(), EdgeLabels.DATA_RANGE);
    var dataRangeTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, dataRangeEdge),
        ImmutableList.of(propertyExprTranslation, dataRangeTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLDataExactCardinality ce) {
    mainNode = createCardinalityNode(ce, NodeLabels.DATA_EXACT_CARDINALITY);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.DATA_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var dataRangeEdge = createEdge(ce.getFiller(), EdgeLabels.DATA_RANGE);
    var dataRangeTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, dataRangeEdge),
        ImmutableList.of(propertyExprTranslation, dataRangeTranslation));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLDataMaxCardinality ce) {
    mainNode = createCardinalityNode(ce, NodeLabels.DATA_MAX_CARDINALITY);
    var propertyExprEdge = createEdge(ce.getProperty(), EdgeLabels.DATA_PROPERTY_EXPRESSION);
    var propertyExprTranslation = createNestedTranslation(ce.getProperty());
    var dataRangeEdge = createEdge(ce.getFiller(), EdgeLabels.DATA_RANGE);
    var dataRangeTranslation = createNestedTranslation(ce.getFiller());
    return Translation.create(mainNode,
        ImmutableList.of(propertyExprEdge, dataRangeEdge),
        ImmutableList.of(propertyExprTranslation, dataRangeTranslation));
  }

  @Override
  protected Node getMainNode() {
    return mainNode;
  }

  @Override
  protected Translation getTranslation(@Nonnull OWLObject anyObject) {
    checkNotNull(anyObject);
    if (anyObject instanceof OWLClassExpression) {
      return getClassExpressionTranslation((OWLClassExpression) anyObject);
    } else if (anyObject instanceof OWLPropertyExpression) {
      return getPropertyExpressionTranslation((OWLPropertyExpression) anyObject);
    } else if (anyObject instanceof OWLIndividual) {
      return getIndividualTranslation((OWLIndividual) anyObject);
    } else if (anyObject instanceof OWLLiteral) {
      return getLiteralTranslation((OWLLiteral) anyObject);
    } else if (anyObject instanceof OWLDataRange) {
      return DataRangeTranslation((OWLDataRange) anyObject);
    }
    throw new IllegalArgumentException("Implementation error");
  }

  private Translation getClassExpressionTranslation(OWLClassExpression classExpression) {
    var classExpressionVisitor = visitorFactory.createClassExpressionVisitor();
    return classExpression.accept(classExpressionVisitor);
  }

  private Translation getPropertyExpressionTranslation(OWLPropertyExpression propertyExpression) {
    var propertyExpressionVisitor = visitorFactory.createPropertyExpressionVisitor();
    return propertyExpression.accept(propertyExpressionVisitor);
  }

  private Translation getIndividualTranslation(OWLIndividual individual) {
    var individualVisitor = visitorFactory.createIndividualVisitor();
    return individual.accept(individualVisitor);
  }

  private Translation getLiteralTranslation(OWLLiteral literal) {
    var dataVisitor = visitorFactory.createDataVisitor();
    return literal.accept(dataVisitor);
  }

  private Translation DataRangeTranslation(OWLDataRange dataRange) {
    var dataVisitor = visitorFactory.createDataVisitor();
    return dataRange.accept(dataVisitor);
  }
}
