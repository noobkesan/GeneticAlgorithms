package ru.geneticalgorithms.diophantine;

import ru.geneticalgorithms.core.GeneticAlgorithm;
import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.function.MutationFunction;
import ru.geneticalgorithms.core.function.crossover.RandomCrossoverFunction;
import ru.geneticalgorithms.core.function.parentselect.RouletteWheelParentSelectFunction;
import ru.geneticalgorithms.core.model.Gene;
import ru.geneticalgorithms.core.model.Individual;
import ru.geneticalgorithms.core.model.Population;

import java.util.Random;
import java.util.function.Predicate;

/**
 * @author avnik
 */
public class DiophantineEquationSolver {
  private static final Random RANDOM = new Random();
  private int result;
  private int[] coefficients;
  private final GeneticAlgorithm<Integer> geneticAlgorithm;

  public DiophantineEquationSolver(int result, int... coefficients) {
    this.result = result;
    this.coefficients = coefficients;
    this.geneticAlgorithm = new GeneticAlgorithm.Builder()
        .setChromosomeLength(coefficients.length)
        .setPopulationSize(50)
        .setMutationRate(0.1)
        .setCrossoverRate(0.95)
        .setElitismCount(1)
        .setMaxGenerationsCount(100)
        .setFitnessFunction(fitnessFunction)
        .setGeneGenerator(geneGenerator)
        .setTerminatePredicate(terminatePredicate)
        .setParentSelectFunction(new RouletteWheelParentSelectFunction<>())
        .setCrossoverFunction(new RandomCrossoverFunction<>(fitnessFunction))
        .setMutationFunction(mutationFunction)
        .build();
  }

  public Individual<Integer> run() {
    return geneticAlgorithm.run();
  }

  private FitnessFunction<Integer> fitnessFunction = chromosome -> {
    int result = 0;
    for (int i = 0; i < coefficients.length; i++) {
      result += coefficients[i] * chromosome.get(i).getValue();
    }

    int difference = Math.abs(result - this.result);
    return 1 / Math.exp(difference);
  };

  private GeneGenerator<Integer> geneGenerator = () -> new Gene<>(1 + RANDOM.nextInt(result));

  private Predicate<Population<Integer>> terminatePredicate = population -> population.getIndividuals().stream()
      .anyMatch(individual -> individual.getFitness() == 1.0);

  private MutationFunction<Integer> mutationFunction = gene -> {
    Gene<Integer> newGene = geneGenerator.generateGene();
    while (newGene.getValue().equals(gene.getValue())) {
      newGene = geneGenerator.generateGene();
    }

    return newGene;
  };
}
