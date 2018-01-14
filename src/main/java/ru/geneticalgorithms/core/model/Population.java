package ru.geneticalgorithms.core.model;

import ru.geneticalgorithms.core.exception.GeneticAlgorithmException;
import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author avnik
 */
public class Population<T> {
  private static final Comparator<Individual> INDIVIDUALS_COMPARATOR = Comparator.comparing(
      (Individual i) -> i.getFitness()
  ).reversed();

  private final List<Individual<T>> individuals;
  private final double populationFitness;

  public Population(int populationSize, int chromosomeLength, FitnessFunction<T> fitnessFunction,
                    GeneGenerator<T> geneGenerator) {
    if(populationSize <= 0) {
      throw new GeneticAlgorithmException("Population size must be > 0!");
    }

    if(chromosomeLength <= 0) {
      throw new GeneticAlgorithmException("Chromosome length must be > 0!");
    }

    Objects.requireNonNull(fitnessFunction);
    Objects.requireNonNull(geneGenerator);

    List<Individual<T>> individuals = Stream.generate(
        () -> Individual.withRandomGenes(chromosomeLength, fitnessFunction, geneGenerator)
    )
        .limit(populationSize)
        .sorted(INDIVIDUALS_COMPARATOR)
        .collect(Collectors.toList());

    this.individuals = Collections.unmodifiableList(individuals);
    this.populationFitness = computePopulationFitness();
  }

  public Population(List<Individual<T>> individuals) {
    Objects.requireNonNull(individuals);
    individuals.sort(INDIVIDUALS_COMPARATOR);

    this.individuals = Collections.unmodifiableList(individuals);
    this.populationFitness = computePopulationFitness();
  }

  public List<Individual<T>> getIndividuals() {
    return individuals;
  }

  public Individual<T> getFittest(int offset) {
    return individuals.get(offset);
  }

  public double getPopulationFitness() {
    return populationFitness;
  }

  private double computePopulationFitness() {
    return individuals.stream().mapToDouble(Individual::getFitness).sum();
  }

  @Override
  public String toString() {
    return "Population{" +
        "individuals=" + individuals +
        '}';
  }
}