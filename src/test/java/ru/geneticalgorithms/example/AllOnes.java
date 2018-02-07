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