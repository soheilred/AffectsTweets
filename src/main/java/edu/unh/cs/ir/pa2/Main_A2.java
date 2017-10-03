package edu.unh.cs.ir.pa2;



import co.nstant.in.cbor.CborException;
        import edu.unh.cs.ir.pa2.Indexer;
        import edu.unh.cs.ir.pa2.SearchEngine;
        import org.apache.lucene.document.Document;
        import org.apache.lucene.search.ScoreDoc;
        import org.apache.lucene.search.TopDocs;

        import java.io.*;

public class Main_A2 {

    public static void main(String[] args) throws FileNotFoundException, CborException {

        try {

            System.setProperty("file.encoding", "UTF-8");
            File file = new File(args[0]);
            File paraFile = new File("/media/piyush/7C6446066445C41C/0 MS in CS/Fall-2017/IR/test200/train.test200.cbor.paragraphs");
            final FileInputStream fileInputStream = new FileInputStream(file);
            final FileInputStream paraFileInputStream = new FileInputStream(paraFile);

            // build a lucene index
            System.out.println("rebuildIndexes");
            Indexer indexer = new Indexer("page-index-directory");
            Indexer paraIndexer = new Indexer("index-directory");
            indexer.rebuildIndexes(fileInputStream);
            paraIndexer.rebuildIndexes1(paraFileInputStream);
            FileWriter f1 = new FileWriter("results2.txt");
            BufferedWriter bw = new BufferedWriter(f1);

            String resultString = null;

            for(int i=0; i<indexer.nid.size();i++){
                //System.out.println("ID: "+indexer.nid.get(i).id + "\tname: "+indexer.nid.get(i).name);
                //System.out.println("rebuildIndexes done");

                // perform search on the query
                // and retrieve the top 10 result
                System.out.println("performSearch");

                //below two lines does scoring
                SearchEngine se = new SearchEngine();
                TopDocs topDocs = se.performSearch(indexer.nid.get(i).name, 100);

                //System.out.println(indexer.nid.get(i).id + " Total hit: "+topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                int rank = 0;
                for (ScoreDoc scoreDoc :hits) {
                    Document doc = se.getDocument(scoreDoc.doc);
                    resultString = indexer.nid.get(i).id + " Q0 " +doc.get("id")
                            //+ " " + doc.get("content")
                            + " " + ++rank
                            + " " + scoreDoc.score + ""
                            + " Team3 Practical";
                    System.out.println(resultString);
                    bw.write(resultString+"\n");

                }

            }
            bw.close();
            f1.close();


            System.out.println("performSearch done");
            indexer.cleanIndex();
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
            e.printStackTrace();
        }
    }

}
