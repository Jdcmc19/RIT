package main;

import LuceneMagement.LuceneConstants;
import LuceneMagement.Searcher;
import LuceneMagement.Stemmer;
import classes.DataCharger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.xml.crypto.Data;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello Worlds");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        String stopwordspath = "C:\\Users\\iworth\\IdeaProjects\\RIT\\Utils\\Stopwords.txt";
        String index = "C:\\Users\\iworth\\iCloudDrive\\Desktop\\Index";
        String archivo = "C:\\Users\\iworth\\IdeaProjects\\RIT\\src\\Collection\\h8.txt";
        Stemmer stemmer = new Stemmer();
        stemmer.setStopWords(stopwordspath);
        String consulta = "Revision actual";

        DataCharger dc = new DataCharger();
        Searcher s = new Searcher();
        try {
            dc.setIndexer(index);
            dc.getFiles(archivo,stopwordspath);
            consulta = stemmer.stemmerToCons(consulta,false);
            System.out.println(consulta);
            s.consultar(consulta,index, LuceneConstants.A);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //launch(args);

    }
}
