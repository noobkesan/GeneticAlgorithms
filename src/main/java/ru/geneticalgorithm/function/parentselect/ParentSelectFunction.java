package ru.geneticalgorithm.function.parentselect;

import ru.geneticalgorithm.model.Individual;

import java.util.List;

/**
 * @author avnik
 */
@FunctionalInterface
public interface ParentSelectFunction<T> {
  Individual<T> selectParent(List<Individual<T>> individuals, double populationFitness);
}
