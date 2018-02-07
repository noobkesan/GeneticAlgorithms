package ru.geneticalgorithm.function;

import ru.geneticalgorithm.model.Gene;

import java.util.List;

/**
 * @author avnik
 */
@FunctionalInterface
public interface FitnessFunction<T> {
  double getFitness(List<Gene<T>> chromosome);
}
