package main;

import LuceneMagement.Searcher;
import LuceneMagement.Stemmer;
import classes.DataCharger;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Controller {
    String stopwordspath="";
    Stemmer stemmer = new Stemmer();
    DataCharger dataCharger = new DataCharger();
    Searcher searcher = new Searcher();



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
