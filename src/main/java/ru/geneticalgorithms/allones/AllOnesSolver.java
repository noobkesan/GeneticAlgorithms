package ru.geneticalgorithms.allones;

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
        .setTerminateCondition(new DefaultTerminateCondition<>())
        .setParentSelectFunction(new RouletteWheelParentSelectFunction<>())
        .setCrossoverFunction(new RandomCrossoverFunction<>(fitnessFunction))
        .setMutationFunction(new RandomMutationFunction<>(geneGenerator))
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
}
