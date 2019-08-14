package ypp170130;

/**
 * FlowEdge represents an edge in FlowGraph
 * Each FlowEdge links two vertices, and has a capacity and flow.
 * It provides methods to access these private members and,
 * also to obtain residual flow capacity and augment flow.
 *
 * @author Yash Pradhan
 */
public class FlowEdge {

    private int u; // from vertex
    private int v; // to vertex
    private int c; // capacity of edge
    private int f; // flow on edge

    /**
     * Constructor for initializing FlowEdge between u and v,
     * with capacity c and f flow.
     * @param u from
     * @param v to
     * @param f flow
     * @param c capacity
     */
    public FlowEdge(int u, int v, int f, int c) {
        this.u = u;
        this.v = v;
        this.c = c;
        this.f = f;
    }

    /**
     * Constructor for initializing FlowEdge between u and v,
     * with capacity c and 0 flow.
     *
     * This internally calls FlowEdge(int u, int v, int f, int c) with f = 0.
     *
     * @param u from
     * @param v to
     * @param c capacity
     */
    public FlowEdge(int u, int v, int c) {
        this(u, v, 0, c);
    }

    /**
     * FlowEdge: u --> v
     * @return u (from vertex)
     */
    public int getFrom() {
        return u;
    }

    /**
     * FlowEdge: u --> v
     * @return v (to vertex)
     */
    public int getTo() {
        return v;
    }

    /**
     * Capacity of FlowEdge
     * @return c (capacity)
     */
    public int getCapacity() {
        return c;
    }

    /**
     * Current flow on FlowEdge
     * @return f
     */
    public int getFlow() {
        return f;
    }

    /**
     * Return vertex on other end of edge
     * @param _v vertex on one end
     * @return vertex on other end
     */
    public int getOtherVertex(int _v) {
        if(_v == u) {
            return v;
        } else {
            return u;
        }
    }

    /**
     * Return residual capacity towards node _v
     * @param _v we want residual capacity towards _v
     * @return residual capacity towards _v
     */
    public int getResidualCapacity(int _v) {
        if(_v == u) {
            // backward edge
            return f;
        } else {
            // forward edge
            return c - f;
        }
    }

    /**
     * Augment flow on edge towards _v
     * @param _v vertex towards which we want to augment flow
     * @param extraFlow quantity to augment flow by
     */
    public void augmentFlow(int _v, int extraFlow) {
        if(_v == v) {
            // forward edge
            f += extraFlow;
        } else {
            // backward edge
            f -= extraFlow;
        }
    }

    /**
     *
     * @return string representation of Flow Edge
     */
    public String toString() {
        return u+ "--->" + v + " (" + f + "/" + c +")";
    }
}
