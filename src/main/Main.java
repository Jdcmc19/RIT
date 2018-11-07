package main;

import LuceneMagement.LuceneConstants;
import LuceneMagement.LuceneIndexer;
import LuceneMagement.Searcher;
import IndexLucene.Stemmer;
import classes.Charger;
import classes.Identifier;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class Main extends Application {
    static String indexDir = "C:\\Users\\Joseph Salas\\Desktop\\Indice";
    static Searcher searcher;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello Worlds");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
    private static void search(String searchQuery) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime) +" ms");
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: "+ doc.get(LuceneConstants.TITLE));
        }
    }
    public static String getConsulta(int modo, String consulta){
        String resultad="";
        switch (modo){
            case 1:break;
            case 2: break;
        }
        return resultad;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {



            Charger charger = new Charger();
            charger.getFiles("C:\\Users\\Joseph Salas\\IdeaProjects\\RIT-master\\RIT\\src\\Collection\\h6.txt");

            LuceneIndexer index = charger.getIndexer();


        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("TIEMPO DE INDEXACION : "+(endTime-startTime)+" ms");
        try {
            search("yorleny");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //launch(args);

    }
}
