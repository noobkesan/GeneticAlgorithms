package ru.geneticalgorithm.function.crossover;

import ru.geneticalgorithm.model.Individual;

/**
 * @author avnik
 */
@FunctionalInterface
public interface CrossoverFunction<T> {
  Individual<T> applyCrossover(Individual<T> parent1, Individual<T> parent2);
}
