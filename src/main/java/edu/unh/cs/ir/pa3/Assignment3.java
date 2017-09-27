package edu.unh.cs.ir.pa3;

//train.test200.cbor.outlines

import co.nstant.in.cbor.CborException;
import edu.unh.cs.ir.pa3.IndexSearcher3;
import edu.unh.cs.ir.pa3.Indexer3;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.*;

public class Assignment3 {




    public static void main(String[] args) throws FileNotFoundException, CborException {
        try {
            System.setProperty("file.encoding", "UTF-8");
            File file = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fileInputStream = new FileInputStream(file);

            // making the run file
            FileWriter f1 = new FileWriter("run_file");
            BufferedWriter bw = new BufferedWriter(f1);
            String resultString = null;


            // build a lucene index to retrieve page ids
            System.out.println("RebuildIndexes");
            Indexer3 indexer_1 = new Indexer3();
            indexer_1.rebuildIndexes(fileInputStream);
            System.out.println("RebuildIndexes done");


            // build a lucene index to retrieve paragraphs
            System.out.println("RebuildIndexes");
            Indexer3 indexer_2 = new Indexer3();
            indexer_2.rebuildIndexes(fileInputStream);
            System.out.println("RebuildIndexes done");


            // perform search on the query
            // and retrieve the top 10 result
            System.out.println("\n--------------\nPerformSearch:");
            IndexSearcher3 se = new IndexSearcher3(false);
            for (String query : queries) {
                System.out.println("\nThe query is: " + query);
                TopDocs topDocs = se.performSearch(query, 100);

                System.out.println("Top " + 10 + " results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                for (ScoreDoc scoreDoc : hits) {
                    Document doc = se.getDocument(scoreDoc.doc);
                    resultString = indexer_1.nid.get(i).id + " Q0 " +doc.get("id")
                            //+ " " + doc.get("content")
                            + " " + ++rank
                            + " " + scoreDoc.score + ""
                            + " Team3 Practical";
                    System.out.println(resultString);
                    bw.write(resultString+"\n");
                }
            }

            // perform search on the query
            // and retrieve the top 10 result
            // using the custom scoring function
            System.out.println("\n--------------\nPerformSearch (custom scoring function):");
            IndexSearcher3 seCustom = new IndexSearcher3(true);
            for (String query : queries) {
                System.out.println("\nThe query is: " + query);
                TopDocs topDocs = seCustom.performSearch(query, 10);

                System.out.println("Top " + 10 + " results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                for (ScoreDoc scoreDoc : hits) {
                    Document doc = seCustom.getDocument(scoreDoc.doc);
                    System.out.println(doc.get("id")
                            + " " + doc.get("content")
                            + " (" + scoreDoc.score + ")");
                }
            }

            System.out.println("\nPerformSearch done");
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }
    }

}
