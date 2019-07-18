package ypp170130;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Implementation of the classic Ford-Fulkerson Algorithm to compute
 * Max Flow and Min ST-Cut in a Flow Network.
 *
 * This Implementation used the Edmand Karp - Shortest Augmenting Path heuristic
 *
 * FlowEdge and FlowGraph are dependency.
 *
 * This assumes a valid graph as input with valid non-negative integer flows and valid non-negative integer capacities.
 *
 * Runtime: |V||E^2|
 *
 * @author Yash Pradhan
 */
public class FordFulkerson {

    private FlowGraph G; // store graph

    // each uplink[v] store edge u-->v that incidents in v.
    private FlowEdge[] uplink; // to reconstruct the augmenting path
    private boolean[] visited; // visited[v]: s-->v path exists
    private int maxflow;

    /**
     * Constructor
     * @param _G FlowGraph
     */
    public FordFulkerson(FlowGraph _G) {
        G = _G;
    }

    /**
     * Executes the Ford Fulkerson Algorithm
     * @param s source vertex
     * @param t destination vertex
     */
    public void run(int s, int t) {
        // we could have flow already in network, like given in assignment
        maxflow = getFlow();
        // augmenting path exists?
        while(hasAugmentingPath(s, t)) {
            int bottleneck = Integer.MAX_VALUE;
            // compute bottleneck
            for(int u = t; u != s; u = uplink[u].getOtherVertex(u)) {
                bottleneck = Math.min(bottleneck, uplink[u].getResidualCapacity(u));
            }
            // augment flow
            for(int u = t; u != s; u = uplink[u].getOtherVertex(u)) {
                uplink[u].augmentFlow(u, bottleneck);
            }
            maxflow += bottleneck;
        }
    }

    /**
     * Uses Breadth First Search Algorithm
     * Edmand Karp Heuristic to use to Shortest Path
     * @param s source
     * @param t target
     * @return true if augmenting path exists s ~~~> t, false otherwise.
     */
    private boolean hasAugmentingPath(int s, int t) {
        // initialize uplink[] and visited[]
        uplink = new FlowEdge[G.V()];
        visited = new boolean[G.V()];
        // BFS algorithm for finding path
        Queue<Integer> q = new LinkedList<>();
        q.add(s); // add source

        while(!q.isEmpty()) {
            int u = q.remove();
            for(FlowEdge e : G.getNeighbours(u)) {
                int v = e.getOtherVertex(u);
                // we only consider edge with residual capacity > 0 in residual graph.
                if(e.getResidualCapacity(v) > 0) {
                    if(!visited[v]) {
                        uplink[v] = e; // store edge
                        visited[v] = true;
                        if(v == t) {
                            // reached destination
                            return true;
                        }
                        q.add(v);
                    }
                }
            }
        }
        return visited[t];
    }

    /**
     * Find the current flow in network
     * @return current flow
     */
    private int getFlow() {
        int flow = 0;
        // source: 0
        for(FlowEdge e : G.getNeighbours(0)) {
             flow += e.getFlow();
        }
        return flow;
    }

    /**
     * To be called after execution of FordFulkerson Algorithm
     * @return maxflow
     */
    public int getMaxflow() {
        return maxflow;
    }

    /**
     * Computes the MinCut
     * For the last search for augmenting path, which failed, because we reached max flow
     * The nodes that were reachable from source, belongs to set S and others to T.
     * We have this information in the visited[].
     */
    private void printMinCut() {
        System.out.print("S: ");
        for(int u = 0; u < G.V(); u++) {
            String t;
            if(visited[u]) {
                System.out.print(u + " ");
            } else {
                System.out.print("");
            }
        }
        System.out.print("\nT: ");
        for(int u = 0; u < G.V(); u++) {
            if(!visited[u]) {
                System.out.print(u + " ");
            } else {
                System.out.print("");
            }
        }
        System.out.println("\n");
    }

    /**
     * Driver Code
     * The string which is passed to Scanner is of the form
     * Example: V E   u0 v0 f0 c0 ... uE-1 vE-1 fE-1 cE-1
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Example with 0 flow in given graph.
        //String s = "6 11    0 1 0 4   0 2 0 7   0 4 0 10  1 3 0 2   1 5 0 10  2 1 0 2   2 3 0 10  2 4 0 2  3 5 0 7    4 3 0 2   4 5 0 6";

        // Example in Assignment
        String s = "6 11    0 1 4 4   0 2 7 7   0 4 6 10  1 3 0 2   1 5 4 10  2 1 0 2   2 3 7 10  2 4 0 2  3 5 7 7    4 3 0 2   4 5 6 6";

        Scanner in;
        in = args.length > 0 ? new Scanner(new java.io.File(args[0])) : new Scanner(s);


        // create graph
        FlowGraph graph = new FlowGraph(in);
        FordFulkerson ff = new FordFulkerson(graph);
        System.out.println("Current Flow: " + ff.getFlow());
        ff.run(0, 5);   // execute

        // results
        System.out.println("\nResults:\n");
        System.out.println(graph);
        System.out.println("Max Flow: " + ff.getMaxflow());

        System.out.println("\nMin Cut:");
        ff.printMinCut();
    }
}
