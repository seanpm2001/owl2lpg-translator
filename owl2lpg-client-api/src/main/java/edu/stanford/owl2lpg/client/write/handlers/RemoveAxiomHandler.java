package edu.stanford.owl2lpg.client.write.handlers;

import edu.stanford.owl2lpg.client.write.GraphWriter;
import edu.stanford.owl2lpg.translator.AxiomTranslator;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class RemoveAxiomHandler {

  @Nonnull
  private final GraphWriter graphWriter;

  @Nonnull
  private final AxiomTranslator axiomTranslator;

  @Nonnull
  private final TranslationTranslator translationTranslator;

  @Inject
  public RemoveAxiomHandler(@Nonnull GraphWriter graphWriter,
                            @Nonnull AxiomTranslator axiomTranslator,
                            @Nonnull TranslationTranslator translationTranslator) {
    this.graphWriter = checkNotNull(graphWriter);
    this.axiomTranslator = checkNotNull(axiomTranslator);
    this.translationTranslator = checkNotNull(translationTranslator);
  }

  public void handle(@Nonnull OWLAxiom axiom) {
    var translation = axiomTranslator.translate(axiom);
    var deleteQuery = translationTranslator.translateToCypherDeleteQuery(translation);
    deleteQuery.forEach(graphWriter::execute);
  }
}
