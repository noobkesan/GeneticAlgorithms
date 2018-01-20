package ru.geneticalgorithms.core.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author avnik
 */
public class Population<T> {
  private static final Comparator<Individual> INDIVIDUALS_COMPARATOR = Comparator.comparing(
      (Individual i) -> i.getFitness()
  ).reversed();

  private final List<Individual<T>> individuals;
  private final double populationFitness;

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