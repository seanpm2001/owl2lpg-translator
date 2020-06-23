package edu.stanford.owl2lpg.client.read.axiom;

import dagger.Component;
import edu.stanford.owl2lpg.client.DatabaseSessionScope;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Component(modules = AxiomSubjectAccessorModule.class)
@DatabaseSessionScope
public interface DataAccessorComponent {

  AxiomSubjectAccessor getAxiomSubjectAccessor();
}