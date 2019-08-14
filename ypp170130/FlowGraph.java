package ypp170130;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * FlowGraph represents a Graph(Network)
 * It consists of V Vertices and E FlowEdges
 * For simplicity, the vertices are labeles from 0 to V-1.
 * 0 indicates Source Vertex, V-1 indicates Sink(Destination) Vertex
 *
 * Representation wise this uses an Adjacency List to store the network.
 * It has methods to access V and E and allows to add edges and obtain neighbours of a vertex.
 *
 * FlowEdge is a dependency.
 *
 * @author Yash Pradhan
 */
public class FlowGraph {
    private int V;  // count of vertices
    private int E;  // count of edges
    private LinkedList<FlowEdge>[] neighbours;

    /**
     * Constructor to Initialize a graph of V vertices and 0 Edges.
     * @param V Number of Vertices
     */
    public FlowGraph(int V) {
        this.V = V;
        this.E = 0;
        neighbours = new LinkedList[V];
        // each vertex holds List containing corresponding edges
        // initializing that empty list.
        for(int v = 0; v < V; v++) {
            neighbours[v] = new LinkedList<>();
        }
    }

    /**
     * Constructor to create a graph from provided Scanner instance.
     * @param in scanner instance representing graph
     *           Example: V E   u0 v0 f0 c0 ... uE-1 vE-1 fE-1 cE-1
     */
    public FlowGraph(Scanner in) {
        this(in.nextInt()); // call default constructor
        int E = in.nextInt();
        int u, v, f, c;
        for(int e = 0; e < E; e++) {
            u = in.nextInt();
            v = in.nextInt();
            f = in.nextInt();
            c = in.nextInt();
            addEdge(new FlowEdge(u, v, f, c));
        }
    }

    /**
     * Creates a graph randomly.
     *
     * @param V number of nodes
     * @param E number of edges
     */
    public FlowGraph(int V, int E) {
        this(V);
        int u, v, c;
        Random random = new Random();
        for(int e = 0; e < E; e++) {
            u = random.nextInt(V);
            v = random.nextInt(V);
            // avoid self loops
            while(v == u) {
                v = random.nextInt(V);
            }
            c = random.nextInt(128);
            addEdge(new FlowEdge(u, v, c));
        }
    }

    /**
     * Return count of Vertices in FlowGraph
     * @return V
     */
    public int V() {
        return V;
    }

    /**
     * Return count of Edges in FlowGraph
     * @return E
     */
    public int E() {
        return E;
    }

    /**
     * Adds an edge represented by FlowEdge e to FlowGraph
     * @param e FlowEdge
     */
    public void addEdge(FlowEdge e) {
        int u, v;
        u = e.getFrom();
        v = e.getTo();
        neighbours[u].add(e);
        neighbours[v].add(e);
        E++;
    }

    /**
     * Return List of FlowEdges adjacent to Vertex u.
     * @param u vertex
     * @return List of FlowEdges adjacent to Vertex u.
     */
    public List<FlowEdge> getNeighbours(int u) {
        return neighbours[u];
    }

    /**
     *
     * @return String representation of the FlowNetwork
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Vertices:\t"+ V + "\nEdges:\t\t" + E + "\n\n");
        for (int v = 0; v < V; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : neighbours[v]) {
                if (e.getTo() != v) {
                    s.append(e + ";  ");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }
}
