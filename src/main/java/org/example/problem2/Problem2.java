package org.example.problem2;

import java.io.*;
import java.util.*;

public class Problem2 {

    static Random random = new Random();

    private static void printDoubleCharArray(char[][] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            System.out.print("[");
            for (int j = 0; j < array[i].length; j++)
            {
                System.out.print(array[i][j] + " ");
            }
            System.out.print("]");
            System.out.println();
        }
        System.out.println();
    }

    private static char[][] stringArrToDoubleChar(List<String> strs)
    {
        char[][] arrays = new char[strs.size()][];
        for (int j = 0; j < strs.size(); j++)
        {
            arrays[j] = strs.get(j).toCharArray();
        }
        return arrays;
    }

    private static Map<Character, Integer> calculateOccurrences(char[][] arrays, int i)
    {
        Map<Character, Integer> occurrences = new HashMap<>();
        for (int j = 0; j < arrays.length; j++)
        {
            var symbol = arrays[j][i];
            if (occurrences.containsKey(symbol))
                occurrences.put(symbol, occurrences.get(symbol) + 1);
            else
                occurrences.put(symbol, 1);
        }
        return occurrences;
    }

    private static List<String> randomKmerSelection(int k, int t, String[] sequences) {
        var l = sequences[0].length();
        List<String> kmers = new ArrayList<>();

        for (var seq : sequences)
        {
            var n = random.nextInt(l-k);
            kmers.add(seq.substring(n, n+k));
        }
        return kmers;
    }

    private static float[][] Profile(List<String> motifs, int k, int t)
    {
        var arrays = stringArrToDoubleChar(motifs);
        //printDoubleCharArray(arrays);

        var pro = new float[4][k];//np.ones(shape=(4,k));

        for (int i = 0; i < pro.length; i++)
        {
            Arrays.fill(pro[i], 1);
        }

        for (int i = 0; i < k; i++)
        {
            Map<Character, Integer> occurrences = calculateOccurrences(arrays, i);
            pro[0][i] += occurrences.getOrDefault('A', 0);
            pro[1][i] += occurrences.getOrDefault('C', 0);
            pro[2][i] += occurrences.getOrDefault('G', 0);
            pro[3][i] += occurrences.getOrDefault('T', 0);
        }
        for (var p : pro)
        {
            for (int j = 0; j < p.length; j++) {
                p[j] = p[j]/ ((float)t + 1.0f);
            }
        }
        return pro;
    }

    private static <K, V extends Comparable<V>> V maxUsingCollectionsMaxAndLambda(Map<K, V> map) {
        Map.Entry<K, V> maxEntry = Collections.max(map.entrySet(), (Map.Entry<K, V> e1, Map.Entry<K, V> e2) -> e1.getValue()
                .compareTo(e2.getValue()));
        return maxEntry.getValue();
    }

    private static int Score(List<String> motifs, int k, int t)
    {
        char[][] arrays = stringArrToDoubleChar(motifs);
        var i = 0;
        var score = 0;
        while (i < k)
        {
            Map<Character, Integer> occurrences = calculateOccurrences(arrays, i);
            score += t - maxUsingCollectionsMaxAndLambda(occurrences);
            i++;
        }
        return score;
    }

    private static int findByChar(char symbol, char[] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] == symbol)
                return i;
        }
        return -1;
    }

    private static float compute(String kmer, char[] order, float[][] profile)
    {
        List<Float> c = new ArrayList<>();
        for (int i = 0; i < kmer.length(); i++)
        {
            var j = findByChar(kmer.charAt(i), order);
            c.add(profile[j][i]);
        }
        float res = 1.0f;
        for (var el : c)
        {
            res = res * el;
        }
        return res;
    }


    private static Set<String> correct(String seq, int k)
    {
        var outSet = new LinkedHashSet<String>();
        var length = seq.length() - k + 1;
        for (int i = 0; i < length; i++)
        {
            outSet.add(seq.substring(i, i + k));
        }
        return outSet;
    }


    private static String pmpkp(String sequence, int k, char[] order, float[][] profile)
    {
        var corrects = correct(sequence, k);
        Map<String, Float> map = new HashMap<>();
        for (var kmer : corrects)
        {
            map.put(kmer, compute(kmer, order, profile));
        }

        Float maxValue = Float.MIN_VALUE;
        String maxStr = "";
        for (var entry : map.entrySet())
        {
            var key = entry.getKey();
            var value = entry.getValue();
            if (value > maxValue)
            {
                maxStr = key;
                maxValue = value;
            }
        }
        return maxStr;
    }

    private static List<String> Motifs(float[][] profile, int k, int t, String[] sequences)
    {
        var order = new char[]{'A', 'C', 'G', 'T'};
        List<String> motifs = new ArrayList<>(sequences.length);
        for (var seq : sequences)
        {
            motifs.add(pmpkp(seq,k,order,profile));
        }
        return motifs;
    }

    public static List<String> randomizedMotifSearch(String[] sequences, int k, int t)
    {
        var bestmotifs = randomKmerSelection(k, t, sequences);
        var initialMotifs = randomKmerSelection(k, t, sequences);
        while (true)
        {
            var profile = Profile(initialMotifs, k, t);
            var motifs = Motifs(profile, k, t, sequences);
            if (Score(motifs,k,t) < Score(bestmotifs,k,t))
            {
                bestmotifs = motifs;
                initialMotifs = motifs;
            }
            else
            {
                return bestmotifs;
            }
        }
    }

    public static class InputData
    {
        private String[] sequences;
        private int k;
        private int t;

        private InputData(int k, int t, String[] sequences)
        {
            this.sequences = sequences;
            this.k = k;
            this.t = t;
        }

        public String[] getSequences() {
            return sequences;
        }

        public int getT() {
            return t;
        }

        public int getK() {
            return k;
        }
    }

    private static InputData getInputData(String filename)
    {
        File file = new File(filename);

        List<String> sequences = new ArrayList<>();
        int k = -1;
        int t = -1;

        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            line = br.readLine();
            var splitLine = line.split(" ");
            k = Integer.parseInt(splitLine[0]);
            System.out.println(k);
            t = Integer.parseInt(splitLine[1]);
            while ((line = br.readLine()) != null) {
                sequences.add(line);
            }
            inputStream.close();

        } catch (IOException exception)
        {

        } finally {

        }
        var outSequences = sequences.toArray(new String[sequences.size()]);
        return new InputData(k, t, outSequences);
    }

    public static List<String> getResultByFile(String filename)
    {
        var input = getInputData(filename);
        var k = input.k;
        var t = input.t;
        var seqs = input.sequences;
        var lowest_bestscore = Float.MAX_VALUE;
        List<String> lowest_motifs = new ArrayList();
        lowest_motifs.add("");
        int i = 0;
        while (true)
        {
            var bestmotifs = randomizedMotifSearch(seqs, k, t);
            var bestscore = Score(bestmotifs, k, t);
            if (bestscore < lowest_bestscore)
            {
                lowest_bestscore = bestscore;
                lowest_motifs = bestmotifs;
                i = 0;
            }
            else
            {
                i += 1;

            }
            if (i > 900)
                break;
        }

        for (var l : lowest_motifs)
        {
            System.out.println(l);
        }

        write("results/problem2/output.txt", lowest_motifs);

        return lowest_motifs;
    }

    private static void write (String filename, List<String> x){

        try (BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename)))
        {
            for (int i = 0; i < x.size(); i++) {
                outputWriter.write(x.get(i));
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException exception)
        {

        }

    }
}
