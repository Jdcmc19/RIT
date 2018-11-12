package main;

import LuceneMagement.LuceneConstants;
import LuceneMagement.Searcher;
import LuceneMagement.Stemmer;
import classes.DataCharger;
import classes.Identifier;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.*;

public class Controller {
    String queryStops;
    String queryColletion;

    String stopwordspath;
    String toIndexPath;
    int currentPage=1;
    String query;
    String currentIndexPath;
    Stemmer stemmer = new Stemmer();
    DataCharger dataCharger = new DataCharger();
    Searcher searcher = new Searcher();
    Map<Button, Identifier> paginaActual = new HashMap<>();


    @FXML
    GridPane gridPane;
    @FXML
    Label lblPathIndex,lblDocF,lblDocIndex;
    @FXML
    ComboBox<String> cbType;
    @FXML
    Button btoIndex,btoQuery,btoDefIndex,btoNext,btoPrevious,btoStopwords,fcIndexar,fcStopwords,fcDefIndex;
    @FXML
    TextField txtIndex,txtQuery,txtDefIndex,txtStopwords;

    public void initialize(){
        cbType.getItems().addAll("texto","ref","encab","titulo");
        gridPane.setPrefWidth(405);
       for(int i=0;i<20;i++){
            gridPane.addRow(i);
       }


        for(int i=0;i<20;i++){
            gridPane.getRowConstraints().add(new RowConstraints(25));
            Button d = new Button();
            d.setPrefWidth(405);
            d.setStyle("-fx-background-color: #ccc;-fx-border-color: #bbb;");
            d.setOnAction(event -> {
                if(!d.getText().isEmpty()){
                    String settings = currentIndexPath+"\\configs.txt";
                    File file = new File(settings);
                    try {
                        String contents = new Scanner(file).useDelimiter("\\Z").next();
                        String[] pba = contents.split(",");
                        queryColletion=pba[0];
                        Identifier id = paginaActual.get(d);
                        File f = new File(queryColletion);
                        String page = new String(dataCharger.getFragment(f,id.getStart(),id.getEnd()-id.getStart()));
                        String path = currentIndexPath+"\\tmp.html";
                        FileWriter fw = new FileWriter(path);
                        BufferedWriter bw = new BufferedWriter(fw);

                        bw.write(page);
                        bw.close();
                        File pagina = new File(path);
                        URI url = pagina.toURI();

                        Desktop.getDesktop().browse(url);


                       /*

                        PrintWriter pw = new PrintWriter(fw);
                        pw.printf(page);
                        pw.close();

                       */




                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
            gridPane.add(d,0,i);
       }

        fcIndexar.setOnAction(event -> {
           FileChooser fileChooser = new FileChooser();
           fileChooser.setTitle("Seleccione el archivo de la colección.");
           File f = fileChooser.showOpenDialog(null);
           txtIndex.setText(f.getAbsolutePath());
        });
        fcStopwords.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione el archivo con los Stopwords.");
            File f = fileChooser.showOpenDialog(null);
            txtStopwords.setText(f.getAbsolutePath());
        });
        fcDefIndex.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);
            txtDefIndex.setText(selectedDirectory.getAbsolutePath());
        });



        btoIndex.setOnAction(event -> {
            toIndexPath = txtIndex.getText();
            if(!toIndexPath.isEmpty() && !stopwordspath.isEmpty()){
                try {
                    setIndex(toIndexPath,currentIndexPath);
                    FileWriter fw = new FileWriter(currentIndexPath+"\\configs.txt");
                    PrintWriter pw = new PrintWriter(fw);
                    pw.printf(toIndexPath+","+stopwordspath);
                    pw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //todo aqui
            }
        });
        btoStopwords.setOnAction(event -> {
            String t = txtStopwords.getText();
            if(!t.isEmpty())
                stopwordspath = t;
            else{
                //todo aqui
            }
        });
        btoQuery.setOnAction(event -> {
            query = txtQuery.getText();
            String where = cbType.getSelectionModel().getSelectedItem();
            if(where==null) where = LuceneConstants.BODY;

            if(!query.isEmpty()){
                try {
                    getConsulta(query,currentIndexPath,where);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else{
                //todo aqui
            }
        });
        btoNext.setOnAction(event -> {
            currentPage++;
            if(!setPage()){
               currentPage--;
               setPage();
            }
        });
        btoPrevious.setOnAction(event -> {
            currentPage--;
            if(currentPage<1){
                currentPage=1;
            }
            setPage();
        });
        btoDefIndex.setOnAction(event -> {
           String t = txtDefIndex.getText();
           if (!t.isEmpty()){
               currentIndexPath = t;
               txtDefIndex.setText("");
               lblPathIndex.setText(currentIndexPath);
           }else{
               //todo aqui
           }
       });

    }

    public Boolean setIndex(String pathCollection, String directoryPath) throws IOException {
        if(!stopwordspath.isEmpty()){
            long startTime = System.currentTimeMillis();
            dataCharger.setIndexer(directoryPath);
            dataCharger.getFiles(pathCollection, stopwordspath);
            long endTime = System.currentTimeMillis();
            getTime(endTime-startTime); //todo poner esto en un messagebox todo aqui
            lblDocIndex.setText(Long.toString(dataCharger.getCantPages()));
        }else{
            //todo aqui
        }

        return true;
    }
    public Boolean getConsulta(String query, String index, String where) throws IOException, ParseException {
        long startTime = System.currentTimeMillis();
        String settings = currentIndexPath+"\\configs.txt";
        File file = new File(settings);
        try {
            String c = new Scanner(file).useDelimiter("\\Z").next();
            String[] pba = c.split(",");
            queryStops=pba[1];

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        stemmer.setStopWords(queryStops);
        query = stemmer.stemmerToCons(query,where);
        System.out.println(query);
        searcher.consultar(query,index,where);

        long endTime = System.currentTimeMillis();
        getTime(endTime-startTime);//todo poner esto en un messagebox todo aqui
        System.out.println("Time :" + (endTime - startTime) +" ms");//todo aqui
        setPage();
        setHits();
        return true;
    }
    public boolean setPage(){
        try {
            paginaActual  =new HashMap<>();
            ArrayList<Document> doc = searcher.getPage(currentPage);

            ArrayList<Node> tops  = new ArrayList<>(gridPane.getChildren());
            for(int i=0;i<20;i++){
                Button b = (Button)tops.get(i);
                if(i<doc.size()){
                    b.setVisible(true);
                    Document pba = doc.get(i);
                    b.setText(pba.get(LuceneConstants.TITLE));
                    paginaActual.put(b,new Identifier(pba.get(LuceneConstants.byteInicio),pba.get(LuceneConstants.byteTermina)));
                }
                else{
                    b.setVisible(false);
                }
            }
            if(doc.size()<1) return false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void setHits(){
        lblDocF.setText(Integer.toString(searcher.getHits()));
    }
    public String getTime(long tiempo){
        String r;
        long horas=tiempo/3600000;
        long min=(tiempo%3600000)/60000;
        long seg=((tiempo%3600000)%60000)/1000;
        long mili=((tiempo%3600000)%60000)%1000;
        r = "Tardó aproximadamente "+horas+" hora(s) "+min+" minuto(s) "+seg+" segundo(s) "+mili+" milisegundo(s).";

        return r;

    }

}
