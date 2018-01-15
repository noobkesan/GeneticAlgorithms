package ru.geneticalgorithms.allones;

import ru.geneticalgorithms.core.GeneticAlgorithm;
import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.function.MutationFunction;
import ru.geneticalgorithms.core.function.crossover.RandomCrossoverFunction;
import ru.geneticalgorithms.core.function.parentselect.RouletteWheelParentSelectFunction;
import ru.geneticalgorithms.core.model.Gene;
import ru.geneticalgorithms.core.model.Population;

import java.util.function.Predicate;

/**
 * @author avnik
 */
public class AllOnesSolver {
  private GeneticAlgorithm<Integer> geneticAlgorithm;

  public AllOnesSolver() {
    this.geneticAlgorithm = new GeneticAlgorithm.Builder()
        .setChromosomeLength(50)
        .setPopulationSize(50)
        .setMutationRate(0.1)
        .setCrossoverRate(0.95)
        .setElitismCount(1)
        .setMaxGenerationsCount(1000)
        .setFitnessFunction(fitnessFunction)
        .setGeneGenerator(geneGenerator)
        .setTerminatePredicate(terminatePredicate)
        .setParentSelectFunction(new RouletteWheelParentSelectFunction<>())
        .setCrossoverFunction(new RandomCrossoverFunction<>(fitnessFunction))
        .setMutationFunction(mutationFunction)
        .build();
  }

  public void run() {
    geneticAlgorithm.run();
  }

  private FitnessFunction<Integer> fitnessFunction = chromosome -> chromosome.stream()
      .mapToInt(Gene::getValue)
      .filter(value -> value == 1)
      .sum() / (double) chromosome.size();

  private GeneGenerator<Integer> geneGenerator = () -> Math.random() < 0.5 ? new Gene<>(0) : new Gene<>(1);

  private Predicate<Population<Integer>> terminatePredicate = population -> population.getIndividuals().stream()
      .anyMatch(individual -> individual.getFitness() == 1.0);

  private MutationFunction<Integer> mutationFunction = gene -> {
    Integer newValue = gene.getValue() == 1 ? 0 : 1;
    return new Gene<>(newValue);
  };
}
