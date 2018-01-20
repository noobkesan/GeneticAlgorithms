package ru.geneticalgorithms.core.function.mutation;

import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.model.Gene;

import java.util.ArrayList;
import java.util.List;
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
  public List<Gene<T>> applyMutation(Gene<T> gene, int geneIndex, List<Gene<T>> chromosome) {
    Gene<T> newGene = nextGene(chromosome);
    while (newGene.getValue().equals(gene.getValue())) {
      newGene = nextGene(chromosome);
    }

    List<Gene<T>> newChromosome = new ArrayList<>(chromosome);
    newChromosome.set(geneIndex, newGene);

    return newChromosome;
  }

  private Gene<T> nextGene(List<Gene<T>> chromosome) {
    return geneGenerator.generateGene(chromosome);
  }
}
