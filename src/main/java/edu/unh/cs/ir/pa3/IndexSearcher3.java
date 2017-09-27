package edu.unh.cs.ir.pa3;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;

public class IndexSearcher3 {

    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /**
     * Creates a new instance of IndexSearcher1
     */
    public IndexSearcher3(int type) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("index-directory3").toPath())));
        switch (type) {
            case (1):
                searcher.setSimilarity(new TFIDFSimilarity() {

                    float sumSW;

                    @Override
                    public float coord(int overlap, int maxOverlap) {
                        return 0;
                    }

                    @Override
                    public float queryNorm(float sumOfSquaredWeights) {
                        sumSW = sumOfSquaredWeights;
                        return 0;
                    }

                    @Override
                    public float tf(float freq) {
                        return (float) (1 + Math.log(freq)) / sumSW;
                    }

                    @Override
                    public float idf(long docFreq, long docCount) {
                        return (float) (1 + Math.log(docCount / docFreq));
                    }

                    @Override
                    public float lengthNorm(FieldInvertState state) {
                        return 0;
                    }

                    @Override
                    public float decodeNormValue(long norm) {
                        return 0;
                    }

                    @Override
                    public long encodeNormValue(float f) {
                        return 0;
                    }

                    @Override
                    public float sloppyFreq(int distance) {
                        return 0;
                    }

                    @Override
                    public float scorePayload(int doc, int start, int end, BytesRef payload) {
                        return 0;
                    }
                });
                break;
            case (2):
                searcher.setSimilarity(new TFIDFSimilarity() {
                    @Override
                    public float coord(int overlap, int maxOverlap) {
                        return 0;
                    }

                    @Override
                    public float queryNorm(float sumOfSquaredWeights) {
                        return 0;
                    }

                    @Override
                    public float tf(float freq) {
                        return 0;
                    }

                    @Override
                    public float idf(long docFreq, long docCount) {
                        return 0;
                    }

                    @Override
                    public float lengthNorm(FieldInvertState state) {
                        return 0;
                    }

                    @Override
                    public float decodeNormValue(long norm) {
                        return 0;
                    }

                    @Override
                    public long encodeNormValue(float f) {
                        return 0;
                    }

                    @Override
                    public float sloppyFreq(int distance) {
                        return 0;
                    }

                    @Override
                    public float scorePayload(int doc, int start, int end, BytesRef payload) {
                        return 0;
                    }
                });
                break;
            case (3):
                searcher.setSimilarity(new TFIDFSimilarity() {
                    @Override
                    public float coord(int overlap, int maxOverlap) {
                        return 0;
                    }

                    @Override
                    public float queryNorm(float sumOfSquaredWeights) {
                        return 0;
                    }

                    @Override
                    public float tf(float freq) {
                        return 0;
                    }

                    @Override
                    public float idf(long docFreq, long docCount) {
                        return 0;
                    }

                    @Override
                    public float lengthNorm(FieldInvertState state) {
                        return 0;
                    }

                    @Override
                    public float decodeNormValue(long norm) {
                        return 0;
                    }

                    @Override
                    public long encodeNormValue(float f) {
                        return 0;
                    }

                    @Override
                    public float sloppyFreq(int distance) {
                        return 0;
                    }

                    @Override
                    public float scorePayload(int doc, int start, int end, BytesRef payload) {
                        return 0;
                    }
                });
                break;
            default:
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