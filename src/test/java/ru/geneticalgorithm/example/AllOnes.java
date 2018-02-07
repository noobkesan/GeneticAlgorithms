package ru.geneticalgorithm.example;

import org.junit.Before;
import org.junit.Test;
import ru.geneticalgorithm.GeneticAlgorithm;
import ru.geneticalgorithm.function.FitnessFunction;
import ru.geneticalgorithm.function.GeneGenerator;
import ru.geneticalgorithm.function.crossover.RandomCrossoverFunction;
import ru.geneticalgorithm.function.mutation.RandomMutationFunction;
import ru.geneticalgorithm.function.parentselect.RouletteWheelParentSelectFunction;
import ru.geneticalgorithm.function.terminatecondition.DefaultTerminateCondition;
import ru.geneticalgorithm.model.Gene;

/**
 * @author avnik
 */
public class AllOnes {
  private GeneticAlgorithm<Integer> geneticAlgorithm;

  @Before
  public void setup() {
    this.geneticAlgorithm = new GeneticAlgorithm.Builder()
        .setFitnessFunction(fitnessFunction)
        .setGeneGenerator(geneGenerator)
        .setTerminateCondition(new DefaultTerminateCondition<>())
        .setParentSelectFunction(new RouletteWheelParentSelectFunction<>())
        .setCrossoverFunction(new RandomCrossoverFunction<>())
        .setMutationFunction(new RandomMutationFunction<>(geneGenerator))
        .build();
  }

  @Test
  public void testAllOnesSolver() {
    geneticAlgorithm.run();
  }

  private final FitnessFunction<Integer> fitnessFunction = chromosome -> chromosome.stream()
      .mapToInt(Gene::getValue)
      .filter(value -> value == 1)
      .sum() / (double) chromosome.size();

  private final GeneGenerator<Integer> geneGenerator = previousGenes -> Math.random() < 0.5 ? new Gene<>(0) : new Gene<>(1);
}