package ru.geneticalgorithm.function;

import ru.geneticalgorithm.model.Gene;

import java.util.List;

/**
 * @author avnik
 */
@FunctionalInterface
public interface GeneGenerator<T> {
  Gene<T> generateGene(final List<Gene<T>> previousGenes);
}
