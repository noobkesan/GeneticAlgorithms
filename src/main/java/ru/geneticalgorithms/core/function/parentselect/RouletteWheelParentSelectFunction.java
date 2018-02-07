package ru.geneticalgorithms.core.function.parentselect;

import ru.geneticalgorithms.core.model.Individual;

import java.util.List;

/**
 * @author avnik
 */
public class RouletteWheelParentSelectFunction<T> implements ParentSelectFunction<T> {
  @Override
  public Individual<T> selectParent(List<Individual<T>> individuals, double populationFitness) {
    // Spin roulette wheel
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
