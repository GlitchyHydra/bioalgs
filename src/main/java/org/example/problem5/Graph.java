package org.example.problem5;

import java.util.*;

public class Graph {

    public final Map<Integer, Integer> RedEdges;
    public final Map<Integer, Integer> BlueEdges;

    public Graph(Map<Integer, Integer> redEdges, Map<Integer, Integer> blueEdges)
    {
        RedEdges = redEdges;
        BlueEdges = blueEdges;
    }

    private Stack<Integer> GetVerticesQueue(Map<Integer, Integer> edges)
    {
        var keys = new ArrayList(edges.keySet().stream().toList());
        Collections.sort(keys);
        Collections.reverse(keys);
        Stack<Integer> vertices = new Stack<>();
        vertices.addAll(keys);
        return vertices;
    }

    public List<List<Integer>> findCycle()
    {
        var blackEdges = this.RedEdges;
        var colorEdges = this.BlueEdges;
        var blackTurn = 0;
        var colorTurn = 1;

        List<List<Integer>> cycles = new ArrayList<>();
        Set<Integer> visited =new LinkedHashSet<>();
        var vertices = GetVerticesQueue(blackEdges);

        while (!vertices.isEmpty())
        {
            var vertex = vertices.pop();

            if (visited.contains(vertex))
                continue;

            List<Integer> path = new ArrayList<>();
            var turn = blackTurn;
            while (!visited.contains(vertex))
            {
                visited.add(vertex);
                path.add(vertex);
                if (turn == blackTurn)
                {
                    vertex = blackEdges.get(vertex);
                    turn = colorTurn;
                } else
                {
                    vertex = colorEdges.get(vertex);
                    turn = blackTurn;
                }
            }
            cycles.add(path);
        }
        return cycles;
    }

    public int[] findNonTrivialCycle()
    {
        var redTurn = 0;
        var blueTurn = 1;

        Set<Integer> visited = new LinkedHashSet<>();
        var vertices = GetVerticesQueue(BlueEdges);

        while (!vertices.isEmpty())
        {
            var root = vertices.pop();

            var vertex = root;
            if (visited.contains(vertex))
                continue;

            List<Integer> path = new ArrayList<>();
            var turn = blueTurn;
            while (!visited.contains(vertex))
            {
                visited.add(vertex);
                path.add(vertex);
                if (turn == blueTurn)
                {
                    vertex = BlueEdges.get(vertex);
                    turn = redTurn;
                } else
                {
                    vertex = RedEdges.get(vertex);
                    turn = blueTurn;
                }
            }
            if (path.size() > 2)
            {
                var nextRoot = BlueEdges.get(root);
                return new int[] {
                        root, RedEdges.get(root), nextRoot, RedEdges.get(nextRoot)
                };
            }
        }
        return null;
    }

    public void twoBreakOnGraph(Map<Integer, Integer> edges, int i1, int i2, int i3, int i4)
    {
        edges.remove(i1);
        edges.remove(i2);
        edges.remove(i3);
        edges.remove(i4);

        edges.put(i1, i3);
        edges.put(i3, i1);
        edges.put(i2, i4);
        edges.put(i4, i2);
    }

    public void printBlue()
    {
        for (var entry: BlueEdges.entrySet()) {
            System.out.print(entry.getKey() + ": "+ entry.getValue() + ", ");
        }
        System.out.println();
    }

    public void printRed()
    {
        for (var entry: RedEdges.entrySet()) {
            System.out.print(entry.getKey() + ": "+ entry.getValue() + ", ");
        }
        System.out.println();
    }
}
