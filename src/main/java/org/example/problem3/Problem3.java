package org.example.problem3;

import java.io.*;
import java.sql.PseudoColumnUsage;
import java.util.*;

public class Problem3 {

    private static String getAllLast(List<Vertex> path, boolean i)
    {
        StringBuilder sb =new StringBuilder();
        for (var p : path)
        {
            var pattern = i ? p.second : p.first;
            var last = pattern.charAt(pattern.length() - 1);
            sb.append(last);
        }
        return sb.toString();
    }

    private static void printResults(List<Vertex> resultedPath, int k, int d)
    {
        var subList = resultedPath.subList(1,resultedPath.size());

        var seq1 = resultedPath.get(0).first + getAllLast(subList, false);
        var seq2 = resultedPath.get(0).second + getAllLast(subList, true);
        var l = seq1.length();
        var sub1 = seq1.substring(k+d);
        var sub2 = seq2.substring(0, l-k-d);
        if (sub1.equals(sub2))
        {
            var r = seq2.substring(seq2.length() - (k+d));
            System.out.println(seq1 + r);
        }
    }

    public static void reconstructByFile(String filename)
    {
        File file = new File(filename);

        List<List<String>> sequences = new ArrayList<>();
        int k = -1;
        int d = -1;

        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<String> lines = new ArrayList<>();

            line = br.readLine();
            var splitLine = line.split("\\s+");
            k = Integer.parseInt(splitLine[0]);
            System.out.println(k);
            d = Integer.parseInt(splitLine[1]);

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            var iter = lines.iterator();
            String[][] patterns = new String[lines.size()][2];
            int i = 0;
            while (iter.hasNext())
            {
                line = iter.next();
                splitLine = line.split("\\|");
                sequences.add(new ArrayList<>());
                patterns[i][0] = splitLine[0];
                patterns[i++][1] = splitLine[1];
            }

            DeBruijnGraph graph = new DeBruijnGraph(k, patterns);
            EulerianPath.init(graph);
            EulerianPath.calculateEulerianPath();
            printResults(EulerianPath.path, k, d);

            inputStream.close();

        } catch (IOException exception)
        {

        } finally {

        }


    }

}
