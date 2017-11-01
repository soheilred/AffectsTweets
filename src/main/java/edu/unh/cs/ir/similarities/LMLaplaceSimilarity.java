package edu.unh.cs.ir.similarities;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class LMLaplaceSimilarity extends SimilarityBase {

    private long vocabSize;

    public LMLaplaceSimilarity(int vocabSize) {
        this.vocabSize = vocabSize;
    }


    @Override
    protected float score(BasicStats stats, float freq, float docLen) {
        return (freq + 1) / (docLen + vocabSize);
    }

    @Override
    public String toString() {
        return null;
    }
}