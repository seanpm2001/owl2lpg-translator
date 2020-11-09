package edu.stanford.owl2lpg.exporter.csv.writer.apoc;

import edu.stanford.owl2lpg.exporter.csv.writer.CsvSchema;
import edu.stanford.owl2lpg.model.Node;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.owl2lpg.model.Node.N4J_JSON_ID;
import static edu.stanford.owl2lpg.model.Node.N4J_JSON_LABELS;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.DATA_EXACT_CARDINALITY;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.DATA_MAX_CARDINALITY;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.DATA_MIN_CARDINALITY;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.OBJECT_EXACT_CARDINALITY;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.OBJECT_MAX_CARDINALITY;
import static edu.stanford.owl2lpg.translator.vocab.NodeLabels.OBJECT_MIN_CARDINALITY;
import static edu.stanford.owl2lpg.translator.vocab.PropertyFields.CARDINALITY;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class CardinalityNodeSchema implements CsvSchema<Node> {

  @Inject
  public CardinalityNodeSchema() {
  }

  @Nonnull
  @Override
  public com.fasterxml.jackson.dataformat.csv.CsvSchema getCsvSchema() {
    return getBuilder().build();
  }

  @Nonnull
  @Override
  public com.fasterxml.jackson.dataformat.csv.CsvSchema getCsvSchemaWithHeader() {
    return getBuilder().setUseHeader(true).build();
  }

  @Override
  public boolean isCompatible(Node node) {
    return node.isTypeOf(OBJECT_MAX_CARDINALITY) ||
        node.isTypeOf(OBJECT_MIN_CARDINALITY) ||
        node.isTypeOf(OBJECT_EXACT_CARDINALITY) ||
        node.isTypeOf(DATA_MAX_CARDINALITY) ||
        node.isTypeOf(DATA_MIN_CARDINALITY) ||
        node.isTypeOf(DATA_EXACT_CARDINALITY);
  }

  private static com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder getBuilder() {
    return com.fasterxml.jackson.dataformat.csv.CsvSchema.builder()
        .addColumn(N4J_JSON_ID)
        .addColumn(N4J_JSON_LABELS)
        .addColumn(CARDINALITY + ":int");
  }
}