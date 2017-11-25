package edu.unh.cs.ir.proj;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.ir.similarities.*;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.SimilarityBase;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AffectsProj {


    public static void main(String[] args) throws FileNotFoundException, CborException {
        Map<String, String> idQueryMap = new HashMap<>(); // map of (id,Tweets)
        Map<String, String> idScoreMap = new HashMap<>(); // map of (id,Scores)
        List<String> tIdList = new ArrayList<>(); // Tweet ID list
        int resultsNum = 1; // number of retrieved top docs
//        List<Integer> scoreList = new ArrayList<>();
        tweetsParser(tIdList, idQueryMap, idScoreMap, "./AffectsTweets/EI-reg-en_joy_train.txt");
        List<Map<String, Float>> rankList = new ArrayList<>();
        double maxScore[] = {0, 0, 0};

        // "dirichlet", "lnc-ltn", "anc-apc"

        String[] simsName = {
                "laplace",
                "jelinekmercer",
                "bnn-bnn"
        }; // null, new LNCSimilarity(), new ANCSimilarity()
        SimilarityBase[] simsIndex = {
                null,
                null,
                new BNNSimilarity()
        }; //new LMDirichletSimilarity(0f), new LTNSimilarity(), new APCSimilarity()
        SimilarityBase[] simsQuery = {
                new LMLaplaceSimilarity(1),
                new LMJelinekMercerSimilarity(0.9f),
                new BNNSimilarity()
        };


        for (int i = 0; i < simsName.length; i++) {

            try {
                // build a lucene index to retrieve paragraphs
                System.out.println("RebuildIndexes");
                IndexerAff indexer = new IndexerAff();
                indexer.buildIndexes("./AffectsTweets/joy_database", simsIndex[i], simsName[i]); //pass the specific similarity to indexer
                System.out.println("RebuildIndexes done");

                // perform search on the query
                System.out.println("--------------\nPerformSearch:");

                IndexSearcherAff se = new IndexSearcherAff(simsQuery[i], simsName[i]);

                Map<String, Float> scoresMap = new HashMap<>();
                for (String id : tIdList) {
                    String query = idQueryMap.get(id);
//                    System.out.println("\nThe query is: " + query);
                            TopDocs topDocs = se.performSearch(query, resultsNum);

//                    System.out.println("Top " + resultsNum + " results found: " + topDocs.totalHits);
                    ScoreDoc[] hits = topDocs.scoreDocs;

//                    System.out.print("s:" + hits[0].score + "\n");


                    float hitsScore = 0f;
                    if (hits.length != 0) {
//                        resultString = id + "\t" + query + "\t" + "joy" + "\t" + (maxScore == 0 ? 0 : hits[0].score / maxScore);
                        hitsScore = hits[0].score;
                    } else {
//                        resultString = id + "\t" + query + "\t" + "joy" + "\t" + 0.0;
                        hitsScore = 0f;
                    }
                    scoresMap.put(id, hitsScore);
                    if (maxScore[i] < hitsScore) {
                        maxScore[i] = hitsScore;
                    }
                }
                rankList.add(scoresMap);

            } catch (Exception e) {
                System.out.println("Exception caught in lucene search.\n" + e.getMessage() + "\n" + e.toString());
            }
        }

        //run files creator
        int rankNum = 0;
        for (Map<String, Float> ranking : rankList) {
            try {
                // make the run file
                BufferedWriter bW = new BufferedWriter(new FileWriter("./AffectsTweets/" + "runfile-affects-" + simsName[rankNum]));

                for (int lineNum = 30000; lineNum < 31616; lineNum++) {
                    String resultString = "";

//                System.out.print(ranking.get(Integer.toString(lineNum)));
                    float score = ranking.get(Integer.toString(lineNum));
                    resultString = lineNum + "\t" + idQueryMap.get(Integer.toString(lineNum)) + "\t" + "joy" + "\t" + (maxScore[rankNum] == 0 ? 0 : score / maxScore[rankNum]);

                    bW.write(resultString);
                    bW.newLine();
                }
                rankNum++;
                bW.close();
            } catch (Exception e) {
                System.out.println("Exception caught in run_file creation.\n" + e.getMessage() + "\n" + e.toString());
            }
        }

        // RankLib file creator
        //id[tab]tweet[tab]emotion[tab]score
        try {
            BufferedWriter rankLibbW = new BufferedWriter(new FileWriter("./AffectsTweets/RankLib"));
            for (int lineNum = 30000; lineNum < 31616; lineNum++) {
                String resultString = lineNum + "\t" + idScoreMap.get(Integer.toString(lineNum));
                int rankLibNum = 0;
                for (Map<String, Float> ranking : rankList) {
                    float score = ranking.get(Integer.toString(lineNum));
                    score = maxScore[rankLibNum] == 0 ? 0 : score / (float) maxScore[rankLibNum];
                    resultString = resultString + "\t" + score;
                    rankLibNum++;
                }
                rankLibbW.write(resultString);
                rankLibbW.newLine();
            }
            rankLibbW.close();
        } catch (Exception e) {
            System.out.println("Exception caught in RankLib file creation.\n" + e.getMessage() + "\n" + e.toString());
        }

    }

    //parse tweets and populate correspondent structures
    //id[tab]tweet[tab]emotion[tab]score
    private static void tweetsParser(List<String> list, Map<String, String> queryMap, Map<String, String> scoreMap, String filepath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;
            String[] linesArray;
            while ((line = br.readLine()) != null) {
                linesArray = line.split("\t");
                list.add(linesArray[0]);
                queryMap.put(linesArray[0], linesArray[1]);
                scoreMap.put(linesArray[0], linesArray[3]);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Tweets Parser Exception Caught." + e.toString() + "\n");
        }
    }

}
