package ru.geneticalgorithms.core.function.parentselect;

import ru.geneticalgorithms.core.model.Individual;

import java.util.List;

/**
 * @author avnik
 */
@FunctionalInterface
public interface ParentSelectFunction<T> {
  Individual<T> selectParent(List<Individual<T>> individuals, double populationFitness);
}
