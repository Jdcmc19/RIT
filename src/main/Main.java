package main;

import classes.Identifier;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static byte[] readData(File f, long startByte, long chunkSize) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        raf.seek(startByte);
        int size = (int) Math.min(chunkSize, raf.length()-startByte);
        byte[] data = new byte[size];
        raf.read(data);
        // TODO check the value returned by read (throw Exception or loop)
        raf.close();
        return data;
    }
    public static void main(String[] args) {
        //launch(args);
        File f = new File("archivos/h8.txt");
        long bytes = f.length();
        Map<String, Identifier> coleccionID = new TreeMap<>();
        int longFrag = 100000;
        int cantPages = 0;
        try {
            long end = longFrag;
            long start = 0;
            long acumulado = 0;
            boolean entra;
            boolean ultima=false;
            int frags=0;

            while(start<bytes && !ultima){
                if(bytes<end){
                    end = bytes;
                    ultima = true;
                }
                byte[] frag = readData(f,start,end-start);
                String fragmento = new String(frag, StandardCharsets.UTF_8);

                int inicia = 0;//fragmento.indexOf("<html");
                int termina = fragmento.indexOf("</html")+7;
                if (inicia>=termina) termina = fragmento.indexOf("</html",inicia)+7;
                int anterior = 0;
                System.out.println("FRAGMENTO: "+start+" end: "+end + " frag: "+fragmento.length());
                entra = false;
                while(inicia!=-1 && termina!=6){
                    System.out.println("Haspage");

                    System.out.println("fragmeto: "+fragmento.length()+" Inicia: " + inicia+" termina: "+termina);
                    String page = fragmento.substring(inicia,termina);
                    page = page.substring(page.indexOf("<html"));
                    Document doc = Jsoup.parse(page);
                    if(cantPages==0)System.out.println(doc.getElementsByTag("title"));
                    System.out.println("*******************************************************PAGINA*****************************************************");
                    System.out.println(++cantPages + " CANT PAGES");
                    anterior = termina;
                    inicia = termina;// fragmento.indexOf("<html",anterior);
                    termina = fragmento.indexOf("</html",anterior)+7;
                    entra = true;

                }
                if(entra == true) {
                    System.out.println(++frags+  "    FRAGMENTOSSSSSS");
                    System.out.println("Termino el frag start: " + start + " endL: " + end);
                    if (inicia != -1) {
                        int p = fragmento.substring(inicia).getBytes().length;
                        int qq = fragmento.getBytes().length;
                        start = start + (qq - p);
                        System.out.println("ASDASDASDAhere + " + inicia + "p " + p + " qq " + qq + " acuymu " + acumulado);
                    } else {
                        int p = fragmento.substring(anterior).getBytes().length;
                        int qq = fragmento.getBytes().length;
                        start = start + (qq - p);
                        System.out.println(termina + " termina start " + inicia);
                        //start = fragmento.substring(0, anterior).getBytes().length + acumulado;
                        System.out.println("not + " + anterior);
                    }
                    System.out.println(132);
                    acumulado = end;
                    end = start + longFrag;
                    System.out.println("siugue el frag: start: " + start + " endL: " + end);
                }else{
                    end+=longFrag;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
