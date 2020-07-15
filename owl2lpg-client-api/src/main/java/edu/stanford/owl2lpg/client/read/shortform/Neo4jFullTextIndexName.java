package edu.stanford.owl2lpg.client.read.shortform;

import com.google.auto.value.AutoValue;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@AutoValue
public abstract class Neo4jFullTextIndexName {

  public static Neo4jFullTextIndexName create(String name) {
    return new AutoValue_Neo4J_FullTextIndexName(name);
  }

  public abstract String getName();
}