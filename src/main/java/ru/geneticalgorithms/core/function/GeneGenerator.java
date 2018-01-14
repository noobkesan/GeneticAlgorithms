package ru.geneticalgorithms.core.function;

import ru.geneticalgorithms.core.model.Gene;

/**
 * @author avnik
 */
@FunctionalInterface
public interface GeneGenerator<T> {
  Gene<T> generateGene();
}
