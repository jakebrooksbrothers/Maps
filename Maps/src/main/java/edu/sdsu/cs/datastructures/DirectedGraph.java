
//Jacob Brooks cssc0799

package edu.sdsu.cs.datastructures;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
public class DirectedGraph<V> implements IGraph<V> {
    private LinkedList<V> vertices;
    private LinkedList<Edge> edges;
    private Map<V, List<V>> neighbors;
    public DirectedGraph() {
        neighbors = new HashMap<V, List<V>>();
        vertices = new LinkedList<V>();
        edges = new LinkedList<Edge>();
    }
    public DirectedGraph(LinkedList<V> theVertices, LinkedList<Edge> theEdges, Map<V, List<V>> theMap) {
        vertices = theVertices;
        edges = theEdges;
        neighbors = theMap;
    }
    @Override
    public void add(V vertexName) {
        if (!vertices.contains(vertexName))
            vertices.add(vertexName);
    }
    @Override
    public void connect(V start, V destination) {
        boolean startExists = false;
        boolean destinationExists = false;
        for (V vertex : vertices) {
            if (start.equals(vertex))
                startExists = true;
            if (destination.equals(vertex))
                destinationExists = true;
        }
        if (startExists && destinationExists) {
            Edge e = new Edge(start, destination, 1);
            edges.add(e);
        } else
            throw new NoSuchElementException();
    }
    @Override
    public void clear() {
        vertices.clear();
    }
    @Override
    public boolean contains(V label) {
        return vertices.contains(label);
    }
    @Override
    public void disconnect(V start, V destination) {
        boolean startExists = false;
        boolean destinationExists = false;
        for (V vertex : vertices) {
            if (start.equals(vertex))
                startExists = true;
            if (destination.equals(vertex))
                destinationExists = true;
        }
        if (!(startExists && destinationExists))
            throw new NoSuchElementException();
        LinkedList<Edge> recycleBin = new LinkedList<Edge>();
        for (Edge e : edges) {
            if (e.source.equals(start) && e.destination.equals(destination)) {
                recycleBin.add(e);
            }
        }
        for (Edge e : recycleBin) {
            edges.remove(e);
        }
    }
    @Override
    public boolean isConnected(V start, V destination) {
        try {
            for (V neigh : neighbors(start)) {
                if (neigh.equals(destination))
                    return true;
                return isConnected(neigh, destination);
            }
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }
    @Override
    public Iterable<V> neighbors(V vertexName) {
        boolean startExists = false;
        for (V vertex : vertices) {
            if (vertexName.equals(vertex))
                startExists = true;
        }
        if (!startExists)
            throw new NoSuchElementException();
        LinkedList<V> vList = new LinkedList<V>();
        for (Edge theEdge : edges)
            if (theEdge.source.equals(vertexName))
                vList.add(theEdge.destination);
        return vList;
    }
    @Override
    public void remove(V vertexName) {
        boolean exists = false;
        for (V vert : vertices) {
            if (vertexName.equals(vert))
                exists = true;
        }
        if (!exists)
            throw new NoSuchElementException();
        vertices.remove(vertexName);
        int size = edges.size();
        for (int i = 0; i < size; i++) {
            if (edges.get(i).source.equals(vertexName) || edges.get(i).destination.equals(vertexName)) {
                edges.remove(i);
                size--;
            }
        }
    }

    public List<V> shortestPath(V start, V destination) {
        if (!contains(start) || !contains(destination))
            throw new NoSuchElementException();
        Map<V, Integer> distant = new HashMap<>();
        Map<V, V> previous = new HashMap<>();
        LinkedList<V> qq = new LinkedList<>();
        List<V> toReturn = new LinkedList<>();
        distant.put(start, 0);
        previous.put(start, null);
        qq.add(start);
        toReturn.add(start);
        Edge e = new Edge(start, destination, 1);
        V u;
        if (edges.contains(e)) {
            toReturn.add(destination);
            return toReturn;
        }
        while (!qq.isEmpty()) {
            u = qq.poll();
            if (u.equals(destination)) {
                List<V> newReturn = new LinkedList<>();
                newReturn.add(destination);
                V preV = previous.get(destination);
                while (preV != start)
                {
                    newReturn.add(0, preV);
                    preV = previous.get(preV);
                }
                newReturn.add(0, start);
                return newReturn;
            }
            for (V neighbor : neighbors(u)) {
                if (!distant.containsKey(neighbor)) {
                    distant.put(neighbor, distant.get(u) + 1);
                    previous.put(neighbor, u);
                    qq.add(neighbor);
                    toReturn.add(neighbor);
                }
            }
        }
        return null;
    }
    @Override
    public int size() {
        return vertices.size();
    }
    @Override
    public Iterable<V> vertices() {
        return vertices;
    }
    @Override
    public IGraph<V> connectedGraph(V origin) {
        boolean startExists = false;
        for (V vertex : vertices) {
            if (origin.equals(vertex))
                startExists = true;
        }
        if (!startExists)
            throw new NoSuchElementException();
        LinkedList<V> verts = new LinkedList<V>();
        LinkedList<Edge> edgy = new LinkedList<Edge>();
        verts.add(origin);
        Edge e;
        for (V theVert : vertices) {
            if (isConnected(origin, theVert) && !origin.equals(theVert)) {
                verts.add(theVert);
            }
            for (V v : neighbors(theVert)) {
                e = new Edge(theVert, v, 1);
                edgy.add(e);
            }
        }
        return new DirectedGraph<V>(verts, edgy, null);
    }
    public class Edge {
        V destination;
        V source;
        int weight;
        public Edge(V source, V destination, int weight) {
            this.destination = destination;
            this.source = source;
            this.weight = 1;
        }
    }
}
