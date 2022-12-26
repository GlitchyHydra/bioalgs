package org.example.problem6;

import org.example.problem2.Problem2;
import org.example.problem7.SuffixTree;

import java.io.*;
import java.util.*;

public class AdditivePhylogeny {

    //https://rosalind.info/problems/ba7c/

    public static int[][] readMatrixFromFile(String filename)
    {
        File file = new File(filename);

        List<String> lines = new ArrayList<>();
        int n = 0;
        int[][] matrix = new int[][]{};
        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            System.out.println(lines);
            n = Integer.parseInt(br.readLine());
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            inputStream.close();

            matrix = new int[n][n];
            for (int i = 0; i < n; i++)
            {
                var l = lines.get(i);
                var splitNumbers = l.split("\\s+");
                for (int j = 0; j < n; j++)
                {
                   matrix[i][j] = Integer.parseInt(splitNumbers[j]);
                }
            }

        } catch (IOException exception)
        {

        }

        return matrix;
    }

    private static int findMin(int[][] matrix, List<Integer> indices, int j, int k)
    {
        var min = Integer.MAX_VALUE;
        for (var i : indices)
        {
            var d = matrix[i][j] - matrix[i][k] + matrix[j][k];
            min = Math.min(min, d);
        }
        return min/2;
    }

    private static int findLimbLength(int[][] matrix, int j)
    {
        var limb = Integer.MAX_VALUE;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++)
        {
            if (i != j)
                indices.add(i);
        }

        for (int k = 0; k < matrix.length; k++)
        {
            if (k == j)
                continue;
            limb = Math.min(limb, findMin(matrix, indices, j, k));
        }
        return limb;
    }


    private static int[] find(int[][] matrix)
    {
        var lastIndex = matrix[0].length - 1;
        for (int k = 0; k < lastIndex; k++)
        {
            var tmpRow = new int[matrix.length];
            var lastRow = matrix[lastIndex];
            for (int j = 0; j < matrix[k].length; j++)
            {
                tmpRow[j] = matrix[k][j] - lastRow[j];
            }

            var lastEl = matrix[k][lastIndex];
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < tmpRow.length; i++)
            {
                if (tmpRow[i] == lastEl)
                    indices.add(i);
            }
            if (indices.size() > 0)
                return new int[] { indices.get(0), k };
        }

        return new int[]{};
    }

    private static int[] nearest(AdditiveTree tree, int x, int i, int k)
    {
        Deque<List<Integer>> queue = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();
        List<Integer> initArr = new ArrayList<>();
        initArr.add(i);
        queue.add(initArr);
        visited.add(i);
        List<Integer> findPath = new ArrayList<>();
        while (!queue.isEmpty())
        {
            var path = queue.pop();
            var node = path.get(path.size() - 1);
            visited.add(node);
            if (node == k)
            {
                findPath = path;
                break;
            }
            for (var next_node : tree.getAllTo(node))
            {
                if (!visited.contains(next_node))
                {
                    var toAdd = new ArrayList<>(path);
                    toAdd.add(next_node);
                    queue.add(toAdd);
                }
            }
        }

        var dist = 0;
        for (int t = 0; t < findPath.size() - 1; t++)
        {
            i = findPath.get(t);
            var j = findPath.get(t + 1);

            var w = tree.getWeight(i, j);
            if (dist + w > x)
            {
                var res = new int[]{i, j, x-dist, dist + w - x};
                return res;
            }
            dist += w;
        }
        return new int[]{};
    }

    private static AdditiveTree.FromFuncData additivePhylogeny(int[][] matrix, int n, int inner_n)
    {
        if (n == 2)
        {
            var additiveTree = new AdditiveTree();
            additiveTree.addEdge(0, 1, matrix[0][1]);
            additiveTree.addEdge(1, 0, matrix[0][1]);
            return new AdditiveTree.FromFuncData(additiveTree, inner_n);
        }

        var limbLength = findLimbLength(matrix, n-1);
        for (int j = 0; j < n - 1; j ++)
        {
            matrix[j][n - 1] -= limbLength;
            matrix[n - 1][j] -= limbLength;
        }

        int[] res = find(matrix);
        var i = res[0];
        var k = res[1];
        var x = matrix[i][matrix[i].length -1];
            //remove row n and column n from D
        var tmpMatrix = new int[matrix.length - 1][matrix.length - 1];
        for (int ind = 0; ind < tmpMatrix.length; ind++)
        {
            System.arraycopy(matrix[ind], 0, tmpMatrix[ind], 0, tmpMatrix.length);
        }
        var pair = additivePhylogeny(tmpMatrix, n-1, inner_n);
        var additiveTree = pair.tree;
        inner_n = pair.inner_n;

        res = nearest(additiveTree, x, i, k);

        int i_near = res[0];
        int k_near = res[1];
        int i_x = res[2];
        int n_x = res[3];
        var new_node = i_near;

        if (i_x != 0)
        {
            new_node = inner_n;
            inner_n += 1;
            additiveTree.removeEdge(i_near, k_near);
            additiveTree.removeEdge(k_near, i_near);

            additiveTree.addEdge(new_node, i_near, i_x);
            additiveTree.addEdge(i_near, new_node, i_x);
            additiveTree.addEdge(new_node, k_near, n_x);
            additiveTree.addEdge(k_near, new_node, n_x);
        }

        additiveTree.addEdge(new_node, n-1, limbLength);
        additiveTree.addEdge(n-1, new_node, limbLength);
        return new AdditiveTree.FromFuncData(additiveTree, inner_n);
    }

    public static void solve(String filename)
    {
        var  matrix = readMatrixFromFile(filename);
        int n = matrix.length;
        var tree = additivePhylogeny(matrix, n, n).tree;
        tree.printTree();
    }


}
