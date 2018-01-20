package ru.geneticalgorithms.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.function.crossover.CrossoverFunction;
import ru.geneticalgorithms.core.function.mutation.MutationFunction;
import ru.geneticalgorithms.core.function.parentselect.ParentSelectFunction;
import ru.geneticalgorithms.core.function.terminatecondition.TerminateCondition;
import ru.geneticalgorithms.core.model.Gene;
import ru.geneticalgorithms.core.model.Individual;
import ru.geneticalgorithms.core.model.Population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author avnik
 */
public class GeneticAlgorithm<T> {
  private static final Logger logger = LoggerFactory.getLogger(GeneticAlgorithm.class);

  private final int chromosomeLength;
  private final int populationSize;
  private final double mutationRate;
  private final double crossoverRate;
  private final int elitismCount;
  private final int maxGenerationsCount;

  private final FitnessFunction<T> fitnessFunction;
  private final GeneGenerator<T> geneGenerator;
  private final TerminateCondition<T> terminateCondition;
  private final ParentSelectFunction<T> parentSelectFunction;
  private final CrossoverFunction<T> crossoverFunction;
  private final MutationFunction<T> mutationFunction;

  private GeneticAlgorithm(int chromosomeLength, int populationSize, double mutationRate, double crossoverRate,
                           int elitismCount, int maxGenerationsCount, FitnessFunction<T> fitnessFunction,
                           GeneGenerator<T> geneGenerator, TerminateCondition<T> terminateCondition,
                           ParentSelectFunction<T> parentSelectFunction, CrossoverFunction<T> crossoverFunction,
                           MutationFunction<T> mutationFunction) {
    this.populationSize = populationSize;
    this.chromosomeLength = chromosomeLength;
    this.mutationRate = mutationRate;
    this.crossoverRate = crossoverRate;
    this.elitismCount = elitismCount;
    this.maxGenerationsCount = maxGenerationsCount;

    this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
    this.geneGenerator = Objects.requireNonNull(geneGenerator);
    this.terminateCondition = Objects.requireNonNull(terminateCondition);
    this.parentSelectFunction = Objects.requireNonNull(parentSelectFunction);
    this.crossoverFunction = Objects.requireNonNull(crossoverFunction);
    this.mutationFunction = Objects.requireNonNull(mutationFunction);

    logger.info(
        "Created with following params: populationSize={}, chromosomeLength={}, mutationRate={}, " +
            "crossoverRate={}, elitismCount={}, maxGenerationsCount={}",
        populationSize, chromosomeLength, mutationRate, crossoverRate, elitismCount, maxGenerationsCount
    );
  }

  public Individual<T> run() {
    logger.info("Genetic algorithm started");
    Population<T> population = newRandomPopulation();

    int generation = 1;
    while (generation < maxGenerationsCount && !terminateCondition.isMet(population)) {
      logger.info("Generation-{} Best solution: {}", generation, population.getFittest(0));

      population = crossoverPopulation(population);
      population = mutatePopulation(population);
      generation++;
    }

    logger.info("Found solution in {} generations", generation);
    logger.info("Final solution: {}", population.getFittest(0));
    return population.getFittest(0);
  }

  private Population<T> crossoverPopulation(Population<T> population) {
    List<Individual<T>> individuals = population.getIndividuals();
    List<Individual<T>> newIndividuals = newEliteIndividuals(individuals);

    for (int i = elitismCount; i < individuals.size(); i++) {
      Individual<T> parent1 = population.getFittest(i);

      if (this.crossoverRate > Math.random()) {
        Individual<T> parent2 = parentSelectFunction.selectParent(population);
        Individual<T> offspring = crossoverFunction.applyCrossover(parent1, parent2);
        offspring.calcFitness(fitnessFunction);
        newIndividuals.add(offspring);

      } else {
        newIndividuals.add(parent1);
      }
    }

    return new Population<>(newIndividuals);
  }

  private Population<T> mutatePopulation(Population<T> population) {
    List<Individual<T>> individuals = population.getIndividuals();
    List<Individual<T>> newIndividuals = newEliteIndividuals(individuals);

    for (int i = elitismCount; i < individuals.size(); i++) {
      Individual<T> individual = population.getFittest(i);
      List<Gene<T>> newChromosome = individual.getChromosome();

      for (int j = 0; j < chromosomeLength; j++) {
        Gene<T> gene = newChromosome.get(j);
        if (mutationRate > Math.random()) {
          newChromosome = mutationFunction.applyMutation(gene, j, newChromosome);
        }
      }

      Individual<T> newIndividual = new Individual<>(newChromosome);
      newIndividual.calcFitness(fitnessFunction);
      newIndividuals.add(newIndividual);
    }

    return new Population<>(newIndividuals);
  }

  private List<Individual<T>> newEliteIndividuals(List<Individual<T>> individuals) {
    return new ArrayList<>(individuals.subList(0, elitismCount));
  }

  @SuppressWarnings("unchecked")
  private Individual<T> newRandomIndividual() {
    List<Gene<T>> chromosome = new ArrayList<>(chromosomeLength);
    for (int i = 0; i < chromosomeLength; i++) {
      Gene<T> newGene = geneGenerator.generateGene(Collections.unmodifiableList(chromosome));
      chromosome.add(newGene);
    }

    Individual<T> newIndividual = new Individual<>(chromosome);
    newIndividual.calcFitness(fitnessFunction);
    return newIndividual;
  }

  private Population<T> newRandomPopulation() {
    List<Individual<T>> individuals = Stream.generate(this::newRandomIndividual)
        .limit(populationSize)
        .collect(Collectors.toList());

    return new Population<>(individuals);
  }

  public static class Builder {
    private int populationSize = 50;
    private int chromosomeLength = 50;
    private double mutationRate = 0.1;
    private double crossoverRate = 0.95;
    private int elitismCount = 5;
    private int maxGenerationsCount = 1000;

    private FitnessFunction fitnessFunction;
    private GeneGenerator geneGenerator;
    private TerminateCondition terminateCondition;
    private ParentSelectFunction parentSelectFunction;
    private CrossoverFunction crossoverFunction;
    private MutationFunction mutationFunction;

    @SuppressWarnings("unchecked")
    public <T> GeneticAlgorithm<T> build() {
      return new GeneticAlgorithm<>(
          chromosomeLength,
          populationSize,
          mutationRate,
          crossoverRate,
          elitismCount,
          maxGenerationsCount,
          fitnessFunction,
          geneGenerator,
          terminateCondition,
          parentSelectFunction,
          crossoverFunction,
          mutationFunction
      );
    }

    public Builder setPopulationSize(int populationSize) {
      this.populationSize = populationSize;
      return this;
    }

    public Builder setChromosomeLength(int chromosomeLength) {
      this.chromosomeLength = chromosomeLength;
      return this;
    }

    public Builder setMutationRate(double mutationRate) {
      this.mutationRate = mutationRate;
      return this;
    }

    public Builder setCrossoverRate(double crossoverRate) {
      this.crossoverRate = crossoverRate;
      return this;
    }

    public Builder setElitismCount(int elitismCount) {
      this.elitismCount = elitismCount;
      return this;
    }

    public Builder setMaxGenerationsCount(int maxGenerationsCount) {
      this.maxGenerationsCount = maxGenerationsCount;
      return this;
    }

    public <T> Builder setFitnessFunction(FitnessFunction<T> fitnessFunction) {
      this.fitnessFunction = fitnessFunction;
      return this;
    }

    public <T> Builder setGeneGenerator(GeneGenerator<T> geneGenerator) {
      this.geneGenerator = geneGenerator;
      return this;
    }

    public <T> Builder setTerminateCondition(TerminateCondition<T> terminateCondition) {
      this.terminateCondition = terminateCondition;
      return this;
    }

    public <T> Builder setParentSelectFunction(ParentSelectFunction<T> parentSelectFunction) {
      this.parentSelectFunction = parentSelectFunction;
      return this;
    }

    public <T> Builder setCrossoverFunction(CrossoverFunction<T> crossoverFunction) {
      this.crossoverFunction = crossoverFunction;
      return this;
    }

    public <T> Builder setMutationFunction(MutationFunction<T> mutationFunction) {
      this.mutationFunction = mutationFunction;
      return this;
    }
  }
}