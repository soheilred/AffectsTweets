package edu.unh.cs.ir.pa3;

import co.nstant.in.cbor.CborException;
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

public class Assignment3 {

    public static void main(String[] args) throws FileNotFoundException, CborException {
        Map<String, String> queriesPagesMap = new HashMap<>();
        List<String> qIdList = new ArrayList<>();

        try {
            // make the run file
            FileWriter fileWriter = new FileWriter("run_file");
            BufferedWriter bw = new BufferedWriter(fileWriter);
            String resultString;


            // read the queries' file
            System.setProperty("file.encoding", "UTF-8");
            File fOutlines = new File("./test200/train.test200.cbor.outlines");
            final FileInputStream fileInputStream = new FileInputStream(fOutlines);


            // read the paragraphs' file
            File fParagraphs = new File("./test200/train.test200.cbor.paragraphs");
            final FileInputStream fileInputStream1 = new FileInputStream(fParagraphs);

            // build a lucene index to retrieve paragraphs
            System.out.println("RebuildIndexes");
            Indexer3 indexer = new Indexer3();
            indexer.rebuildIndexes(fileInputStream1);
            System.out.println("RebuildIndexes done");

            // perform search on the query
            // and retrieve the top 100 result
            System.out.println("\n--------------\nPerformSearch:");
            IndexSearcher3 se = new IndexSearcher3(1);

            for (Data.Page page : DeserializeData.iterableAnnotations(fileInputStream)) {
                // Index all Accommodation entries
                queriesPagesMap.put(page.getPageId(), page.getPageName());
                qIdList.add(page.getPageId());
            }
            for (String id : qIdList) {
                String queryPage = queriesPagesMap.get(id);
                System.out.println("\nThe query is: " + queryPage);
                TopDocs topDocs = se.performSearch(queryPage, 100);

                System.out.println("Top " + 100 + " results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                int rank = 0;
                for (ScoreDoc scoreDoc : hits) {
                    Document doc = se.getDocument(scoreDoc.doc);
                    resultString = id + " Q0 " + doc.get("id")
                            + " " + ++rank
                            + " " + scoreDoc.score + ""
                            + " Team3 Practical";
//                        System.out.println(resultString);
                    bw.write(resultString + "\n");
                }
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }

    }

}
