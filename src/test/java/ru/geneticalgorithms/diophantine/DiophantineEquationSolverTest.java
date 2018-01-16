package ru.geneticalgorithms.diophantine;

import org.junit.Test;
import ru.geneticalgorithms.core.model.Individual;

/**
 * @author avnik
 */
public class DiophantineEquationSolverTest {

  @Test
  public void testSolverWith3Roots() {
    DiophantineEquationSolver solver = new DiophantineEquationSolver(20, 3, 5, 2);
    Individual<Integer> result = solver.run();
    System.out.println(result);
  }

  @Test
  public void testSolverWith10Roots() {
    DiophantineEquationSolver solver = new DiophantineEquationSolver(81, 13, 8, 7, -5, -3, 6, 1, -1, 20, -3);
    Individual<Integer> result = solver.run();
    System.out.println(result);
  }
}