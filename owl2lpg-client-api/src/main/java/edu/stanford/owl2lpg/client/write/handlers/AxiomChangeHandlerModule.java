package edu.stanford.owl2lpg.client.write.handlers;

import dagger.Binds;
import dagger.Module;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Module
public abstract class AxiomChangeHandlerModule {

  @Binds
  public abstract AxiomChangeHandler
  provideAnnotationAssertionAxiomChangeHandler(AnnotationAssertionAxiomChangeHandler impl);
}
