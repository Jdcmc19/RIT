package main;

import IndexLucene.Stemmer;
import classes.Charger;
import classes.Identifier;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello Worlds");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        Charger.getFiles("C:\\Users\\Joseph Salas\\IdeaProjects\\RIT-master\\RIT\\src\\Collection\\h10.txt");
        //launch(args);
        /*HashSet<String> prueba =  Stemmer.getStopWordsSet();
        for(String s : prueba)
        {
            System.out.println(s);
        }*/

    }
}
