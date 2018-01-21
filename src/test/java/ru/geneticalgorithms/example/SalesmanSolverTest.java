package ru.geneticalgorithms.example;

import org.junit.Test;

/**
 * @author avnik
 */
public class SalesmanSolverTest {

  @Test
  public void testWith3Vertices() {
    double[][] weightMatrix = {
        {0, 10, 5, 12},
        {10, 0, 8, 3},
        {5, 8, 0, 4},
        {12, 3, 4, 0},
    };

    SalesmanSolver solver = new SalesmanSolver(weightMatrix);
    solver.run();
  }

  @Test
  public void testWith6Vertices() {
    double[][] weightMatrix = {
        {0, 1, 3, 4, 5, 6},
        {1, 0, 2, 3, 4, 5},
        {3, 2, 0, 3, 4, 5},
        {4, 3, 3, 0, 4, 5},
        {5, 4, 4, 4, 0, 5},
        {6, 5, 5, 5, 5, 0},
    };

    SalesmanSolver solver = new SalesmanSolver(weightMatrix);
    solver.run();
  }
}