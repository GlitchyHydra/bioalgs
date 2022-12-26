package org.example;


import org.example.problem2.Problem2;
import org.example.problem3.Problem3;
import org.example.problem4.AffineGapPenalties;
import org.example.problem5.ShortestTransformation;
import org.example.problem6.AdditivePhylogeny;
import org.example.problem6.AdditiveTree;
import org.example.problem7.LongestRepeatString;
import org.example.problem7.PatternMatching;
import org.example.problem8.BaumWelch;
import org.example.problem8.Viterbi;

public class Main {

    public static void main(String[] args) {
        /* Problem 1
        * var med = new MedianString();

        String[] sequences = null;
        var inputData = med.ReadInputData("rosalind_ba2b.txt");

        var res =med.FindMedianString(inputData.getK(), inputData.getSequences());
        System.out.println(res);
        * */

        /*-----------------Problem 2-----------------*/
        //Problem2.getResultByFile("input/problem2/rosalind_ba2f (1).txt");

        /*-----------------Problem 3-----------------*/
        //Problem3.reconstructByFile("input/problem3/rosalind_ba3j.txt");

        /*-----------------Problem 4-----------------*/
        //AffineGapPenalties.solve("input/problem4/rosalind_ba5j.txt");

        /*-----------------Problem 5-----------------*/
        //ShortestTransformation.solve("input/problem5/rosalind_ba6d.txt");

        /*-----------------Problem 6-----------------*/
        //AdditivePhylogeny.solve("input/problem6/rosalind_ba7c.txt");

        /*-----------------Problem 7-----------------*/
        //7.1.
        //PatternMatching.solve("input/problem7/rosalind_ba9h.txt");
        //7.2.
        //LongestRepeatString.runExample();

        /*-----------------Problem 8-----------------*/
        //8.1
        //Viterbi v = new Viterbi("input/problem8/rosalind_ba10c.txt");
        //v.probPath();
        //8.2
        //BaumWelch.solve("input/problem8/input.txt");
    }
}