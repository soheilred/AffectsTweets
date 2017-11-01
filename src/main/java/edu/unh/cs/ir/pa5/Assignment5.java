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
        String qId;
        String dId;

        Assignment5 a5 = new Assignment5();
        try {
            BufferedWriter bW = new BufferedWriter(new FileWriter("RankLibOutput"));

            String[] runfileFuncs = {"outputs/pa5/LM_U", "outputs/pa5/U_JM", "outputs/pa5/U_DS"};

            ArrayList<String> rankLibStr = new ArrayList<>();
            int rank = 0;
            // read the queries' file
            File fOutlines = new File("./test200/train.test200.cbor.outlines");
            final FileInputStream fISOutlines = new FileInputStream(fOutlines);

            // read the paragraphs' file
            File fParags = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fISParags = new FileInputStream(fParags);

            double feature = 0;
            String featureStr ="";
            int target = 0;
            for (Data.Page page : DeserializeData.iterableAnnotations(fISOutlines)) {
                qId = page.getPageId();
                for (Data.Paragraph paragraph : DeserializeData.iterableParagraphs(fISParags)) {
                    dId = paragraph.getParaId();
                    for (int i = 0; i < 5; i++) {
                        rank = a5.rankParser(runfileFuncs[i], dId, qId);
                        if (rank > 0) {
                            feature = (1.0 / (double) rank);
                        } else {
                            feature = 0;
                        }
                        featureStr = featureStr.concat(" " + i + 1 + ":" + feature);
                        target = a5.targetParser("./train.test200.cbor.article.qrels", qId, dId);
                    }


                }
            }

    } catch(
    Exception e)

    {
        System.out.println("Exception caught." + e.toString() + "\n");
    }


//        Map<String, String> queriesMap = new HashMap<>();
//        List<String> qIdList = new ArrayList<>();
//        int methodsNum = 5;
//        int searchCutOff = 10;

        try

    {
        // make the run file:
//            BufferedWriter bW = new BufferedWriter(new FileWriter("run_file_default5"));
//            String resultString;


        // read the queries' file
            /*System.setProperty("file.encoding", "UTF-8");
            File fOutlines = new File("./test200/train.test200.cbor.outlines");
            final FileInputStream FISOutlines = new FileInputStream(fOutlines);

            // read the paragraphs' file
            System.setProperty("file.encoding", "UTF-8");
            File fParags = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fISParags = new FileInputStream(fParags);
*/
        Map<String, String[]> rankingsMap = new HashSet<>();

        String[] rank = new String[20];
        rank[0] = "D1";
        rank[1] = "D2";
        rank[2] = "D3";
        rank[3] = "D4";
        rank[4] = "D5";
        rank[5] = "D6";
        rankingsMap.put("R1", rank);

        rank = new String[20];
        rank[0] = "D2";
        rank[1] = "D5";
        rank[2] = "D6";
        rank[2] = "D7";
        rank[3] = "D8";
        rank[4] = "D9";
        rank[5] = "D10";
        rank[6] = "D11";
        rankingsMap.put("R2", rank);

        rank = new String[20];
        rank[0] = "D1";
        rank[1] = "D2";
        rank[2] = "D5";
        rankingsMap.put("R3", rank);

        rank = new String[20];
        rank[0] = "D1";
        rank[1] = "D2";
        rank[2] = "D8";
        rank[3] = "D10";
        rank[4] = "D12";
        rankingsMap.put("R4", rank);

        float features[][] = new float[4][12];

        for (int i = 0; i < 4; i++) {
            String[] rnk = rankingsMap.get(i + 1);

            for (int j = 0; j < rnk.length; j++) {
                for ()
                    if (rnk.contains("D" + j)) {
                        features[i][j] = (float) (1 / (rnk.indexOf("D" + j) + 1));
                    } else {
                        features[i][j] = 0;

                    }
            }

        }


        BufferedWriter bwRankLib = new BufferedWriter(new FileWriter("RankLib"));

        String rlFormatted = 1 + " qid:" + 1;
        for (int i = 0; i < features[0].length; i++) {
            rlFormatted = rlFormatted.concat(" " + i + ":" + String.valueOf(features[0][i]));
        }
        bwRankLib.write(rlFormatted);
        bwRankLib.close();


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


    } catch(
    Exception e)

    {
        System.out.println("Exception caught.\n");
    }

}

}
