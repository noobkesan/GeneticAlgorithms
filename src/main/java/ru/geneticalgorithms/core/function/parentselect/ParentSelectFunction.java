package ru.geneticalgorithms.core.function.parentselect;

import ru.geneticalgorithms.core.model.Individual;
import ru.geneticalgorithms.core.model.Population;

/**
 * @author avnik
 */
@FunctionalInterface
public interface ParentSelectFunction<T> {
  Individual<T> selectParent(Population<T> population);
}
