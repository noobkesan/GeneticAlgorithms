package ru.geneticalgorithm.function.terminatecondition;

import ru.geneticalgorithm.model.Population;

/**
 * @author avnik
 */
@FunctionalInterface
public interface TerminateCondition<T> {
  boolean isMet(Population<T> population);
}
