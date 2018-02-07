package ru.geneticalgorithm.model;

import ru.geneticalgorithm.function.FitnessFunction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author avnik
 */
public class Individual<T> {
  private final List<Gene<T>> chromosome;
  private double fitness = Double.NaN;

  public Individual(List<Gene<T>> chromosome) {
    Objects.requireNonNull(chromosome);
    this.chromosome = Collections.unmodifiableList(chromosome);
  }

  public List<Gene<T>> getChromosome() {
    return chromosome;
  }

  public double getFitness() {
    return fitness;
  }

  public void calcFitness(FitnessFunction<T> fitnessFunction) {
    Objects.requireNonNull(fitnessFunction);
    fitness = fitnessFunction.getFitness(chromosome);
  }

  public String toString() {
    return chromosome.stream()
        .map(Gene::getValue)
        .map(Object::toString)
        .collect(Collectors.joining(" "));
  }
}
