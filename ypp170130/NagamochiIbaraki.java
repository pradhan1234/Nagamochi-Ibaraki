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

  /**
   * Drive Code for Nagamochi Ibaraki Algorithm
   *
   * @param args
   */
  public static void main(String[] args) {
    NagamochiIbaraki ni = new NagamochiIbaraki();
    ni.createGraph(19, 100);
    ni.printGraph();
    ni.printStatus();
    System.out.println("Min Cut: " + ni.minCut());
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
      // parallel edges are allowed
      int w = 1 + random.nextInt(32);
      // if initially graph[u][v] = graph[v][u] = 0
      // indicates new edge of weight w is being added,
      // otherwise we have a parallel edge of weight w between vertices u and v.
      // we combine parallel edges on the go.
      if (graph[u][v] > 0) {
        countParallel++;
      }
      graph[u][v] += w;
      graph[v][u] += w;
    }
    this.E -= countParallel; // deduct parallel edges from edge count
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
          {0, 1, 1, 0},
          {1, 0, 1, 0},
          {1, 1, 0, 0},
          {0, 0, 0, 0},
        };
  }

  private boolean merge(int u, int v) {
    // we collapse v onto u
    // delete edges between them
    // hence avoiding self loops
    if (graph[u][v] > 0) {
      E--;
      graph[u][v] = graph[v][u] = 0; //  was this edge there??
    }
    for (int i = 0; i < _V; i++) {
      // this contains bugs
      // delete edges of v
      if ((graph[v][i] > 0 && graph[u][i] > 0)) {
        E--;
      }
      // add edges of v to u, merging parallel edges on the go
      graph[u][i] += graph[v][i];
      graph[i][u] += graph[i][v];

      graph[v][i] = 0;
      graph[i][v] = 0;
    }
    // delete vertex
    present[v] = false;
    this.V--;

    return true;
  }

  private State[] minCutPhase() {
    State[] A = new State[_V]; // make this enum
    Arrays.fill(A, State.NOT_IN_CUT);
    for (int i = 0; i < _V; i++) {
      if (!present[i]) {
        A[i] = State.DELETED;
      }
    }
    int a = getArbitaryVertex();
    A[a] = State.IN_CUT;
    int countA = 1, lastIndex = a, secondLast = -1;
    while (countA != V) {
      int v = getMostTightVertex(A);
      A[v] = State.IN_CUT;
      secondLast = lastIndex;
      lastIndex = v;
      countA++;
    }

    A[secondLast] = State.SECOND_LAST;
    A[lastIndex] = State.LAST;
    return A;
  }

  private int minCut() {
    int w_min_cut = Integer.MAX_VALUE;
    State[] min_cut;

    while (V > 1) {
      State[] s_t_phase_cut = minCutPhase();
      // is this better?
      int w_s_t_phase_cut = weight(s_t_phase_cut);
      if (w_s_t_phase_cut < w_min_cut) {
        w_min_cut = w_s_t_phase_cut;
        min_cut = s_t_phase_cut;
      }
      if (!mergeST(s_t_phase_cut)) // disconnected graph
      return 0;
    }
    return w_min_cut;
  }

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
    if (s == -1 || t == -1) {
      // problem detected
      return false;
    }
    return merge(s, t);
  }

  private int weight(State[] cut) {
    int t = -1;
    for (int i = 0; i < _V; i++) {
      if (cut[i] == State.LAST) {
        t = i;
        break;
      }
    }
    int w = 0;
    for (int i = 0; i < _V; i++) {
      if (cut[i] == State.IN_CUT || cut[i] == State.SECOND_LAST) {
        w += graph[i][t];
      }
    }
    return w;
  }

  private int getMostTightVertex(State[] A) {
    int[] strength = new int[_V];
    Arrays.fill(strength, 0);
    for (int v = 0; v < _V; v++) {
      if (A[v] == State.NOT_IN_CUT) {
        for (int i = 0; i < _V; i++) {
          if (A[i] == State.IN_CUT || A[i] == State.SECOND_LAST) {
            strength[v] += graph[v][i];
          }
        }
      }
    }
    int index = 0, maxVal = 0;
    for (int i = 0; i < _V; i++) {
      if (maxVal < strength[i]) {
        maxVal = strength[i];
        index = i;
      }
    }
    return index;
  }

  private int getArbitaryVertex() {
    // collect present vertices in temp
    int[] temp = new int[_V];
    int cursor = 0;
    for (int i = 0; i < _V; i++) {
      if (present[i]) {
        temp[cursor++] = i;
      }
    }
    // choose any one randomly
    return temp[random.nextInt(cursor)];
  }

  // deterministic way to get a vertex
  private int getNextVertex() {
    for (int i = 0; i < _V; i++) {
      if (present[i]) return i;
    }
    return -1;
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

  public enum State {
    DELETED,
    IN_CUT,
    NOT_IN_CUT,
    LAST,
    SECOND_LAST
  }
}
