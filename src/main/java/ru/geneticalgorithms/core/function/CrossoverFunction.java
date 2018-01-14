package ru.geneticalgorithms.core.function;

import ru.geneticalgorithms.core.model.Individual;

/**
 * @author avnik
 */
@FunctionalInterface
public interface CrossoverFunction<T> {
  Individual<T> applyCrossover(Individual<T> parent1, Individual<T> parent2);
}
