package ru.geneticalgorithms.core.function.mutation;

import ru.geneticalgorithms.core.model.Gene;

/**
 * @author avnik
 */
@FunctionalInterface
public interface MutationFunction<T> {
  Gene<T> applyMutation(Gene<T> gene);
}
