MATCH (:Project {projectId:$projectId})-[:BRANCH]->(:Branch {branchId:$branchId})-[:ONTOLOGY_DOCUMENT]->(o:OntologyDocument {ontologyDocumentId:$ontoDocId})
MATCH (o)<-[:ENTITY_SIGNATURE_OF]-(child:DataProperty)
MATCH (o)<-[:ENTITY_SIGNATURE_OF]-(parent:DataProperty {iri:$entityIri})
MATCH p=(parent)<-[:SUB_DATA_PROPERTY_OF*]-(child)
WHERE apoc.coll.duplicates(NODES(p)) = []
RETURN p