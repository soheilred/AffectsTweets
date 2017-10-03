package edu.unh.cs.ir.pa3;

//train.test200.cbor.outlines
//train.test200.cbor.paragraphs

import co.nstant.in.cbor.CborException;
import edu.unh.cs.ir.pa3.IndexSearcher3;
import edu.unh.cs.ir.pa3.Indexer3;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assignment3 {




    public static void main(String[] args) throws FileNotFoundException, CborException {
        Map<String, String> queriesMap = new HashMap<>();
        List<String> qIdList = new ArrayList<>();
        try {
            // make the run file
            FileWriter f1 = new FileWriter("run_file");
            BufferedWriter bw = new BufferedWriter(f1);
            String resultString = null;


            // read the queries' file
            System.setProperty("file.encoding", "UTF-8");
            File file = new File("./test200/train.test200.cbor.outlines");
            final FileInputStream fileInputStream = new FileInputStream(file);


            // read the paragraphs' file
            System.setProperty("file.encoding", "UTF-8");
            File file1 = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fileInputStream1 = new FileInputStream(file1);

            // build a lucene index to retrieve paragraphs
            System.out.println("RebuildIndexes");
            Indexer3 indexer_3 = new Indexer3();
            indexer_3.rebuildIndexes(fileInputStream1);
            System.out.println("RebuildIndexes done");


            // perform search on the query
            // and retrieve the top 100 result
            System.out.println("\n--------------\nPerformSearch:");
            IndexSearcher3 se = new IndexSearcher3(1);

            for (Data.Page page : DeserializeData.iterableAnnotations(fileInputStream)) {
                // Index all Accommodation entries
                queriesMap.put(page.getPageId(), page.getPageName());
                qIdList.add(page.getPageId());
                for (String id : qIdList) {
                    String query = queriesMap.get(id);
                    System.out.println("\nThe query is: " + query);
                    TopDocs topDocs = se.performSearch(query, 100);

                    System.out.println("Top " + 100 + " results found: " + topDocs.totalHits);
                    ScoreDoc[] hits = topDocs.scoreDocs;

                    int rank = 0;
                    for (ScoreDoc scoreDoc : hits) {
                        Document doc = se.getDocument(scoreDoc.doc);
                        resultString = id + " Q0 " + doc.get("id")
                                //+ " " + doc.get("content")
                                + " " + ++rank
                                + " " + scoreDoc.score + ""
                                + " Team3 Practical";
                        System.out.println(resultString);
                        bw.write(resultString + "\n");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }


            // perform search on the query
            // and retrieve the top 10 result
            // using the custom scoring function
//            System.out.println("\n--------------\nPerformSearch (custom scoring function):");
//            IndexSearcher3 seCustom = new IndexSearcher3(true);
//            for (String query : queries) {
//                System.out.println("\nThe query is: " + query);
//                TopDocs topDocs = seCustom.performSearch(query, 10);
//
//                System.out.println("Top " + 10 + " results found: " + topDocs.totalHits);
//                ScoreDoc[] hits = topDocs.scoreDocs;
//
//                for (ScoreDoc scoreDoc : hits) {
//                    Document doc = seCustom.getDocument(scoreDoc.doc);
//                    System.out.println(doc.get("id")
//                            + " " + doc.get("name")
//                            + " (" + scoreDoc.score + ")");
//                }
//            }

//            System.out.println("\nPerformSearch done");

    }

}
