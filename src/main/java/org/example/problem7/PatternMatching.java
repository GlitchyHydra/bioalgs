package org.example.problem7;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatternMatching {

    private static String seq;
    private static List<String> patterns = new ArrayList<>();

    private static List<Integer> getIndices(List<String> occurrences)
    {
        List<Integer> list = new ArrayList<>();
        for (var occurrence : occurrences)
        {
            var array = occurrence.toCharArray();
            for (int i = 0; i < array.length; i++)
            {
                if (array[i] == '[')
                    list.add(i);
            }
        }
        return list;
    }

    private static void readFile(String filename)
    {
        File file = new File(filename);
        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));

            seq = br.readLine().trim();
            String line;
            while ((line = br.readLine()) != null) {
                patterns.add(line.trim());
            }

            inputStream.close();
        } catch (IOException exception)
        {

        }
    }

    public static void solve(String filename)
    {
        readFile(filename);
        var suffixTree = new SuffixTree(seq);
        List<Integer> indices = new ArrayList<>();
        for (var pattern : patterns)
        {
            var resStrings = suffixTree.searchText(pattern);
            var resIndices = getIndices(resStrings);

            for (var i : resIndices)
            {
                indices.add(i);
            }
        }

        Collections.sort(indices);
        for (var i : indices)
        {
            System.out.print(i + " ");
        }


        System.out.println();
    }
}
