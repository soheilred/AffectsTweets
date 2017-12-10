package proj;

import co.nstant.in.cbor.CborException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class IndexerAff {

    public IndexerAff() {
    }

    private IndexWriter indexWriter;

    public void buildIndexes(String path, SimilarityBase similarity, String simsName) throws IOException, CborException {
        String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
//        for (Data.Paragraph paragraph : DeserializeData.iterableParagraphs(fileInputStream)) {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("index-directory-affects/" + simsName).toPath());
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            if (similarity != null) {
                config.setSimilarity(similarity);
            }
            indexWriter = new IndexWriter(indexDir, config);
        }
        IndexWriter writer = indexWriter;

        Document doc = new Document();
        doc.add(new StringField("id", "1", Field.Store.YES));
        doc.add(new TextField("content", text, Field.Store.YES));

        writer.updateDocument(new Term("id", "1"), doc);
//        }
        System.out.println("You have " + indexWriter.numDocs() + " docs indexed");

        if (indexWriter != null) {
            indexWriter.close();
        }
    }

}