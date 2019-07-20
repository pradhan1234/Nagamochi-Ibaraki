package ypp170130;

import java.util.Arrays;
import java.util.Random;

public class NagamochiIbaraki {

  // weighted adjacency matrix to represent graph
  private int[][] graph;
  // random number generator
  private Random random;
  private int _V, _E;
  private int V, E;
  private boolean[] present;

  private NagamochiIbaraki() {
    random = new Random();
  }

  private void init(int V, int E) {
    this.V = this._V = V;
    this.E = this._E = E;
    present = new boolean[V];
    Arrays.fill(present, true);
    // initialize empty graph with just V vertices
    graph = new int[V][V];
  }
  // create a random graph with V vertices and E edges
  private void createGraph(int V, int E) {
    init(V, E);
    // add E edges to this graph
    for (int e = 0; e < E; e++) {
      // randomly select 2 vertices
      int u = random.nextInt(V);
      int v = random.nextInt(V);
      // self loops not allowed
      while (v == u) {
        v = random.nextInt(V);
      }
      // parallel edges are allowed
      int w = 1 + random.nextInt(32);
      // if initially graph[u][v] = graph[v][u] = 0
      // indicates new edge of weight w is being added,
      // otherwise we have a parallel edge of weight w between vertices u and v.
      graph[u][v] += w;
      graph[v][u] += w;
    }
  }

  // this is the one from slides
  private void createSampleGraph() {
    init(6, 7);
    graph =
        new int[][] {
          {0, 6, 0, 0, 0, 0},
          {6, 0, 8, 3, 0, 0},
          {0, 8, 0, 0, 1, 0},
          {0, 3, 0, 0, 20, 5},
          {0, 0, 1, 20, 0, 2},
          {0, 0, 0, 5, 2, 0},
        };
  }

  private void createSampleGraph2() {
    init(4, 5);
    graph =
        new int[][] {
          {0, 1, 1, 1},
          {1, 0, 1, 0},
          {1, 1, 0, 1},
          {1, 0, 1, 0},
        };
  }

  private void merge(int u, int v) {
    // we collapse v onto u
    // delete edges between them
    // hence avoiding self loops
    graph[u][v] = graph[v][u] = 0;
    for (int i = 0; i < _V; i++) {
      // add edges of v to u, merging parallel edges on the go
      graph[u][i] += graph[v][i];
      graph[i][u] += graph[i][v];
      // delete edges of v
      graph[v][i] = 0;
      graph[i][v] = 0;
    }
    // delete vertex
    present[v] = false;
    this.V--;
  }

  private void printStatus() {
    for (int i = 0; i < _V; i++) {
      System.out.print(i + ": " + present[i] + ", ");
    }
    System.out.println();
  }

  // utility to print the graph
  private void printGraph() {
    if (graph == null) {
      System.out.println("create graph by calling createSampleGraph() or CreateGraph()");
      return;
    }
    System.out.println("#Vertices: " + V + "; #Edges: " + E);
    System.out.println("------------------------------------");
    for (int i = 0; i < _V; i++) {
      for (int j = 0; j < _V; j++) {
        System.out.print(graph[i][j] + "\t");
      }
      System.out.println();
    }
    System.out.println("------------------------------------");
  }

  /**
   * Drive Code for Nagamochi Ibaraki Algorithm
   *
   * @param args
   */
  public static void main(String[] args) {
    NagamochiIbaraki ni = new NagamochiIbaraki();
    // ni.createGraph(6, 20);
    ni.createSampleGraph();
    // ni.createSampleGraph2();
    ni.printGraph();
    ni.printStatus();
    ni.merge(4, 5);
    ni.printGraph();
    ni.printStatus();
  }
}
