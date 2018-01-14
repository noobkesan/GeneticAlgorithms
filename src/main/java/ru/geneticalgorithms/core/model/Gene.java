package ru.geneticalgorithms.core.model;

import java.util.Objects;

/**
 * @author avnik
 */
public class Gene<T> {
  private final T value;

  public Gene(T value) {
    this.value = Objects.requireNonNull(value);
  }

  public T getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Gene{" +
        "value=" + value +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Gene<?> gene = (Gene<?>) o;
    return Objects.equals(value, gene.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
