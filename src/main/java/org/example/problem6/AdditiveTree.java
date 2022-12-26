package org.example.problem6;

import java.util.*;

public class AdditiveTree {

    public static class FromFuncData
    {
        public AdditiveTree tree;
        public int inner_n;

        public FromFuncData(AdditiveTree tree, int inner_n)
        {
            this.tree = tree;
            this.inner_n = inner_n;
        }
    }

    //from - to/weight
    public Map<Integer, Map<Integer, Integer>> edgeMap;

    public AdditiveTree()
    {
        edgeMap = new LinkedHashMap<>();
    }

    public void addEdge(int from, int to, int weight)
    {
        if (!edgeMap.containsKey(from))
            edgeMap.put(from, new LinkedHashMap<>());

        edgeMap.get(from).put(to, weight);
    }

    public void removeEdge(int from, int to)
    {
        edgeMap.get(from).remove(to);
    }

    public List<Integer> getAllTo(int from)
    {
        return new ArrayList<>(edgeMap.get(from).keySet());
    }

    public int getWeight(int from, int to)
    {
        return edgeMap.get(from).get(to);
    }

    public void printTree()
    {
        var keys = new ArrayList(edgeMap.keySet().stream().toList());
        Collections.sort(keys);
        for (var i : keys)
        {
            var toMap = edgeMap.get(i);
            var toKeys = new ArrayList(toMap.keySet().stream().toList());
            Collections.sort(toKeys);
            for (var j : toKeys)
            {
                System.out.println(i + "->" + j + ":" + toMap.get(j));
            }
        }
    }
}
