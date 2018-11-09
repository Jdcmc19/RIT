package main;

import LuceneMagement.Searcher;
import LuceneMagement.Stemmer;
import classes.DataCharger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Controller {
    String stopwordspath="";
    Stemmer stemmer = new Stemmer();
    DataCharger dataCharger = new DataCharger();
    Searcher searcher = new Searcher();

    @FXML
    GridPane grid;


    public void initialize(){

        Button a = new Button("hola");
        grid.add(a,0,3);

    }

    public Boolean setIndex(String pathCollection, String directoryPath) throws IOException {
        dataCharger.setIndexer(directoryPath);
        dataCharger.getFiles(pathCollection, stopwordspath);

        return true;
    }
    public Boolean getConsulta(String query, boolean stopwordsp, String index, String where) throws IOException, ParseException {
        query = stemmer.stemmerToCons(query,stopwordsp);
        stemmer.setStopWords(stopwordspath);
        searcher.consultar(query,index,where);

        return true;
    }

}
