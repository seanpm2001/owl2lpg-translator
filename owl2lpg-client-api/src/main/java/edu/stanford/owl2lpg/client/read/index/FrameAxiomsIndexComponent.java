package edu.stanford.owl2lpg.client.read.index;

import dagger.Component;
import edu.stanford.owl2lpg.client.DatabaseSessionScope;
import edu.stanford.owl2lpg.client.read.axiom.AxiomContextModule;
import edu.stanford.owl2lpg.client.read.axiom.AxiomSubjectAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.handlers.OwlDataFactoryModule;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Component(modules = {
    AxiomContextModule.class,
    AxiomSubjectAccessorModule.class,
    OwlDataFactoryModule.class
})
@DatabaseSessionScope
public interface FrameAxiomsIndexComponent {

  Neo4jClassFrameAxiomsIndex getClassFrameAxiomsIndex();

  Neo4jNamedIndividualFrameAxiomsIndex getNamedIndividualFrameAxiomsIndex();
}