package ru.geneticalgorithms.core.model;

import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author avnik
 */
public class Individual<T> {
  private final List<Gene<T>> chromosome;
  private final FitnessFunction<T> fitnessFunction;

  public Individual(List<Gene<T>> chromosome, FitnessFunction<T> fitnessFunction) {
    Objects.requireNonNull(chromosome);

    this.chromosome = Collections.unmodifiableList(chromosome);
    this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
  }

  @SuppressWarnings("unchecked")
  public static <T> Individual<T> withRandomGenes(int chromosomeLength, FitnessFunction<T> fitnessFunction,
                                                  GeneGenerator<T> geneGenerator) {
    List<Gene<T>> chromosome = new ArrayList<>(chromosomeLength);
    for (int i = 0; i < chromosomeLength; i++) {
      Gene<T> newGene = geneGenerator.generateGene(Collections.unmodifiableList(chromosome));
      chromosome.add(newGene);
    }

    return new Individual<>(chromosome, fitnessFunction);
  }

  public List<Gene<T>> getChromosome() {
    return chromosome;
  }

  public double getFitness() {
    return fitnessFunction.getFitness(chromosome);
  }

  public String toString() {
    return chromosome.stream()
        .map(Gene::getValue)
        .map(Object::toString)
        .collect(Collectors.joining(" "));
  }
}
