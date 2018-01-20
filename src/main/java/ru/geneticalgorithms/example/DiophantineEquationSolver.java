package ru.geneticalgorithms.example;

import ru.geneticalgorithms.core.GeneticAlgorithm;
import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.function.crossover.RandomCrossoverFunction;
import ru.geneticalgorithms.core.function.mutation.RandomMutationFunction;
import ru.geneticalgorithms.core.function.parentselect.RouletteWheelParentSelectFunction;
import ru.geneticalgorithms.core.function.terminatecondition.DefaultTerminateCondition;
import ru.geneticalgorithms.core.model.Gene;
import ru.geneticalgorithms.core.model.Individual;

import java.util.Random;

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
        .setTerminateCondition(new DefaultTerminateCondition<>())
        .setParentSelectFunction(new RouletteWheelParentSelectFunction<>())
        .setCrossoverFunction(new RandomCrossoverFunction<>())
        .setMutationFunction(new RandomMutationFunction<>(geneGenerator))
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

  private GeneGenerator<Integer> geneGenerator = previousGenes -> new Gene<>(1 + RANDOM.nextInt(result));
}
