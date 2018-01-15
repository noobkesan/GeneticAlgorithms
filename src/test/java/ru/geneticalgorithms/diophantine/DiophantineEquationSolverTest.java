package ru.geneticalgorithms.diophantine;

import org.junit.Test;
import ru.geneticalgorithms.core.model.Individual;

/**
 * @author avnik
 */
public class DiophantineEquationSolverTest {

  @Test
  public void test() {
    DiophantineEquationSolver solver = new DiophantineEquationSolver(20, 3, 5, 2);
    Individual<Integer> result = solver.run();
    System.out.println(result);
  }
}