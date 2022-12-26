package org.example.problem8;

import java.io.*;
import java.util.*;

public class Viterbi {

    String outcome;
    Map<Character, Map<Character, Double>> transition;
    Map<Character, Map<Character, Double>> emission;

    public Viterbi(String filename)
    {
        transition = new LinkedHashMap<>();
        emission = new LinkedHashMap<>();

        readData(filename);
    }

    private void readData(String filename)
    {
        File file = new File(filename);

        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
            outcome = lines.get(0).split("\\s+")[0];

            inputStream.close();

            var list = lines.get(7).split("\\s+");
            transition.put('A', new LinkedHashMap<>());
            transition.get('A').put('A', Math.log(Float.parseFloat(list[1])));
            transition.get('A').put('B', Math.log(Float.parseFloat(list[2])));
            list = lines.get(8).split("\\s+");
            transition.put('B', new LinkedHashMap<>());
            transition.get('B').put('A', Math.log(Float.parseFloat(list[1])));
            transition.get('B').put('B', Math.log(Float.parseFloat(list[2])));

            list = lines.get(11).split("\\s+");
            emission.put('A', new LinkedHashMap<>());
            emission.get('A').put('x', Math.log(Float.parseFloat(list[1])));
            emission.get('A').put('y', Math.log(Float.parseFloat(list[2])));
            emission.get('A').put('z', Math.log(Float.parseFloat(list[3])));
            list = lines.get(12).split("\\s+");
            emission.put('B', new LinkedHashMap<>());
            emission.get('B').put('x', Math.log(Float.parseFloat(list[1])));
            emission.get('B').put('y', Math.log(Float.parseFloat(list[2])));
            emission.get('B').put('z', Math.log(Float.parseFloat(list[3])));
        } catch (IOException exception)
        {

        }
    }

    private static double maximum(double[] array) {
        if (array.length <= 0)
            throw new IllegalArgumentException("The array is empty");
        double max = array[0];
        for (int i = 1; i < array.length; i++)
            if (array[i] > max)
                max = array[i];
        return max;
    }

    public void probPath()
    {
        double[][] dp = new double[outcome.length()][2];
        double[] dpTmp = dp[dp.length - 1];
        for (int i = 0; i < outcome.length(); i++)
        {
            if (i - 1 >= 0)
                dpTmp = dp[i-1];

            dp[i][0] = max_state('A', outcome.charAt(i), dpTmp);
            dp[i][1] = max_state('B', outcome.charAt(i), dpTmp);
        }

        var dpMax = maximum(dp[outcome.length() - 1]);
        List<Character> hiddenPath = new ArrayList<>();
        if (dpMax == dp[outcome.length() - 1][0])
        {
            hiddenPath.add('A');
        } else
            hiddenPath.add('B');

        for (int i = outcome.length(); i > 1; i--)
        {
            var first = hiddenPath.get(0);
            var dA = dpMax - transition('A', first) - emission(first, outcome.charAt(i - 1));
            var dB = dpMax - transition('B', first) - emission(first, outcome.charAt(i - 1));

            if (compareFloat(dA, dp[i-2]))
            {
                hiddenPath.add(0, 'A');
                dpMax = dA;
            } else
            {
                hiddenPath.add(0, 'B');
                dpMax = dB;
            }

        }

        for (var h : hiddenPath)
            System.out.print(h);
    }

    private double max_state(char state, char outcome, double[] dp)
    {
        var dA = dp[0] + transition('A', state) + emission(state, outcome);
        var dB = dp[1] + transition('B', state) + emission(state, outcome);
        return Math.max(dA,dB);
    }


    private double transition(char prev_state, char state)
    {
        return transition.get(prev_state).get(state);
    }


    public double emission(char state, char outcome)
    {
        return emission.get(state).get(outcome);
    }

    private double roundFloat(double number, int precision)
    {
        var t = Math.pow(10, precision);
        return (Math.round(number * 100.0)) / 100.0;
    }

    private boolean compareFloat(double num, double[] numArr)
    {
        var roundedNum = roundFloat(num, 4);
        return Double.compare(roundedNum, roundFloat(numArr[0], 4)) == 0 ||
                Double.compare(roundedNum, roundFloat(numArr[1], 4)) == 0;
    }

}
