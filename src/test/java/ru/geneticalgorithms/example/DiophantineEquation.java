package ru.geneticalgorithms.example;

import org.junit.Before;
import org.junit.Test;
import ru.geneticalgorithms.core.GeneticAlgorithm;
import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.function.crossover.RandomCrossoverFunction;
import ru.geneticalgorithms.core.function.mutation.RandomMutationFunction;
import ru.geneticalgorithms.core.function.parentselect.RouletteWheelParentSelectFunction;
import ru.geneticalgorithms.core.function.terminatecondition.DefaultTerminateCondition;
import ru.geneticalgorithms.core.model.Gene;

import java.util.Random;

/**
 * @author avnik
 */
public class DiophantineEquation {
  private static final Random RANDOM = new Random();

  private GeneticAlgorithm.Builder builder;

  private int result;
  private int[] coefficients;

  @Before
  public void setup() {
    this.builder = new GeneticAlgorithm.Builder()
        .setElitismCount(1)
        .setMaxGenerationsCount(100)
        .setFitnessFunction(fitnessFunction)
        .setGeneGenerator(geneGenerator)
        .setTerminateCondition(new DefaultTerminateCondition<>())
        .setParentSelectFunction(new RouletteWheelParentSelectFunction<>())
        .setCrossoverFunction(new RandomCrossoverFunction<>())
        .setMutationFunction(new RandomMutationFunction<>(geneGenerator));
  }

  @Test
  public void testSolverWith3Roots() {
    this.result = 20;
    this.coefficients = new int[]{3, 5, 2};

    newGeneticAlgorithm().run();
  }

  @Test
  public void testSolverWith10Roots() {
    this.result = 81;
    this.coefficients = new int[]{13, 8, 7, -5, -3, 6, 1, -1, 20, -3};

    newGeneticAlgorithm().run();
  }

  private GeneticAlgorithm<Integer> newGeneticAlgorithm() {
    return this.builder
        .setChromosomeLength(coefficients.length)
        .build();
  }

  private final FitnessFunction<Integer> fitnessFunction = chromosome -> {
    int result = 0;
    for (int i = 0; i < coefficients.length; i++) {
      result += coefficients[i] * chromosome.get(i).getValue();
    }

    int difference = Math.abs(result - this.result);
    return 1 / Math.exp(difference);
  };

  private final GeneGenerator<Integer> geneGenerator = previousGenes -> new Gene<>(1 + RANDOM.nextInt(result));
}