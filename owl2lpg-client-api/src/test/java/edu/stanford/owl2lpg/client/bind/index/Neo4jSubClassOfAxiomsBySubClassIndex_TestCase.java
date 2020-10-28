package edu.stanford.owl2lpg.client.bind.index;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
class Neo4jSubClassOfAxiomsBySubClassIndex_TestCase {


    private Neo4jSubClassOfAxiomsBySubClassIndex axiomIndex;

    private OWLClass clsA;

    private OWLSubClassOfAxiom axiom1;

    private OWLSubClassOfAxiom axiom2;

    private AxiomIndexTestHarness testHarness;

    private OWLOntologyID ontologyIdA;


    @BeforeEach
    void setUp() {
        testHarness = AxiomIndexTestHarness.createAndSetUp();
        ontologyIdA = testHarness.getOntologyIdA();
        axiomIndex = new Neo4jSubClassOfAxiomsBySubClassIndex(testHarness.getProjectId(),
                                                              testHarness.getBranchId(),
                                                              testHarness.getDocumentMap(),
                                                              testHarness.getAxiomAccessor());

        clsA = Class(IRI.create("http://example.org/A"));
        var clsB = Class(IRI.create("http://example.org/B"));
        var clsC = Class(IRI.create("http://example.org/C"));
        axiom1 = SubClassOf(clsA, clsB);
        axiom2 = SubClassOf(clsA, clsC);
    }

    @Test
    void shouldNotGetAnyAxioms() {
        var subClassOfAxioms = getAxioms(ontologyIdA);
        assertThat(subClassOfAxioms.isEmpty(), is(true));
    }

    @Test
    void shouldGetAllSubClassOfAxiomsFromOntDoc() {
        testHarness.addAxiomToOntologyDocument_A(axiom1);
        testHarness.addAxiomToOntologyDocument_A(axiom2);

        var subClassOfAxioms = getAxioms(ontologyIdA);

        assertThat(subClassOfAxioms, containsInAnyOrder(axiom1, axiom2));
    }

    @Test
    void shouldOnlyGetSubClassOfAxiomsFromSpecificOntDoc() {
        testHarness.addAxiomToOntologyDocument_A(axiom1);
        testHarness.addAxiomToOntologyDocument_B(axiom2);

        var subClassOfAxioms = getAxioms(ontologyIdA);

        assertThat(subClassOfAxioms, containsInAnyOrder(axiom1));
    }

    private ImmutableList<OWLSubClassOfAxiom> getAxioms(OWLOntologyID ontologyID) {
        return axiomIndex.getSubClassOfAxiomsForSubClass(clsA, ontologyID).collect(ImmutableList.toImmutableList());
    }

    @AfterEach
    void tearDown() {
        testHarness.tearDown();
    }
}