package edu.unh.cs.ir.pa3;

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

public class Indexer3 {
    /**
     * Creates a new instance of Indexer1
     */
    public Indexer3() {
    }

    private IndexWriter indexWriter;

    private IndexWriter getIndexWriter() throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("index-directory").toPath());
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }

    public void rebuildIndexes(FileInputStream fileInputStream) throws IOException, CborException {
        for (Data.Page page : DeserializeData.iterableAnnotations(fileInputStream)) {
            // Index all Accommodation entries
            IndexWriter writer = getIndexWriter();
            Document doc = new Document();
            doc.add(new StringField("id", page.getPageId(), Field.Store.YES));
            doc.add(new TextField("name", page.getPageName(), Field.Store.YES));
            writer.updateDocument(new Term("id",page.getPageId()),doc);
        }

        if (indexWriter != null) {
            indexWriter.close();
        }
    }

}