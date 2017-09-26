package edu.unh.cs.ir.toolset;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author John
 */
public class Indexer {

    /**
     * Creates a new instance of Indexer
     */
    public Indexer() {
    }

    private IndexWriter indexWriter;

    public IndexWriter getIndexWriter() throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("index-directory").toPath());
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    public void indexParagraph(Data.Paragraph paragraph) throws IOException {
//        System.out.println("Indexing paragraph: " + paragraph);
        IndexWriter writer = getIndexWriter();
        Document doc = new Document();
        doc.add(new StringField("id", paragraph.getParaId(), Field.Store.YES));
        doc.add(new TextField("content", paragraph.getTextOnly(), Field.Store.YES));
        writer.addDocument(doc);
    }

    public void rebuildIndexes(FileInputStream fileInputStream) throws IOException, CborException {
        //
        // Erase existing index
        //
        getIndexWriter();


        for (Data.Paragraph paragraph : DeserializeData.iterableParagraphs(fileInputStream)) {

            //
            // Index all Accommodation entries
            //
            indexParagraph(paragraph);


            /*List<String> result = new ArrayList<>();
            for (Data.PageSkeleton skel : paragraph) {
                result.addAll(recurseArticle(skel, page.getPageName()));
            }*/

            /*for (String line : result) {
                System.out.println(line);
            }*/
        }
        closeIndexWriter();
    }


   /* private static List<String> recurseArticle(Data.Paragraph parag, String query) {
        if (skel instanceof Data.Para) {

            String text = "";
            for (Data.ParaBody body : paragraph.getBodies()) {
                if (body instanceof Data.ParaLink) text += ((Data.ParaLink) body).getAnchorText();
                if (body instanceof Data.ParaText) text += ((Data.ParaText) body).getText();
            }
            if (text.length() > 10) {
                return Collections.singletonList(query + " " + text);
            } else return Collections.emptyList();
        } else throw new UnsupportedOperationException("not known skel " + skel);
    }*/


     /*System.out.println("hello");


        for (Data.Paragraph paragraph : DeserializeData.iterableParagraphs(fileInputStream)) {

            List<String> result = new ArrayList<>();
            for (Data.PageSkeleton skel : paragraph.getSkeleton()) {
                result.addAll(recurseArticle(skel, page.getPageName()));
            }

            for (String line : result) {
                System.out.println(line);
            }
        }*/


}
