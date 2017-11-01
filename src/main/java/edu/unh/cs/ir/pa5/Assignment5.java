package edu.unh.cs.ir.pa5;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Assignment5 {

    public void task1() {
        String qId;
        String dId;

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("RankLibOutputTask1"));

            String[] runfileFuncs = {"outputs/pa5/bnn_bnn", "outputs/pa5/lnc_ltn", "outputs/pa5/LM_U", "outputs/pa5/U_JM"};

            ArrayList<String> rankLibStr = new ArrayList<>();
            int rank = 0;

            double feature = 0;
            String featureStr = "";
            int target = 0;
            qId = "Q";
            for (int j = 0; j <12; j++) {
                featureStr = "";
                dId = "D" + String.valueOf(j + 1);
                for (int i = 0; i < runfileFuncs.length; i++) {
                    rank = rankParser(runfileFuncs[i], dId, qId);
                    if (rank > 0) {
                        feature = (1.0 / (double) rank);
                    } else {
                        feature = 0;
                    }
                    featureStr = featureStr.concat(" " + (i + 1) + ":" + feature);
                    target = targetParser("outputs/pa5/U_DS", qId, dId);
                }
                rankLibStr.add(target + " qid:" + qId + featureStr + " # " + dId);
            }

            for (String str : rankLibStr) {
                bw.write(str + "\n");
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Exception caught." + e.toString() + "\n");
        }


    }

    public void task2() {
        String qId;
        String dId;

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("RankLibOutput"));

            String[] runfileFuncs = {"outputs/pa5/bnn_bnn", "outputs/pa5/lnc_ltn", "outputs/pa5/LM_U", "outputs/pa5/U_JM", "outputs/pa5/U_DS"};

            ArrayList<String> rankLibStr = new ArrayList<>();
            int rank = 0;
            // read the queries' file
            File fOutlines = new File("./test200/train.test200.cbor.outlines");
            final FileInputStream fISOutlines = new FileInputStream(fOutlines);

            // read the paragraphs' file
            File fParags = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fISParags = new FileInputStream(fParags);

            double feature = 0;
            String featureStr = "";
            int target = 0;
            for (Data.Page page : DeserializeData.iterableAnnotations(fISOutlines)) {
                qId = page.getPageId();
                for (Data.Paragraph paragraph : DeserializeData.iterableParagraphs(fISParags)) {
                    dId = paragraph.getParaId();
                    for (int i = 0; i < runfileFuncs.length; i++) {
                        rank = rankParser(runfileFuncs[i], dId, qId);
                        if (rank > 0) {
                            feature = (1.0 / (double) rank);
                        } else {
                            feature = 0;
                        }
                        featureStr = featureStr.concat(" " + (i + 1) + ":" + feature);
                        target = targetParser("./test200/train.test200.cbor.article.qrels", qId, dId);
                    }
                    rankLibStr.add(target + " qid:" + qId + featureStr + " # " + dId);
                }
            }

            for (String str : rankLibStr) {
                bw.write(str + "\n");
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Exception caught." + e.toString() + "\n");
        }

    }

    public int rankParser(String rf, String doc, String query) {
        int rank = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(rf));
            String line;
            String[] linesArray;
            while ((line = br.readLine()) != null) {
                linesArray = line.split(" ");
                if (linesArray[0].equals(query) && linesArray[2].equals(doc)) {
                    rank = Integer.parseInt(linesArray[3]);
                    break;
                }
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Run File Parser Exception Caught." + e.toString() + "\n");
        }
        return rank;
    }

    public int targetParser(String qrelFile, String doc, String query) {
        int target = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(qrelFile));
            String line;
            String[] linesArray;
            while ((line = br.readLine()) != null) {
                linesArray = line.split(" ");
                if (linesArray[0].equals(query) && linesArray[2].equals(doc)) {
                    target = 1;
                }
                break;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Target Parser Exception Caught." + e.toString() + "\n");
        }
        return target;
    }

    public static void main(String[] args) throws FileNotFoundException, CborException {
        int taskNumber = 1;
        Assignment5 a5 = new Assignment5();

        if (taskNumber == 1) {
            a5.task1();
        } else {
            a5.task2();

        }
    }

}
