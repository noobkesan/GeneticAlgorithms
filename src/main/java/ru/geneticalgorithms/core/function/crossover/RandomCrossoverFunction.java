package ru.geneticalgorithms.core.function.crossover;

import ru.geneticalgorithms.core.model.Gene;
import ru.geneticalgorithms.core.model.Individual;

import java.util.ArrayList;
import java.util.List;

/**
 * @author avnik
 */
public class RandomCrossoverFunction<T> implements CrossoverFunction<T> {

  @Override
  public Individual<T> applyCrossover(Individual<T> parent1, Individual<T> parent2) {
    List<Gene<T>> parent1Chromosome = parent1.getChromosome();
    List<Gene<T>> parent2Chromosome = parent2.getChromosome();
    List<Gene<T>> newGenes = new ArrayList<>(parent1Chromosome.size());

    for (int geneIndex = 0; geneIndex < parent1Chromosome.size(); geneIndex++) {
      Gene<T> gene = Math.random() < 0.5 ? parent1Chromosome.get(geneIndex) : parent2Chromosome.get(geneIndex);
      newGenes.add(gene);
    }

    return new Individual<>(newGenes);
  }
}
