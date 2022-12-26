package org.example.problem5;

import java.io.*;
import java.util.*;

public class ShortestTransformation {

    public static List<String> readFile(String filename)
    {
        File file = new File(filename);

        List<String> lines = new ArrayList<>();
        int[][] matrix = new int[][]{};
        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                lines.add(line);
            }

        } catch (IOException exception)
        {

        }
        return lines;
    }

    //https://youtu.be/naovHyR0eEg
    private static List<Integer> chromeToCycle(String chrome)
    {
        List<Integer> vertices = new ArrayList();
        StringBuilder temp = new StringBuilder();
        for (var s : chrome.toCharArray())
        {
            switch(s)
            {
                case '(':
                {
                    temp = new StringBuilder();
                    break;
                }
                case ')':
                {
                    var nums = temp.toString().split("\\s+");
                    for (int i = 0; i < nums.length; i++)
                    {
                        var n = Integer.parseInt(nums[i]);
                        int head, tail;
                        if (n > 0)
                        {
                            head = n*2-1;
                            tail = n*2;
                        } else
                        {
                            head = -n*2;
                            tail = -n*2-1;
                        }
                        vertices.add(head);
                        vertices.add(tail);
                    }
                    break;
                }
                default:
                {
                    temp.append(s);
                    break;
                }
            }
        }
        return vertices;
    }

    private static List<Integer> cycletoChrome(List<Integer> vertices)
    {
        List<Integer> chrome = new ArrayList<>();
        for (int i = 0; i < vertices.size() / 2; i++)
        {
            if (vertices.get(2*i) < vertices.get(2*i+1))
                chrome.add(vertices.get(2*i+1)/2);
            else
                chrome.add(-vertices.get(2*i)/2);
        }
        return chrome;
    }

    private static String graphToGenome(Graph graph)
    {
        var path = new StringBuilder("");
        var cycles = graph.findCycle();
        for (var cycle : cycles)
        {
            var vertices = cycle;
            var chrome = cycletoChrome(vertices);

            path.append('(');
            var temp = new StringBuilder();
            for (var n : chrome)
            {
                if (n > 0)
                {
                    temp.append('+');
                    temp.append(n);
                    temp.append(" ");
                }
                else
                {
                    temp.append(n);
                    temp.append(" ");
                }
            }
            path.append(temp.toString().trim());
            path.append(')');
        }
        return path.toString();
    }

    private static Map<Integer, Integer> colorEdges(String chromeS)
    {
        Map<Integer, Integer> edges = new LinkedHashMap<>();
        StringBuilder temp = new StringBuilder();
        for (var s : chromeS.toCharArray())
        {
            switch(s)
            {
                case '(':
                {
                    temp = new StringBuilder("(");
                    break;
                }
                case ')':
                {
                    temp.append(')');
                    var vertices = chromeToCycle(temp.toString());
                    for(int i = 0; i < vertices.size() / 2; i++)
                    {
                        var n1 = vertices.get(2 * i + 1);
                        var n2 = vertices.get((2*i+2) % vertices.size());
                        edges.put(n1, n2);
                        edges.put(n2, n1);
                    }
                    break;
                }
                default:
                {
                    temp.append(s);
                    break;
                }
            }
        }
        return edges;
    }

    private static Map<Integer, Integer> blackEdges(String chromeS)
    {
        Map<Integer, Integer> edges = new LinkedHashMap<>();
        StringBuilder temp = new StringBuilder();
        for (var s : chromeS.toCharArray())
        {
            switch(s)
            {
                case '(':
                {
                    temp.append('(');
                    break;
                }
                case ')':
                {
                    temp.append(')');
                    var vertices = chromeToCycle(temp.toString());
                    for(int i = 0; i < vertices.size() / 2; i++)
                    {
                        var n1 = vertices.get(2 * i);
                        var n2 = vertices.get(2*i+1);
                        edges.put(n1, n2);
                        edges.put(n2, n1);
                    }
                    break;
                }
                default:
                {
                    temp.append(s);
                    break;
                }
            }
        }
        return edges;
    }

    private static Graph graphEdges(String chromeS)
    {
        var black_edges = blackEdges(chromeS);
        var colorEdges = colorEdges(chromeS);
        return new Graph(black_edges, colorEdges);
    }

    private static String twoBreakOnGenome(String P, int a, int b, int c, int d)
    {
        var graph = graphEdges(P);
        graph.twoBreakOnGraph(graph.BlueEdges, a, b, c, d);
        return graphToGenome(graph);
    }

    private static void shortestRearrangement(String P, String Q)
    {
        System.out.println(P);
        var redEdges = colorEdges(P);
        var blueEdges = colorEdges(Q);
        var graph = new Graph(redEdges, blueEdges);
        var cycle = graph.findNonTrivialCycle();

        while (cycle != null)
        {
            var a = cycle[0];
            var b = cycle[1];
            var c = cycle[2];
            var d = cycle[3];
            P = twoBreakOnGenome(P, a, b, c, d);

            redEdges = colorEdges(P);
            graph = new Graph(redEdges, graph.BlueEdges);

            System.out.println(P);
            cycle = graph.findNonTrivialCycle();
        }
    }

    public static void solve(String filename)
    {
        var lines = readFile(filename);
        shortestRearrangement(lines.get(0), lines.get(1));
    }

}
