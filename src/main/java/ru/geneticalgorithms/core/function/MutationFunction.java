package ru.geneticalgorithms.core.function;

import ru.geneticalgorithms.core.model.Gene;

/**
 * @author avnik
 */
@FunctionalInterface
public interface MutationFunction<T> {
  Gene<T> applyMutation(Gene<T> gene);
}
