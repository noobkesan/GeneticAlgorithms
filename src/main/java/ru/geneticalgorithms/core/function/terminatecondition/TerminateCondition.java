package ru.geneticalgorithms.core.function.terminatecondition;

import ru.geneticalgorithms.core.model.Population;

/**
 * @author avnik
 */
@FunctionalInterface
public interface TerminateCondition<T> {
  boolean isMet(Population<T> population);
}
