package ru.geneticalgorithms.core.function.mutation;

import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.model.Gene;

/**
 * @author avnik
 */
public class RandomMutationFunction<T> implements MutationFunction<T> {
  private final GeneGenerator<T> geneGenerator;

  public RandomMutationFunction(GeneGenerator<T> geneGenerator) {
    this.geneGenerator = geneGenerator;
  }

  @Override
  public Gene<T> applyMutation(Gene<T> gene) {
    Gene<T> newGene = geneGenerator.generateGene();
    while (newGene.getValue().equals(gene.getValue())) {
      newGene = geneGenerator.generateGene();
    }

    return newGene;
  }
}
