package ru.geneticalgorithms.core.model;

import ru.geneticalgorithms.core.exception.GeneticAlgorithmException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author avnik
 */
public class Population<T> {
  private final List<Individual<T>> individuals;
  private final double populationFitness;

  public Population(List<Individual<T>> individuals, Comparator<Individual> individualComparator) throws GeneticAlgorithmException {
    validateIndividuals(individuals);
    Objects.requireNonNull(individualComparator);

    individuals.sort(individualComparator);

    this.individuals = Collections.unmodifiableList(individuals);
    this.populationFitness = calcPopulationFitness();
  }

  public List<Individual<T>> getIndividuals() {
    return individuals;
  }

  public Individual<T> getFittest() {
    return individuals.get(0);
  }

  public double getPopulationFitness() {
    return populationFitness;
  }

  private double calcPopulationFitness() {
    return individuals.stream().mapToDouble(Individual::getFitness).sum();
  }

  private void validateIndividuals(List<Individual<T>> individuals) throws GeneticAlgorithmException {
    if(individuals == null || individuals.isEmpty()) {
      throw new GeneticAlgorithmException("Population individuals mustn't be null or empty");
    }
  }

  @Override
  public String toString() {
    return "Population{" +
        "individuals=" + individuals +
        '}';
  }
}