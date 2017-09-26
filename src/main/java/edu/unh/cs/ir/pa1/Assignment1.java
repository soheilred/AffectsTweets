package edu.unh.cs.ir.pa1;

import co.nstant.in.cbor.CborException;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Assignment1 {

    private static String[] queries =
            {
                    "power nap benefits",
                    "whale vocalization production of sound",
                    "pokemon puzzle league"
            };

    public static void main(String[] args) throws FileNotFoundException, CborException {
        try {
            System.setProperty("file.encoding", "UTF-8");
            File file = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fileInputStream = new FileInputStream(file);

            // build a lucene index
            System.out.println("RebuildIndexes");
            Indexer1 indexer1 = new Indexer1();
            indexer1.rebuildIndexes(fileInputStream);
            System.out.println("RebuildIndexes done");

            // perform search on the query
            // and retrieve the top 10 result
            System.out.println("\n--------------\nPerformSearch:");
            IndexSearcher1 se = new IndexSearcher1(false);
            for (String query : queries) {
                System.out.println("\nThe query is: " + query);
                TopDocs topDocs = se.performSearch(query, 10);

                System.out.println("Top " + 10 + " results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                for (ScoreDoc scoreDoc : hits) {
                    Document doc = se.getDocument(scoreDoc.doc);
                    System.out.println(doc.get("id")
                            + " " + doc.get("content")
                            + " (" + scoreDoc.score + ")");
                }
            }

            // perform search on the query
            // and retrieve the top 10 result
            // using the custom scoring function
            System.out.println("\n--------------\nPerformSearch (custom scoring function):");
            IndexSearcher1 seCustom = new IndexSearcher1(true);
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