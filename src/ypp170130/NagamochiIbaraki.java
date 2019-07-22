package ypp170130;

import java.util.Arrays;
import java.util.Random;

/**
 * author: yash pradhan (ypp170130)
 *
 * <p>This code is an implementation of Nagamochi Ibaraki algorithm to compute the min cut of an
 * undirected graph.
 *
 * <p>This Implementation uses Adjacency Matrix to represent the graph.
 */
public class NagamochiIbaraki {

  // to keep track of vertices if they are in the cut or not in the cut
  State[] min_cut;
  // weighted adjacency matrix
  // to represent graph
  private int[][] graph;
  // random number generator
  private Random random;
  private int _V, _E; // initial vertices and edges
  private int V, E; // tracks count of vertices and edges
  // to keep track of deleted vertices after merge
  private boolean[] present; // present[v] = false indicates, that it was merged

  /** Default Constructor */
  private NagamochiIbaraki() {
    random = new Random();
  }

  /**
   * Drive Code for Nagamochi Ibaraki Algorithm
   *
   * @param args
   */
  public static void main(String[] args) {

    NagamochiIbaraki ni = new NagamochiIbaraki();
    // slides example
    System.out.println("Slides Example:");
    ni.createSampleGraph();
    ni.printGraph();
    System.out.println("Min Cut: " + ni.minCut());

    System.out.println("\n\nRandom Run");
    // random graphs
    for (int i = 50; i <= 550; i += 5) {
      ni.createGraph(25, i);
      System.out.println(" #edges " + i + ": " + ni.minCut());
    }
  }

  /**
   * init() initializes few class level variables.
   *
   * @param V count of vertices in the graph
   * @param E count of edges in the graph
   */
  private void init(int V, int E) {
    this.V = this._V = V;
    this.E = this._E = E;
    present = new boolean[V];
    // mark all vertices as present
    Arrays.fill(present, true);
    // initialize empty graph with just V vertices
    graph = new int[V][V];
  }

  /**
   * Creates a random graph with V vertices and E edges
   *
   * @param V count of vertices
   * @param E count of edges
   */
  private void createGraph(int V, int E) {
    init(V, E); // call to init
    int countParallel = 0;
    // add E edges to this graph
    for (int e = 0; e < E; e++) {
      // randomly select 2 vertices
      int u = random.nextInt(V);
      int v = random.nextInt(V);
      // self loops not allowed
      while (v == u) {
        v = random.nextInt(V);
      }
      // randomly choose a weight for this edge
      int w = 1 + random.nextInt(32);
      // parallel edges are allowed
      // if initially graph[u][v] = graph[v][u] = 0
      // indicates new edge of weight w is being added for first time between u and v.
      // otherwise we have a parallel edge of weight w between vertices u and v.
      // parallel edges are 'combined' on the go.
      if (graph[u][v] > 0) {
        countParallel++;
      }
      // assign weight
      graph[u][v] += w;
      graph[v][u] += w;
    }
    // deduct parallel edges from edge count
    this.E -= countParallel;
  }

  /**
   * Creates a Sample Graph This is the one given in the slides Value of min cut for this graph is
   * 4.
   */
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

  /**
   * Merges vertices u and v into u. Steps: 1. delete edges between u and v. 2. assign all edges of
   * u to v, combining parallel edges as they arise 3. mark vertex v as deleted
   *
   * @param u prior of the vertices to be merged, this would exist after merge
   * @param v posterior of vertices to be merged, this would be deleted after merge
   * @return true on successful merge
   */
  private boolean merge(int u, int v) {
    // is this edge present??
    if (graph[u][v] > 0) {
      E--;
      graph[u][v] = graph[v][u] = 0;
    }

    // assign all edges of u to v,
    for (int i = 0; i < _V; i++) {
      // delete edges of v if they exist
      if (graph[v][i] > 0 && graph[u][i] > 0) {
        E--;
      }
      // add edges of v to u, merging parallel edges on the go
      graph[u][i] += graph[v][i];
      graph[i][u] += graph[i][v];

      graph[v][i] = 0;
      graph[i][v] = 0;
    }
    // delete vertex v
    present[v] = false;
    this.V--;

    return true;
  }

  /**
   * Computes the cut
   *
   * @return State[] indicates whether vertex is in cut or not
   */
  private State[] minCutPhase() {
    State[] A = new State[_V];
    // default value
    Arrays.fill(A, State.NOT_IN_CUT);
    // mark deleted, as we dont want to consider them any further
    for (int i = 0; i < _V; i++) {
      if (!present[i]) {
        A[i] = State.DELETED;
      }
    }

    int a = getArbitaryVertex();
    A[a] = State.IN_CUT; // mark in cut

    int countA = 1, lastIndex = a, secondLast = -1;
    while (countA != V) {
      int v = getMostTightVertex(A);
      A[v] = State.IN_CUT;
      secondLast = lastIndex;
      lastIndex = v;
      countA++;
    }
    // mark s and t
    A[secondLast] = State.SECOND_LAST;
    A[lastIndex] = State.LAST;
    return A;
  }

  /**
   * Computes the Min Cut
   *
   * @return value of the min cut
   */
  private int minCut() {
    int w_min_cut = Integer.MAX_VALUE;
    while (V > 1) {
      // get s-t-phase cut
      State[] s_t_phase_cut = minCutPhase();
      int w_s_t_phase_cut = weight(s_t_phase_cut);
      // is this better?
      if (w_s_t_phase_cut < w_min_cut) {
        w_min_cut = w_s_t_phase_cut;
        min_cut = s_t_phase_cut;
      }
      if (!mergeST(s_t_phase_cut)) {
        // case: disconnected graph
        return 0;
      }
    }
    // return the weight of the min cut for the graph
    return w_min_cut;
  }

  /**
   * It determines the s and t and calls merge on them
   *
   * @param s_t_phase_cut
   * @return true on successful merge
   */
  private boolean mergeST(State[] s_t_phase_cut) {
    int s = -1, t = -1;
    for (int i = 0; i < _V; i++) {
      if (s != -1 && t != -1) break;
      if (s_t_phase_cut[i] == State.LAST) {
        t = i;
        continue;
      }
      if (s_t_phase_cut[i] == State.SECOND_LAST) {
        s = i;
        continue;
      }
    }
    // now s and t should have the vertec index
    if (s == -1 || t == -1) {
      // this happens if its a disconnected graph
      return false;
    }
    // call merge
    return merge(s, t);
  }

  /**
   * Computes weight of the cut
   *
   * @param cut
   * @return weight of the cut
   */
  private int weight(State[] cut) {
    int t = -1;
    // determine t
    for (int i = 0; i < _V; i++) {
      if (cut[i] == State.LAST) {
        t = i;
        break;
      }
    }
    // find weight
    int w = 0;
    for (int i = 0; i < _V; i++) {
      if (cut[i] == State.IN_CUT || cut[i] == State.SECOND_LAST) {
        w += graph[i][t];
      }
    }
    return w;
  }

  /**
   * Finds the vertex which is not in cut with highest strength into A
   *
   * @param A current cut
   * @return index of most tight vertex
   */
  private int getMostTightVertex(State[] A) {
    int[] strength = new int[_V];
    Arrays.fill(strength, 0);
    // determine strength of the vertices
    for (int v = 0; v < _V; v++) {
      if (A[v] == State.NOT_IN_CUT) {
        for (int i = 0; i < _V; i++) {
          if (A[i] == State.IN_CUT || A[i] == State.SECOND_LAST) {
            strength[v] += graph[v][i];
          }
        }
      }
    }
    // find the most strong one
    int index = 0, maxVal = 0;
    for (int i = 0; i < _V; i++) {
      if (maxVal < strength[i]) {
        maxVal = strength[i];
        index = i;
      }
    }
    return index;
  }

  /** @return index of an arbitary vertex present in the graph */
  private int getArbitaryVertex() {
    int[] temp = new int[_V];
    int cursor = 0;
    // collect present vertices in temp
    for (int i = 0; i < _V; i++) {
      if (present[i]) {
        temp[cursor++] = i;
      }
    }
    // choose any one randomly
    return temp[random.nextInt(cursor)];
  }

  /**
   * deterministic way to get a vertex like in the slides example
   *
   * @return index of next smallest vertex
   */
  private int getNextVertex() {
    for (int i = 0; i < _V; i++) {
      if (present[i]) return i;
    }
    return -1;
  }

  /** prints present[] array */
  private void printStatus() {
    for (int i = 0; i < _V; i++) {
      System.out.print(i + ": " + present[i] + ", ");
    }
    System.out.println();
  }

  /** prints the graph */
  private void printGraph() {
    if (graph == null) {
      System.out.println(
          "Please create graph first bby calling createSampleGraph() or CreateGraph()");
      return;
    }
    System.out.println("#Vertices: " + V + "; #Edges: " + E);
    System.out.println("------------------------------------");
    for (int i = 0; i < _V; i++) {
      System.out.print("{");
      for (int j = 0; j < _V; j++) {
        System.out.print(graph[i][j] + ",\t");
      }
      System.out.print("},\n");
    }
    System.out.println("------------------------------------");
  }

  /**
   * prints the cut
   *
   * @param arr
   */
  private void printSet(State[] arr) {
    for (int i = 0; i < _V; i++) {
      System.out.print(i + ": " + arr[i] + ", ");
    }
    System.out.println();
  }

  /** To determine vertex status note: vertex s is in cut vertex t is not in cut */
  public enum State {
    DELETED,
    IN_CUT,
    NOT_IN_CUT,
    LAST, // vertex t
    SECOND_LAST // vertex s
  }
}
