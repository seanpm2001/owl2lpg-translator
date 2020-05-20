// Get Class Frame
CALL {
   MATCH (project)-[:BRANCH]->(branch)-[:ONTOLOGY_DOCUMENT]->(document)-[:AXIOM]->(axiom:ClassAxiom)
   MATCH (entity:Class { iri: $subjectIri })-[:IS_SUBJECT_OF]->(axiom)
   WHERE project.projectId = $projectId
   AND branch.branchId = $branchId
   AND document.ontologyDocumentId = $ontoDocId
   RETURN entity
   UNION
   MATCH (project)-[:BRANCH]->(branch)-[:ONTOLOGY_DOCUMENT]->(document)-[:AXIOM]->(annotation:AnnotationAssertion)
   MATCH (entity)-[:ENTITY_IRI]->(:IRI { iri: $subjectIri })-[:IS_SUBJECT_OF]->(annotation)
   WHERE project.projectId = $projectId
   AND branch.branchId = $branchId
   AND document.ontologyDocumentId = $ontoDocId
   RETURN entity
}
OPTIONAL MATCH (entity)-[:SUB_CLASS_OF]->(parent:Class)
OPTIONAL MATCH (entity)-[property:RELATED_TO]->(object)

RETURN { type: "ClassFrame",
       subject: { type: "owl:Class", iri: entity.iri },
       parents: COLLECT(DISTINCT( CASE WHEN parent IS NOT NULL THEN { type: "owl:Class", iri: parent.iri } END )),
       propertyValues:
       COLLECT(DISTINCT( CASE WHEN object IS NOT NULL THEN
          CASE WHEN 'Class' IN LABELS(object) AND property.type = 'ObjectProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:ObjectProperty", iri: property.iri },
               value: { type: "owl:Class", iri: object.iri }
             }
          WHEN 'NamedIndividual' IN LABELS(object) AND property.type = 'ObjectProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:ObjectProperty", iri: property.iri },
               value: { type: "owl:NamedIndividual", iri: object.iri }
             }
          WHEN 'Datatype' IN LABELS(object) AND property.type = 'DataProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:DataProperty", iri: property.iri },
               value: { type: "owl:Datatype", iri: object.iri }
             }
          WHEN 'Literal' IN LABELS(object) AND property.type = 'DataProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:DataProperty", iri: property.iri },
               value:
               CASE WHEN object.datatype = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral' THEN
                  { value: object.lexicalForm, lang: object.language }
               ELSE
                  { type: object.datatype, value: object.lexicalForm }
               END
             }
          WHEN 'Literal' IN LABELS(object) AND property.type = 'AnnotationProperty' THEN
             { type: "PropertyAnnotationValue",
               property: { type: "owl:AnnotationProperty", iri: property.iri },
               value:
               CASE WHEN object.datatype = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral' THEN
                  { value: object.lexicalForm, lang: object.language }
               ELSE
                  { type: object.datatype, value: object.lexicalForm }
               END
             }
          WHEN 'IRI' IN LABELS(object) AND property.type = 'AnnotationProperty' THEN
             { type: "PropertyAnnotationValue",
               property: { type: "owl:AnnotationProperty", iri: property.iri },
               value: object.iri
             }
          END
       END ))} AS result