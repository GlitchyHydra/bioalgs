package org.example.problem3;

import java.util.*;

public class EulerianPath {

    static int n;
    static int numberToExplore;
    static Map<Vertex, Integer> inDeg;
    static Map<Vertex, Integer> outDeg;
    static Map<Vertex, Integer> adjCurPos;

    static DeBruijnGraph graph;

    static Map<Vertex, Integer> nodesWUE;
    static List<Vertex> path;
    static List<Vertex> unbalancedNode;

    public static void init(DeBruijnGraph graph1)
    {
        graph = graph1;
        n = graph.getEdgesLength();
        numberToExplore = 0;
        nodesWUE = new LinkedHashMap<>();
        inDeg = new LinkedHashMap<>();
        outDeg = new LinkedHashMap<>();
        adjCurPos = new LinkedHashMap<>();
        path = new ArrayList<>();
        unbalancedNode = new ArrayList<>();
        for (var entry : graph.getEdges().entrySet())
        {
            var from = entry.getKey();
            var toList = entry.getValue();

            inDeg.put(from, inDeg.getOrDefault(from, 0));
            for (var toVertex : toList)
            {
                inDeg.put(toVertex, inDeg.getOrDefault(toVertex, 0) + 1);
            }
            var l = toList.size();
            outDeg.put(from, l);
            numberToExplore += l;
            adjCurPos.put(from, 0);
        }
        int a =3;
    }

    private static void addEdge()
    {
        var vertices = graph.getVertices();
        for (var v : vertices)
        {
            if (!inDeg.get(v).equals(outDeg.get(v)))
            {
                if (inDeg.get(v) < outDeg.get(v))
                    unbalancedNode.add(v);
                else
                    unbalancedNode.add(0, v);
            }
        }
        if (unbalancedNode.size() > 0)
        {
            graph.addEdge(unbalancedNode.get(0), unbalancedNode.get(1));

            var oVal = outDeg.get(unbalancedNode.get(0));
            outDeg.put(unbalancedNode.get(0), oVal + 1);

            var iVal = inDeg.get(unbalancedNode.get(1));
            inDeg.put(unbalancedNode.get(1), iVal + 1);
        }
    }

    private static void explore(Vertex from)
    {
        path.add(from);
        var currentPos = adjCurPos.get(from);
        var currentMaxPos = outDeg.get(from);
        while (currentPos < currentMaxPos)
        {
            adjCurPos.put(from, currentPos + 1);
            if (currentPos + 1 < currentMaxPos)
                nodesWUE.put(from, path.size() - 1);
            else
            {
                nodesWUE.remove(from);
            }
            var v = graph.getToByIndex(from, currentPos);
            path.add(v);
            from = v;
            currentPos = adjCurPos.get(from);
            currentMaxPos = outDeg.get(from);
            numberToExplore--;
        }
    }

    private static void updatePath(int startPos)
    {
        var l = path.size() - 1;
        var tempPath = path;
        path = new ArrayList<>();
        path.addAll(tempPath.subList(startPos, l));
        path.addAll(tempPath.subList(0, startPos));

        for (var entry : nodesWUE.entrySet())
        {
            var v = entry.getKey();
            var pos = entry.getValue();
            if (pos < startPos)
                nodesWUE.put(v, pos + l - startPos);
            else
                nodesWUE.put(v, pos - startPos);
        }
    }

    private static void calculateEulerianCycle()
    {
        var entry = graph.getFirstEdges();
        var from = entry.getKey();
        var to = entry.getValue();
        explore(from);
        while (numberToExplore > 0)
        {
            var firstEntry = nodesWUE.entrySet().iterator().next();
            nodesWUE.remove(firstEntry.getKey());
            var v = firstEntry.getKey();
            var pos = firstEntry.getValue();
            updatePath(pos);
            explore(v);
        }
    }

    public static void calculateEulerianPath()
    {
        addEdge();
        calculateEulerianCycle();
        if (unbalancedNode.size() > 0)
        {
            for (int i = 0; i < path.size() - 1; i++)
            {
                if (path.get(i) == unbalancedNode.get(0) &&
                path.get(i+1) == unbalancedNode.get(1))
                {
                    updatePath(i+1);
                    break;
                }
            }
        }
    }
}
