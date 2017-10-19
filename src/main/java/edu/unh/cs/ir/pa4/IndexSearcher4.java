package edu.unh.cs.ir.pa4;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class IndexSearcher4 {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /**
     * Creates a new instance of IndexSearcher1
     */
    public IndexSearcher4(int lm) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("index-directory4").toPath())));
        /*if (custom) {
            SimilarityBase similarityBase = new SimilarityBase() {
                @Override
                protected float score(BasicStats stats, float freq, float docLen) {
                    return stats.getDocFreq();
                }

                @Override
                public String toString() {
                    return "Results from the custom scoring function";
                }
            };
            searcher.setSimilarity(similarityBase);
        }*/

        switch (lm) {
            case 0: //Laplace smoothing
                searcher.setSimilarity(new LMSimilarity() {
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    protected float score(BasicStats stats, float freq, float docLen) {
//                        stats
                        return 0;
                    }
                });
                break;
            case 1: //Jelinek-Mercer smoothing
                searcher.setSimilarity(new LMJelinekMercerSimilarity(0.9f));
                break;
            case 2: //Dirichlet smoothing
                searcher.setSimilarity(new LMDirichletSimilarity(1000f));
                break;
            case 3: //Grads: Bigram language model with Laplace smoothing
                searcher.setSimilarity(new LMDirichletSimilarity(1000f));
                break;
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