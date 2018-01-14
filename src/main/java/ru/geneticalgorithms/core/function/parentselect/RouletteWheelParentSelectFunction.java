package ru.geneticalgorithms.core.function.parentselect;

import ru.geneticalgorithms.core.model.Individual;
import ru.geneticalgorithms.core.model.Population;

import java.util.List;

/**
 * @author avnik
 */
public class RouletteWheelParentSelectFunction<T> implements ParentSelectFunction<T> {
  @Override
  public Individual<T> selectParent(Population<T> population) {
    List<Individual<T>> individuals = population.getIndividuals();

    // Spin roulette wheel
    double populationFitness = population.getPopulationFitness();
    double rouletteWheelPosition = Math.random() * populationFitness;

    // Find parent
    double spinWheel = 0;
    for (Individual<T> individual : individuals) {
      spinWheel += individual.getFitness();
      if (spinWheel >= rouletteWheelPosition) {
        return individual;
      }
    }

    return individuals.get(individuals.size() - 1);
  }
}
