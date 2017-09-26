package edu.unh.cs.ir.pa1;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class IndexSearcher1 {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /**
     * Creates a new instance of IndexSearcher1
     */
    public IndexSearcher1(boolean custom) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("index-directory").toPath())));
        if (custom) {
            SimilarityBase similarityBase = new SimilarityBase() {
                @Override
                protected float score(BasicStats stats, float freq, float docLen) {
                    return stats.getTotalTermFreq();
                }

                @Override
                public String toString() {
                    return "Results from the custom scoring function";
                }
            };
            searcher.setSimilarity(similarityBase);
        }
        parser = new QueryParser("content", new StandardAnalyzer());
    }

    public TopDocs performSearch(String queryString, int n)
            throws IOException, ParseException {
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }

    public Document getDocument(int docId)
            throws IOException {
        return searcher.doc(docId);
    }
}