package edu.unh.cs.ir.pa1;

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
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Indexer1 {

    /**
     * Creates a new instance of Indexer1
     */
    public Indexer1() {
    }

    private IndexWriter indexWriter;

    private IndexWriter getIndexWriter() throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("index-directory1").toPath());
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }

    public void rebuildIndexes(FileInputStream fileInputStream) throws IOException, CborException {
        for (Data.Paragraph paragraph : DeserializeData.iterableParagraphs(fileInputStream)) {
            // Index all Accommodation entries
            IndexWriter writer = getIndexWriter();
            Document doc = new Document();
            doc.add(new StringField("id", paragraph.getParaId(), Field.Store.YES));
            doc.add(new TextField("content", paragraph.getTextOnly(), Field.Store.YES));
            writer.updateDocument(new Term("id",paragraph.getParaId()),doc);
        }

        if (indexWriter != null) {
            indexWriter.close();
        }
    }

}