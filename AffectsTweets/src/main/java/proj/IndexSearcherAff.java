package proj;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class IndexSearcherAff {

    private IndexSearcher searcher = null;
    private QueryParser parser = null;


    /*
    * "index-directory-affects/" + simsName*/

    public IndexSearcherAff(SimilarityBase similarity, String simsName) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("index-directory-affects/" + simsName).toPath())));
        if (similarity != null) {
            searcher.setSimilarity(similarity);
        }
        parser = new QueryParser("content", new StandardAnalyzer());
    }

    public TopDocs performSearch(String queryString, int n)
            throws IOException, ParseException {
        Query query = parser.parse(QueryParser.escape(queryString));
        return searcher.search(query, n);
    }

    public Document getDocument(int docId)
            throws IOException {
        return searcher.doc(docId);
    }
}