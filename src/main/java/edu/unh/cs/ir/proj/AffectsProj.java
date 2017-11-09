package edu.unh.cs.ir.proj;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.ir.pa4.IndexSearcher4;
import edu.unh.cs.ir.pa4.Indexer4;
import edu.unh.cs.ir.similarities.LMLaplaceSimilarity;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AffectsProj {


    public static void main(String[] args) throws FileNotFoundException, CborException {
        List<String> tIdList = new ArrayList<>();
        Map<String, String> tMap = new HashMap<>();
        int resultsNum = 1;
        String resultString;

        try {
            // make the run file:
            // run_file_default4
            // run_file_custom4
            BufferedWriter bW = new BufferedWriter(new FileWriter("./AffectsTweets/affects-runfile"));

            // build a lucene index to retrieve paragraphs
            System.out.println("RebuildIndexes");
            IndexerAff indexer = new IndexerAff();
            indexer.buildIndexes("./AffectsTweets/joy_database", null); //pass the specific similarity to indexer
            System.out.println("RebuildIndexes done");

            // perform search on the query
            // and retrieve the top 100 result
            System.out.println("\n--------------\nPerformSearch:");

            //new LMDirichletSimilarity(1000f)
            //new LMJelinekMercerSimilarity(0.9f)
            IndexSearcherAff se = new IndexSearcherAff(new LMLaplaceSimilarity(1));

            tweetsParser(tIdList, tMap, "./AffectsTweets/EI-reg-en_joy_train.txt");

            for (String id : tIdList) {
                String query = tMap.get(id);
                System.out.println("\nThe query is: " + query);
                TopDocs topDocs = se.performSearch(query, resultsNum);

                System.out.println("Top " + resultsNum + " results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                for (ScoreDoc scoreDoc : hits) {
                    Document doc = se.getDocument(scoreDoc.doc);
                    //id[tab]tweet[tab]emotion[tab]score
                    resultString = id + "\t" + query + "\t" + "joy" + "\t" + scoreDoc.score;

                    bW.write(resultString);
                    bW.newLine();
                    System.out.println(resultString);
                }
            }
            bW.close();
        } catch (Exception e) {
            System.out.println("Main Exception caught.\n" + e.getMessage() + "\n" + e.toString());
        }

    }

    //id[tab]tweet[tab]emotion[tab]score
    private static void tweetsParser(List<String> list, Map<String, String> map, String filepath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;
            String[] linesArray;
            while ((line = br.readLine()) != null) {
                linesArray = line.split("\t");
                list.add(linesArray[0]);
                map.put(linesArray[0], linesArray[1]);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Tweets Parser Exception Caught." + e.toString() + "\n");
        }
    }

}
