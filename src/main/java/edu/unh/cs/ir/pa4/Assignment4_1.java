package edu.unh.cs.ir.pa4;

import co.nstant.in.cbor.CborException;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Assignment4_1 {


    public static void main(String[] args) throws FileNotFoundException, CborException {

        try {
            String resultString;

            // read the paragraphs' file
            System.setProperty("file.encoding", "UTF-8");
            File fParags = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fISParags = new FileInputStream(fParags);

            // build a lucene index to retrieve paragraphs
            System.out.println("RebuildIndexes");
            Indexer4 indexer = new Indexer4();
            indexer.buildIndexes(fISParags, null); //pass the specific similarity to indexer
            System.out.println("RebuildIndexes done");

            // perform search on the query
            // and retrieve the top 100 result
            System.out.println("\n--------------\nPerformSearch:");

            //new LMDirichletSimilarity(1000f)
            //new LMJelinekMercerSimilarity(0.9f)
            IndexSearcher4 se = new IndexSearcher4(new LMDirichletSimilarity(1000f)); //

            TopDocs topDocs = se.performSearch("Brush%20rabbit", 10);
            System.out.println("Top " + " results found: " + topDocs.totalHits);
            ScoreDoc[] hits = topDocs.scoreDocs;
            int rank = 0;
            for (ScoreDoc scoreDoc : hits) {
                Document doc = se.getDocument(scoreDoc.doc);
                resultString = 1 + " Q0 " + doc.get("id")
                        //+ " " + doc.get("content")
                        + " " + ++rank
                        + " " + scoreDoc.score + ""
                        + " Team3-Practical";
                System.out.println(resultString);
            }
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }

    }

}
