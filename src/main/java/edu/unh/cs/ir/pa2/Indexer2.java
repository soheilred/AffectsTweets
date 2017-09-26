/*
 * Indexer.java
 *
 * Created on 6 March 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.unh.cs.ir.pa2;

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
import java.nio.file.Path;

/**
 * @author John
 */
public class Indexer2 {

    /**
     * Creates a new instance of Indexer
     */
    public Indexer2() {
    }

    private IndexWriter indexWriter = null;
    File f1 = new File("index-directory");
    Directory indexDir;
    String file1 = "index-directory";
    int count =0;

    public IndexWriter getIndexWriter() throws IOException {
        if (indexWriter == null) {
        	System.out.println("indexWriter is null");
        	f1 = new File(file1);
        	indexDir = FSDirectory.open(f1.toPath());
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }else{
        	//System.out.println("indexWriter is not null");
        	
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
    	//indexWriter = null;
        //getIndexWriter();


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

    public void cleanIndex(){
    	File f[] = f1.listFiles();
        for(int i=0;i<f.length;i++){
        	f[i].delete();
        }
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
