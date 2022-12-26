package org.example.problem3;

import java.util.*;

public class DeBruijnGraph {

    Map<Vertex, List<Vertex>> edges;

    public DeBruijnGraph(int k, String[][] patternPairs)
    {
        edges = new LinkedHashMap<>();
        for (var patternPair : patternPairs)
        {
            var firstPattern = patternPair[0];
            var secondPattern = patternPair[1];
            var p1 = new Vertex(firstPattern.substring(0, k-1),
                     secondPattern.substring(0, k-1));
            var p2 = new Vertex(firstPattern.substring(1),
                    secondPattern.substring(1));
            if (edges.containsKey(p1))
            {
                edges.get(p1).add(p2);
            }
            else
            {
                edges.put(p1, new ArrayList<>());
                edges.get(p1).add(p2);
            }

            if (!edges.containsKey(p2))
                edges.put(p2, new ArrayList<>());
        }
    }

    public Map.Entry<Vertex, List<Vertex>> getFirstEdges()
    {
        var entry = edges.entrySet().iterator().next();
        edges.remove(entry.getKey());
        edges.put(entry.getKey(), entry.getValue());
        return entry;
    }

    public Map<Vertex, List<Vertex>> getEdges()
    {
        return edges;
    }

    public Vertex getToByIndex(Vertex from, int i)
    {
        return edges.get(from).get(i);
    }

    public void addEdge(Vertex from, Vertex to)
    {
        edges.get(from).add(to);
    }
    public Set<Vertex> getVertices()
    {
        return edges.keySet();
    }

    public int getEdgesLength()
    {
        return edges.size();
    }


}
