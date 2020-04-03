package edu.stanford.owl2lpg.model;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

public class GraphFactory {

  public static Node Node(@Nonnull ImmutableList<String> labels, @Nonnull Properties properties) {
    return Node.create(labels, properties);
  }

  public static Node Node(@Nonnull ImmutableList<String> labels) {
    return Node.create(labels, Properties.empty());
  }

  public static Edge Edge(@Nonnull Node fromNode,
                          @Nonnull Node toNode,
                          @Nonnull String label,
                          @Nonnull Properties properties) {
    return Edge.create(fromNode, toNode, label, properties);
  }

  public static Edge Edge(@Nonnull Node fromNode,
                          @Nonnull Node toNode,
                          @Nonnull String label) {
    return Edge.create(fromNode, toNode, label, Properties.empty());
  }
}