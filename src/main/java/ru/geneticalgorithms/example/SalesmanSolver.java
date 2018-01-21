package ru.geneticalgorithms.example;

import ru.geneticalgorithms.core.GeneticAlgorithm;
import ru.geneticalgorithms.core.exception.GeneticAlgorithmException;
import ru.geneticalgorithms.core.function.FitnessFunction;
import ru.geneticalgorithms.core.function.GeneGenerator;
import ru.geneticalgorithms.core.function.crossover.CrossoverFunction;
import ru.geneticalgorithms.core.function.mutation.MutationFunction;
import ru.geneticalgorithms.core.function.parentselect.RouletteWheelParentSelectFunction;
import ru.geneticalgorithms.core.model.Gene;
import ru.geneticalgorithms.core.model.Individual;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author avnik
 */
public class SalesmanSolver {
  private static final Random RANDOM = new Random();
  private double[][] weightMatrix;
  private final GeneticAlgorithm<Integer> geneticAlgorithm;

  public SalesmanSolver(double[][] weightMatrix) {
    this.weightMatrix = checkWeightMatrix(weightMatrix);
    this.geneticAlgorithm = new GeneticAlgorithm.Builder()
        .setChromosomeLength(weightMatrix.length)
        .setPopulationSize(50)
        .setMutationRate(0.1)
        .setCrossoverRate(0.95)
        .setElitismCount(1)
        .setMaxGenerationsCount(100)
        .setFitnessFunction(fitnessFunction)
        .setIndividualComparator(Comparator.comparing(Individual::getFitness))
        .setGeneGenerator(geneGenerator)
        .setTerminateCondition(population -> false)
        .setParentSelectFunction(new RouletteWheelParentSelectFunction<>())
        .setCrossoverFunction(crossoverFunction)
        .setMutationFunction(mutationFunction)
        .build();
  }

  public Individual<Integer> run() {
    return geneticAlgorithm.run();
  }

  private FitnessFunction<Integer> fitnessFunction = chromosome -> {
    double cost = 0;
    for (int i = 0; i < chromosome.size() - 1; i++) {
      int rowIndex = chromosome.get(i).getValue();
      int colIndex = chromosome.get(i + 1).getValue();
      cost += weightMatrix[rowIndex][colIndex];
    }

    return cost;
  };

  private GeneGenerator<Integer> geneGenerator = previousGenes -> {
    Set<Integer> previousValuesSet = previousGenes.stream()
        .map(Gene::getValue)
        .collect(Collectors.toSet());

    int newValue = RANDOM.nextInt(weightMatrix.length);
    while (previousValuesSet.contains(newValue)) {
      newValue = RANDOM.nextInt(weightMatrix.length);
    }

    return new Gene<>(newValue);
  };

  private CrossoverFunction<Integer> crossoverFunction = (parent1, parent2) -> {
    List<Gene<Integer>> parent1Chromosome = parent1.getChromosome();
    List<Gene<Integer>> parent2Chromosome = parent2.getChromosome();
    List<Gene<Integer>> newGenes = new ArrayList<>(parent1Chromosome.size());

    for (int geneIndex = 0; geneIndex < parent1Chromosome.size(); geneIndex++) {
      Gene<Integer> parent1Gene = parent1Chromosome.get(geneIndex);
      Gene<Integer> parent2Gene = parent1Chromosome.get(geneIndex);
      Gene<Integer> gene = null;

      if(newGenes.contains(parent1Gene)) {
        gene = parent2Chromosome.get(geneIndex);
      } else if(newGenes.contains(parent2Gene)) {
        gene = parent1Gene;
      } else {
        gene = RANDOM.nextDouble() < 0.5 ? parent1Gene : parent2Gene;
      }

      newGenes.add(gene);
    }

    return new Individual<>(newGenes);
  };

  private MutationFunction<Integer> mutationFunction = (gene, geneIndex, chromosome) -> {
    Gene<Integer> newGene = geneGenerator.generateGene(Collections.emptyList());
    while (newGene.getValue().equals(gene.getValue())) {
      newGene = geneGenerator.generateGene(Collections.emptyList());
    }

    int newGeneOldIndex = -1;
    for (int i = 0; i < chromosome.size(); i++) {
      if (newGene.getValue().equals(chromosome.get(i).getValue())) {
        newGeneOldIndex = i;
        break;
      }
    }

    List<Gene<Integer>> newChromosome = new ArrayList<>(chromosome);
    newChromosome.set(geneIndex, newGene);
    newChromosome.set(newGeneOldIndex, gene);

    return newChromosome;
  };

  private double[][] checkWeightMatrix(double[][] weightMatrix) throws GeneticAlgorithmException {
    if(weightMatrix == null || weightMatrix.length == 0) {
      throw new GeneticAlgorithmException("Weight matrix must exist!");
    }

    for (double[] row : weightMatrix) {
      if(row == null) {
        throw new GeneticAlgorithmException("Weight row must exist!");
      }

      if(row.length != weightMatrix.length) {
        throw new GeneticAlgorithmException("Weight matrix must be square!");
      }

      boolean hasNegativeWeights = Arrays.stream(row).anyMatch(weight -> weight < 0);
      if(hasNegativeWeights) {
        throw new GeneticAlgorithmException("Weight must be > 0!");
      }
    }

    return weightMatrix;
  }
}
