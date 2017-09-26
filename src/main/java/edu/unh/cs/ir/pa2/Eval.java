package edu.unh.cs.ir.pa2;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.ir.toolset.SearchEngine;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Eval {

    private static List<String> recurseArticle(Data.PageSkeleton skel, String query) {

        if (skel instanceof Data.Section) {
            final Data.Section section = (Data.Section) skel;
            String query2 = section.getHeading();

            List<String> result = new ArrayList<>();
            for (Data.PageSkeleton child : section.getChildren()) {
                result.addAll(recurseArticle(child, query + " " + query2));
            }
            return result;

        } else if (skel instanceof Data.Para) {
            Data.Para para = (Data.Para) skel;
            Data.Paragraph paragraph = para.getParagraph();

            String text = "";
            for (Data.ParaBody body : paragraph.getBodies()) {
                if (body instanceof Data.ParaLink) text += ((Data.ParaLink) body).getAnchorText();
                if (body instanceof Data.ParaText) text += ((Data.ParaText) body).getText();
            }
            if (text.length() > 10) {
                return Collections.singletonList(query + " " + text);
            } else return Collections.emptyList();
        } else throw new UnsupportedOperationException("not known skel " + skel);
    }


    public static void main(String[] args) throws FileNotFoundException, CborException {
        System.setProperty("file.encoding", "UTF-8");
        final FileInputStream fileInputStream = new FileInputStream(new File(args[0]));

        for (Data.Page page : DeserializeData.iterableAnnotations(fileInputStream)) {
            String keyWordQuery = page.getPageName();
            String queryID = page.getPageId();

            System.out.println("performSearch");
            try {
                SearchEngine se = new SearchEngine();
                TopDocs topDocs = se.performSearch(keyWordQuery, 100);

                System.out.println("Results found: " + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

            } catch (Exception e) {
                e.printStackTrace();
            }


            List<String> result = new ArrayList<>();
            for (Data.PageSkeleton skel : page.getSkeleton()) {
                result.addAll(recurseArticle(skel, page.getPageName()));
            }

            for (String line : result) {
                System.out.println(line);
            }
        }


    }


}
