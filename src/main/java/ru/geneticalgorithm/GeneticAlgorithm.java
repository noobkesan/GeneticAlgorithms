package ru.geneticalgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geneticalgorithm.function.FitnessFunction;
import ru.geneticalgorithm.function.GeneGenerator;
import ru.geneticalgorithm.function.crossover.CrossoverFunction;
import ru.geneticalgorithm.function.mutation.MutationFunction;
import ru.geneticalgorithm.function.parentselect.ParentSelectFunction;
import ru.geneticalgorithm.function.terminatecondition.TerminateCondition;
import ru.geneticalgorithm.model.Gene;
import ru.geneticalgorithm.model.Individual;
import ru.geneticalgorithm.model.Population;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author avnik
 */
public class GeneticAlgorithm<T> {
  private static final Logger logger = LoggerFactory.getLogger(GeneticAlgorithm.class);
  private static final int SEQUENCE_PROCESSING_SIZE = 10;

  @SuppressWarnings("all")
  private static final Comparator<Individual> DEFAULT_INDIVIDUALS_COMPARATOR = Comparator.comparing(
      (Individual i) -> i.getFitness()
  ).reversed();

  private final int chromosomeLength;
  private final int populationSize;
  private final double mutationRate;
  private final double crossoverRate;
  private final int elitismCount;
  private final int maxGenerationsCount;
  private final boolean parallel;

  private final FitnessFunction<T> fitnessFunction;
  private final Comparator<Individual> individualComparator;
  private final GeneGenerator<T> geneGenerator;
  private final TerminateCondition<T> terminateCondition;
  private final ParentSelectFunction<T> parentSelectFunction;
  private final CrossoverFunction<T> crossoverFunction;
  private final MutationFunction<T> mutationFunction;

  @SuppressWarnings("unchecked")
  private GeneticAlgorithm(Builder builder) {
    this.populationSize = builder.populationSize;
    this.chromosomeLength = builder.chromosomeLength;
    this.mutationRate = builder.mutationRate;
    this.crossoverRate = builder.crossoverRate;
    this.elitismCount = builder.elitismCount;
    this.maxGenerationsCount = builder.maxGenerationsCount;
    this.parallel = builder.parallel;

    this.fitnessFunction = Objects.requireNonNull(builder.fitnessFunction);
    this.individualComparator = Objects.requireNonNull(builder.individualComparator);
    this.geneGenerator = Objects.requireNonNull(builder.geneGenerator);
    this.terminateCondition = Objects.requireNonNull(builder.terminateCondition);
    this.parentSelectFunction = Objects.requireNonNull(builder.parentSelectFunction);
    this.crossoverFunction = Objects.requireNonNull(builder.crossoverFunction);
    this.mutationFunction = Objects.requireNonNull(builder.mutationFunction);

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
      logger.info("Generation-{} Best solution: {}", generation, population.getFittest());

      population = parallel ? parallelCrossover(population) : crossover(population);
      population = parallel ? parallelMutation(population) : mutation(population);
      generation++;
    }

    logger.info("Found solution in {} generations", generation);
    logger.info("Final solution: {}", population.getFittest());
    return population.getFittest();
  }

  private Population<T> crossover(Population<T> population) {
    List<Individual<T>> newIndividuals = crossover(
        population.getIndividuals(),
        population.getPopulationFitness(),
        0,
        populationSize
    );

    return newPopulation(newIndividuals);
  }

  private Population<T> parallelCrossover(Population<T> population) {
    Crossover crossover = new Crossover(population.getIndividuals(), population.getPopulationFitness(), 0, populationSize);

    List<Individual<T>> newIndividuals = ForkJoinPool.commonPool().invoke(crossover);
    return newPopulation(newIndividuals);
  }

  private List<Individual<T>> crossover(final List<Individual<T>> individuals, double populationFitness,
                                        int startIndex, int endIndex) {
    List<Individual<T>> newIndividuals = new ArrayList<>();

    for (int i = startIndex; i < endIndex; i++) {
      Individual<T> parent1 = individuals.get(i);

      if (i >= elitismCount && crossoverRate > Math.random()) {
        Individual<T> parent2 = parentSelectFunction.selectParent(individuals, populationFitness);
        Individual<T> offspring = crossoverFunction.applyCrossover(parent1, parent2);
        offspring.calcFitness(fitnessFunction);
        newIndividuals.add(offspring);

      } else {
        newIndividuals.add(parent1);
      }
    }

    return newIndividuals;
  }

  private Population<T> mutation(Population<T> population) {
    List<Individual<T>> newIndividuals = mutation(population.getIndividuals(), 0, populationSize);
    return newPopulation(newIndividuals);
  }

  private Population<T> parallelMutation(Population<T> population) {
    Mutation mutation = new Mutation(population.getIndividuals(), 0, populationSize);

    List<Individual<T>> newIndividuals = ForkJoinPool.commonPool().invoke(mutation);
    return newPopulation(newIndividuals);
  }

  private List<Individual<T>> mutation(final List<Individual<T>> individuals, int startIndex, int endIndex) {
    List<Individual<T>> newIndividuals = new ArrayList<>();

    for (int i = startIndex; i < endIndex; i++) {
      Individual<T> individual = individuals.get(i);
      Individual<T> newIndividual = i < elitismCount ? individual : mutateIndividual(individual);
      newIndividuals.add(newIndividual);
    }

    return newIndividuals;
  }

  private Individual<T> mutateIndividual(Individual<T> individual) {
    List<Gene<T>> newChromosome = individual.getChromosome();

    for (int j = 0; j < chromosomeLength; j++) {
      Gene<T> gene = newChromosome.get(j);
      if (mutationRate > Math.random()) {
        newChromosome = mutationFunction.applyMutation(gene, j, newChromosome);
      }
    }

    Individual<T> newIndividual = new Individual<>(newChromosome);
    newIndividual.calcFitness(fitnessFunction);

    return newIndividual;
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

    return newPopulation(individuals);
  }

  private Population<T> newPopulation(List<Individual<T>> individuals) {
    return new Population<>(individuals, individualComparator);
  }

  private class Crossover extends RecursiveTask<List<Individual<T>>> {
    private final List<Individual<T>> individuals;
    private final double populationFitness;
    private final int startIndex;
    private final int endIndex;

    Crossover(final List<Individual<T>> individuals, double populationFitness, int startIndex, int endIndex) {
      this.individuals = individuals;
      this.populationFitness = populationFitness;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

    @Override
    protected List<Individual<T>> compute() {
      if(endIndex - startIndex <= SEQUENCE_PROCESSING_SIZE) {
        return crossover(individuals, populationFitness, startIndex, endIndex);
      }

      int mediumIndex = (startIndex + endIndex) / 2;

      Crossover crossover1 = new Crossover(individuals, populationFitness, startIndex, mediumIndex);
      crossover1.fork();

      Crossover crossover2 = new Crossover(individuals, populationFitness, mediumIndex, endIndex);

      List<Individual<T>> result = crossover2.compute();
      result.addAll(crossover1.join());

      return result;
    }
  }

  private class Mutation extends RecursiveTask<List<Individual<T>>> {
    private final List<Individual<T>> individuals;
    private final int startIndex;
    private final int endIndex;

    Mutation(final List<Individual<T>> individuals, int startIndex, int endIndex) {
      this.individuals = individuals;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

    @Override
    protected List<Individual<T>> compute() {
      if(endIndex - startIndex <= SEQUENCE_PROCESSING_SIZE) {
        return mutation(individuals, startIndex, endIndex);
      }

      int mediumIndex = (startIndex + endIndex) / 2;

      Mutation mutation1 = new Mutation(individuals, startIndex, mediumIndex);
      mutation1.fork();

      Mutation mutation2 = new Mutation(individuals, mediumIndex, endIndex);

      List<Individual<T>> result = mutation2.compute();
      result.addAll(mutation1.join());

      return result;
    }
  }

  public static class Builder {
    private int populationSize = 50;
    private int chromosomeLength = 50;
    private double mutationRate = 0.1;
    private double crossoverRate = 0.95;
    private int elitismCount = 5;
    private int maxGenerationsCount = 1000;
    private boolean parallel = false;

    private FitnessFunction fitnessFunction;
    private Comparator<Individual> individualComparator = DEFAULT_INDIVIDUALS_COMPARATOR;
    private GeneGenerator geneGenerator;
    private TerminateCondition terminateCondition;
    private ParentSelectFunction parentSelectFunction;
    private CrossoverFunction crossoverFunction;
    private MutationFunction mutationFunction;

    public <T> GeneticAlgorithm<T> build() {
      return new GeneticAlgorithm<>(this);
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

    public Builder parallel() {
      this.parallel = true;
      return this;
    }

    public <T> Builder setFitnessFunction(FitnessFunction<T> fitnessFunction) {
      this.fitnessFunction = fitnessFunction;
      return this;
    }

    public Builder setIndividualComparator(Comparator<Individual> individualComparator) {
      this.individualComparator = individualComparator;
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