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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assignment5 {


    public static void main(String[] args) throws FileNotFoundException, CborException {
        Map<String, String> queriesMap = new HashMap<>();
        List<String> qIdList = new ArrayList<>();
        int methodsNum = 5;
        int searchCutOff = 10;

        try {
            // make the run file:
//            BufferedWriter bW = new BufferedWriter(new FileWriter("run_file_default5"));
//            String resultString;


            // read the queries' file
            System.setProperty("file.encoding", "UTF-8");
            File fOutlines = new File("./test200/train.test200.cbor.outlines");
            final FileInputStream FISOutlines = new FileInputStream(fOutlines);

            // read the paragraphs' file
            System.setProperty("file.encoding", "UTF-8");
            File fParags = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fISParags = new FileInputStream(fParags);

            List<Integer> rankList = new ArrayList<>();
            Map<Integer, List<Integer>> rankingsMap = new HashMap<>();

            rankList.add(1);
            rankList.add(2);
            rankList.add(3);
            rankList.add(4);
            rankList.add(5);
            rankList.add(6);
            rankingsMap.put(1, rankList);

            rankingsMap.clear();

            rankList.add(2);
            rankList.add(5);
            rankList.add(6);
            rankList.add(7);
            rankList.add(8);
            rankList.add(9);
            rankList.add(10);
            rankList.add(11);
            rankingsMap.put(2, rankList);

            rankingsMap.clear();

            rankList.add(1);
            rankList.add(2);
            rankList.add(5);
            rankingsMap.put(3, rankList);

            rankingsMap.clear();

            rankList.add(1);
            rankList.add(2);
            rankList.add(8);
            rankList.add(10);
            rankList.add(12);
            rankingsMap.put(4, rankList);

            float features[][] = new float[4][];

            for (int i = 1; i <= 4; i++) {
                List<Integer> lrnk = rankingsMap.get(i);

                for (int j = 1; j <= lrnk.size(); j++) {
                    if (lrnk.contains(j)) {
                        features[i][j] = (1 / lrnk.indexOf(j));
                    } else {
                        features[i][j] = 0;
                    }
                }

            }

            BufferedWriter bwRankLib = new BufferedWriter(new FileWriter("RankLib"));

            String rlFormatted = 1 + " qid:" + 1;
            for (int i = 1; i <= features[1].length; i++) {
                rlFormatted = rlFormatted.concat(" " + i + ":" + String.valueOf(features[1][i]));
            }
            bwRankLib.write(rlFormatted);


//            3 qid:1 1:1 2:1 3:0 4:0.2 5:0 # 1A


            // build a lucene index to retrieve paragraphs
            /*System.out.println("RebuildIndexes");
            Indexer5 indexer = new Indexer5();
            indexer.buildIndexes(fISParags, null); //pass the specific similarity to indexer
            System.out.println("RebuildIndexes done");

            // perform search on the query
            // and retrieve the top 100 result
            System.out.println("\n--------------\nPerformSearch:");

            //new LMDirichletSimilarity(1000f)
            //new LMJelinekMercerSimilarity(0.9f)
            IndexSearcher5 se = new IndexSearcher5(new LMDirichletSimilarity(1000f)); //

            int q_num = 0;
            for (Data.Page page : DeserializeData.iterableAnnotations(FISOutlines)) {
                // Index all Accommodation entries
                queriesMap.put(page.getPageId(), page.getPageName());
                qIdList.add(page.getPageId());
                q_num++;
            }

            int i = 0,j = 0;
            for (String id : qIdList) {
                String query = queriesMap.get(id);
                System.out.println("\nQuery: " + query);
                TopDocs topDocs = se.performSearch(query, searchCutOff);

                System.out.println("Top " + searchCutOff + " results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                int ranking = 0;
                for (ScoreDoc scoreDoc : hits) {
                    Document doc = se.getDocument(scoreDoc.doc);

//                    resultString = id + " Q0 " + doc.get("id")
//                            //+ " " + doc.get("content")
//                            + " " + ++ranking
//                            + " " + scoreDoc.score + ""
//                            + " Team3-Practical";
//                    bW.write(resultString);
//                    bW.newLine();
//                    System.out.println(resultString);
                    j++;
                }
                i++;
            }
            bW.close();*/


        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }

    }

}
