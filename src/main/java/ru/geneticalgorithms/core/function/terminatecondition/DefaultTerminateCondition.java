package ru.geneticalgorithms.core.function.terminatecondition;

import ru.geneticalgorithms.core.model.Population;

/**
 * @author avnik
 */
public class DefaultTerminateCondition<T> implements TerminateCondition<T> {
  @Override
  public boolean isMet(Population<T> population) {
    return population.getIndividuals().stream()
        .anyMatch(individual -> individual.getFitness() == 1.0);
  }
}
