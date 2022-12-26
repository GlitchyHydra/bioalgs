package org.example.problem1;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class MedianString {

    private class BurrowsWheelerTransform
    {
        int[] endIndices;

        String first;
        String last;

        private String constructStringByIndex(int index, String[] suffixes)
        {
            StringBuilder sb = new StringBuilder();
            for (var s : suffixes)
                sb.append(s.charAt(index));
            return sb.toString();
        }

        private int findByIndex(String str, String[] arr)
        {
            for (int i = 0; i < arr.length; i++)
            {
                if (arr[i].equals(str))
                    return i;
            }
            return -1;
        }

        private BurrowsWheelerTransform(String pattern)
        {
            String t = pattern + "$";
            int length = t.length();
            endIndices = IntStream.range(0, length).toArray();

            String[] suffixes = new String[length];
            for (int i = 0; i < length; i ++)
            {
                suffixes[i] = t.substring(i) + t.substring(0, i);
            }
            var sortedSuffixes = new String[length];
            System.arraycopy(suffixes, 0, sortedSuffixes, 0, length);
            Arrays.sort(sortedSuffixes);

            last = constructStringByIndex(t.length() - 1, sortedSuffixes);
            first = constructStringByIndex(0, sortedSuffixes);
            for (int i = 0; i < length; i++)
            {
                var s = sortedSuffixes[i].charAt(length - 1) + sortedSuffixes[i].substring(0, length - 1);
                endIndices[i] = findByIndex(s, sortedSuffixes);
            }
        }

        private int SearchKmer(String kmer)
        {
            var d = 0;
            var reversedKmer = new StringBuilder(kmer).reverse().toString();
            var top = 0;
            var bot = last.length() - 1;
            for (var c : reversedKmer.toCharArray())
            {
                List<Integer> list = new ArrayList<>();
                for (int i = top; i <= bot; i++)
                {
                    if (last.charAt(i) == c)
                        list.add(endIndices[i]);
                }
                Collections.sort(list);
                if (list.isEmpty())
                {
                    d++;
                    continue;
                }

                top = list.get(0);
                bot = list.get(list.size() - 1);
            }

            return d;
        }
    }

    private static List<String> FindAllKmersForSequence(int k, String sequence)
    {
        List<String> kmers = new ArrayList<>();
        for (int i = 0; i < sequence.length(); i++)
        {
            if (i + k - 1 < sequence.length())
                kmers.add(sequence.substring(i, i + k));
        }
        return kmers;
    }

    public class InputData
    {
        private final String[] sequences;
        private final int k;

        public InputData(String[] sequences, int k)
        {
            this.sequences = sequences;
            this.k = k;
        }

        public String[] getSequences() {
            return sequences;
        }

        public int getK() {
            return k;
        }
    }

    public InputData ReadInputData(String filename)
    {
        File file = new File(filename);

        List<String> sequences = new ArrayList<>();
        int k = -1;
        try (InputStream inputStream = new FileInputStream(file);) {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            k = Integer.parseInt(br.readLine());

            while ((line = br.readLine()) != null) {
                sequences.add(line);
            }
            inputStream.close();

        } catch (IOException exception)
        {

        } finally {

        }
        var outSequences = sequences.toArray(new String[sequences.size()]);
        return new InputData(outSequences, k);
    }

    public String FindMedianString(int k, String[] sequences)
    {
        BurrowsWheelerTransform[] transforms = new BurrowsWheelerTransform[sequences.length];
        for (int i = 0; i < sequences.length; i++)
            transforms[i] = new BurrowsWheelerTransform(sequences[i]);

        List<String> allKmers = new ArrayList<>();
        for (var seq : sequences)
        {
            allKmers.addAll(FindAllKmersForSequence(k, seq));
        }

        List<Integer> allDistances = new ArrayList<>(allKmers.size());
        for (var kmer : allKmers)
        {
            var d = 0;
            for (var t : transforms)
            {
                var di = t.SearchKmer(kmer);
                d += di;
            }
            allDistances.add(d);
        }
        var minDistance = Collections.min(allDistances);
        var minIndex = allDistances.indexOf(minDistance);
        return allKmers.get(minIndex);
    }
}
