package org.example.problem8;

import java.io.*;
import java.util.*;

public class BaumWelch {

    static int numberOfIteration;
    static String x;
    static List<Character> alphabet;
    static List<Character> states;


    static Map<Character, Map<Character, Double>> transition;
    static Map<Character, Map<Character, Double>> emission;

    static double[][] prob1;
    static double[][][] prob2;

    private static void appendToMap(Map<Character, Map<Character, Double>> map,
                                    String[] row, String[] rowNames, int offset)
    {
        var currentName = row[0].charAt(0);
        for (int i = 1; i < row.length; i++)
        {
            var rowName = rowNames[i - offset].charAt(0);
            var rowVal = Double.parseDouble(row[i]);

            if (!map.containsKey(currentName))
                map.put(currentName, new LinkedHashMap<>());

            map.get(currentName).put(rowName, rowVal);
        }
    }

    private static void readInputData(String filename)
    {
        File file = new File(filename);

        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            List<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            numberOfIteration = Integer.parseInt(lines.get(0));
            x = lines.get(2);
            alphabet = new ArrayList<>();
            for (var a : lines.get(4).split("\\s+"))
            {
                alphabet.add(a.toCharArray()[0]);
            }
            states = new ArrayList<>();
            for (var a : lines.get(6).split("\\s+"))
            {
                states.add(a.toCharArray()[0]);
            }

            transition = new LinkedHashMap<>();
            var rowNames = lines.get(8).split("\\s+");

            int i = 9;
            while (!lines.get(i).equals("--------"))
            {
                var row = lines.get(i).split("\\s+");
                appendToMap(transition, row, rowNames, 0);
                i++;
            }

            emission = new LinkedHashMap<>();
            var offset = 1;
            rowNames = lines.get(i + 1).split("\\s+");
            if (Arrays.stream(rowNames).toList().contains(""))
                offset = 0;

            for (i = i + 2; i < lines.size(); i++)
            {
                var row = lines.get(i).split("\\s+");
                appendToMap(emission, row, rowNames, offset);
            }

            var t = 3;
        } catch (IOException exception)
        {

        }
    }

    private static void printResult()
    {
        System.out.print(" ");
        for (int i = 0; i < states.size(); i++)
        {
            System.out.print(" ");
            System.out.print(states.get(i));
        }
        System.out.println();
        for (var entry : transition.entrySet())
        {
            var state = entry.getKey();
            var traverseMap = entry.getValue();
            System.out.print(state);
            System.out.print(" ");

            int i = 0;
            int size = traverseMap.entrySet().size();
            for (var entry2 : traverseMap.entrySet())
            {
                var e = entry2.getValue();
                System.out.printf("%.3f", e);

                if (i < size - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }

        System.out.println("--------");
        System.out.print(" ");
        for (Character character : alphabet) {
            System.out.print(" ");
            System.out.print(character);
        }
        System.out.println();
        for (var entry : emission.entrySet())
        {
            var state = entry.getKey();
            var traverseMap = entry.getValue();
            System.out.print(state);
            System.out.print(" ");

            int i = 0;
            int size = traverseMap.entrySet().size();
            for (var entry2 : traverseMap.entrySet())
            {
                var e = entry2.getValue();
                System.out.printf("%.3f", e);

                if (i < size - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }

    private static double[][] generate2D(int n, int l)
    {
        double array[][] = new double[n][l];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < l; j ++)
            {
                array[i][j] = 0.0;
            }
        }
        return array;
    }

    private static double[][][] generate3D(int n, int l, int m)
    {
        double array[][][] = new double[n][l][m];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < l; j ++)
            {
                for (int k = 0; k < m; k++)
                    array[i][j][k] = 0.0;
            }
        }
        return array;
    }

    private static void decode()
    {
        var n = x.length();
        var l = transition.entrySet().size();

        var forward = generate2D(n, l);
        var backward = generate2D(n, l);

        int i = 0;
        for (var state : emission.entrySet())
        {
            var innerMap = state.getValue();
            forward[0][i++] = innerMap.get(x.charAt(0)) / l;
        }

        var transStates = new ArrayList<>(transition.entrySet());
        var emisStates = new ArrayList<>(emission.entrySet());
        for (i = 1; i < n; i++)
        {
            for (int j = 0; j < l; j++)
            {
                double sum = 0.0;
                for (int k = 0; k < l; k++)
                {
                    var transVal = transStates.get(k).getValue().get(states.get(j));
                    var emissionVal = emisStates.get(j).getValue().get(x.charAt(i));
                    sum += forward[i-1][k] * transVal * emissionVal;
                }
                forward[i][j] = sum;
            }
        }
        double fsink = 0.0;
        for (i = 0; i < forward[n-1].length; i++)
            fsink += forward[n-1][i];

        for (i = 0; i < l; i++)
            backward[n-1][i] = 1;
        for (i = n - 2; i > -1; i--)
        {
            for (int j = 0; j < l; j++)
            {
                double sum = 0.0;
                for (int k = 0; k < l; k++)
                {
                    var transVal = transStates.get(j).getValue().get(states.get(k));
                    var emissionVal = emisStates.get(k).getValue().get(x.charAt(i+1));
                    sum += backward[i+1][k] * transVal * emissionVal;
                }
                backward[i][j] = sum;
            }
        }

        prob1 = generate2D(l, n);
        for (i = 0; i < n; i++)
        {
            for (int j = 0; j < l; j++)
                prob1[j][i] = forward[i][j] * backward[i][j] / fsink;
        }

        prob2 = generate3D(l, l, n - 1);
        for (i = 0; i < l; i++)
        {
            for (int j = 0; j < l; j++)
            {
                for (int k = 0; k < n - 1; k++)
                {
                    var transVal = transStates.get(i).getValue().get(states.get(j));
                    var emissionVal = emisStates.get(j).getValue().get(x.charAt(k+1));
                    prob2[i][j][k] = forward[k][i] * transVal * emissionVal * backward[k+1][j] / fsink;
                }
            }
        }

    }

    private static double getSum(Character state, Map<Character, Map<Character, Double>> toSumMap)
    {
        var map = toSumMap.get(state);
        double sum = 0.0;
        for (var s : map.entrySet())
        {
            sum += s.getValue();
        }
        return sum;
    }

    private static void sumMap(Map<Character, Double> map, double val)
    {
        for (var e : map.entrySet())
        {
            e.setValue(e.getValue() + val);
        }
    }

    private static void mulMap(Map<Character, Double> map, double val)
    {
        for (var e : map.entrySet())
        {
            e.setValue(e.getValue() * val);
        }
    }

    private static void estimateParams()
    {
        var n = x.length();
        var l = prob2.length;

        transition = new LinkedHashMap<>();
        emission = new LinkedHashMap<>();

        for (int k1 = 0; k1 < l; k1++)
        {
            for (int k2 = 0; k2 < l; k2++)
            {
                var first = states.get(k1);
                var second = states.get(k2);

                if (!transition.containsKey(first))
                    transition.put(first, new LinkedHashMap<>());

                double sum = 0.0;
                int length = prob2[k1][k2].length;
                for (int k = 0; k < length; k++)
                    sum += prob2[k1][k2][k];

                transition.get(first).put(second, sum);
            }
        }

        for (int k = 0; k < l; k++)
        {
            var a = states.get(k);
            if (!emission.containsKey(a))
                emission.put(a, new HashMap<>());
            var innerMap = emission.get(a);

            for (int i = 0; i < n; i++)
            {
                var a2 = x.charAt(i);
                if (!innerMap.containsKey(a2))
                    innerMap.put(a2, prob1[k][i]);
                else
                {
                    var current = innerMap.get(a2);
                    innerMap.put(a2, current + prob1[k][i]);
                }

            }
        }

        for (int i = 0; i < l; i++)
        {
            var sum1 = getSum(states.get(i), transition);
            if (sum1 <= 0.00001f && sum1 >= -0.00001f)
            {
                sumMap(transition.get(states.get(i)), 1.f/(double)l);
            } else
            {
                mulMap(transition.get(states.get(i)), 1.f/sum1);
            }

            var sum2 = getSum(states.get(i), emission);
            if (sum2 <= 0.00001f && sum2 >= -0.00001f)
            {
                sumMap(emission.get(states.get(i)), 1.f/(double)alphabet.size());
            } else
            {
                mulMap(emission.get(states.get(i)), 1.f/sum2);
            }
        }
    }



    public static void solve(String filename)
    {
        readInputData(filename);
        for (int i = 0; i < numberOfIteration; i++)
        {
            decode();
            estimateParams();
        }
        printResult();
    }

}
