package ru.geneticalgorithm.function.mutation;

import ru.geneticalgorithm.model.Gene;

import java.util.List;

/**
 * @author avnik
 */
@FunctionalInterface
public interface MutationFunction<T> {
  List<Gene<T>> applyMutation(Gene<T> gene, int geneIndex, List<Gene<T>> chromosome);
}
