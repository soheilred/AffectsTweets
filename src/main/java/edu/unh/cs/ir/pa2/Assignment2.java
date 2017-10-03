package edu.unh.cs.ir.pa2;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.ir.pa3.IndexSearcher3;
import edu.unh.cs.ir.pa3.Indexer3;
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

public class Assignment2 {

    public static void main(String[] args) throws FileNotFoundException, CborException {
        Map<String, String> queriesMap = new HashMap<>();
        List<String> qIdList = new ArrayList<>();

        try {
            // make the run file:
            // run_file_default2
            // run_file_custom2
            BufferedWriter bW = new BufferedWriter(new FileWriter("run_file_default2"));
            String resultString;

            // read the queries' file
            System.setProperty("file.encoding", "UTF-8");
            File fOutlines = new File("./test200/train.test200.cbor.outlines");
            final FileInputStream FISOutlines = new FileInputStream(fOutlines);

            // read the paragraphs' file
            System.setProperty("file.encoding", "UTF-8");
            File fParags = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fISParags = new FileInputStream(fParags);

            // build a lucene index to retrieve paragraphs
            System.out.println("RebuildIndexes");
            Indexer2 indexer = new Indexer2();
            indexer.rebuildIndexes(fISParags);
            System.out.println("RebuildIndexes done");

            // perform search on the query
            // and retrieve the top 100 result
            System.out.println("\n--------------\nPerformSearch:");
            IndexSearcher2 se = new IndexSearcher2(false);

            for (Data.Page page : DeserializeData.iterableAnnotations(FISOutlines)) {
                // Index all Accommodation entries
                queriesMap.put(page.getPageId(), page.getPageName());
                qIdList.add(page.getPageId());
            }
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
                            + " Team3-Practical";
                    bW.write(resultString);
                    bW.newLine();
                        System.out.println(resultString);
                }
            }
            bW.close();
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }

    }

}
