package ru.geneticalgorithms.core.function;

import ru.geneticalgorithms.core.model.Gene;

import java.util.List;

/**
 * @author avnik
 */
@FunctionalInterface
public interface FitnessFunction<T> {
  double getFitness(List<Gene<T>> chromosome);
}
