package edu.unh.cs.ir.pa5;

public class RankClass {
    private String docID;
    private int rank;

    public RankClass(String doc, int rank){
        this.docID = doc;
        this.rank = rank;
    }
    public int getRank(){
        return rank;
    }
}