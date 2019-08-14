package ypp170130;

import java.util.Scanner;

public class NagamochiIbaraki {


    /**
     * merge(G, s, t)
     *  G = G\{s,t} U {st}
     *
     *
     */

    /**
     * Drive Code for Nagamochi Ibaraki Algorithm
     * @param args
     */
    public static void main(String[] args) {
//        // take number of nodes and edges as param
//        // for now, just do it directly
//        // m edges, n nodes
//        int m, n;
//        n = 6; // nodes
//        m = 16; // edges
//        FlowGraph graph = new FlowGraph(n, m);
//        System.out.println(graph);
//        // the above is the random generation for a graph

        // now implement nagamochi ibaraki, run for sample graph
        String s = "6 11    0 1 0 4   0 2 0 7   0 4 0 10  1 3 0 2   1 5 0 10  2 1 0 2   2 3 0 10  2 4 0 2  3 5 0 7    4 3 0 2   4 5 0 6";
        Scanner in = new Scanner(s);
        FlowGraph graph = new FlowGraph(in);
        System.out.println(graph);
    }
}
