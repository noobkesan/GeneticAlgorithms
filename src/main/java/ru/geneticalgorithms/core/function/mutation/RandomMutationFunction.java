package ru.geneticalgorithms.core.function.mutation;

import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.model.Gene;

import java.util.Collections;
import java.util.Objects;

/**
 * @author avnik
 */
public class RandomMutationFunction<T> implements MutationFunction<T> {
  private final GeneGenerator<T> geneGenerator;

  public RandomMutationFunction(GeneGenerator<T> geneGenerator) {
    this.geneGenerator = Objects.requireNonNull(geneGenerator);
  }

  @Override
  public Gene<T> applyMutation(Gene<T> gene) {
    Gene<T> newGene = nextGene();
    while (newGene.getValue().equals(gene.getValue())) {
      newGene = nextGene();
    }

    return newGene;
  }

  private Gene<T> nextGene() {
    return geneGenerator.generateGene(Collections.emptyList());
  }
}
