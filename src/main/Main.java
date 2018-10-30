package main;

import classes.Identifier;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public static byte[] readData(File f, int startByte, int chunkSize) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        raf.seek(startByte);
        int size = (int) Math.min(chunkSize, raf.length()-startByte);
        byte[] data = new byte[size];
        raf.read(data);
        // TODO check the value returned by read (throw Exception or loop)
        raf.close();
        return data;
    }
    public static boolean hasPage(String fragmento){
        return (fragmento.contains("<html") && fragmento.contains("</html"));
    }
    public static void main(String[] args) {
        //launch(args);
        File f = new File("archivos/h9.txt");
        long bytes = f.length();
        Map<String, Identifier> coleccionID = new TreeMap<>();
        int longFrag = 100000;
        int cantPages = 0;
        try {
            int end = longFrag;
            int start = 0;

            while(start<bytes){
                if(bytes<end) end = (int)bytes;
                byte[] frag = readData(f,start,end-start);
                String fragmento = new String(frag, StandardCharsets.UTF_8);

                int inicia = fragmento.indexOf("<html");
                int termina = fragmento.indexOf("</html")+7;
                int anterior = 0;
                System.out.println("FRAGMENTO: "+start+" end: "+end + " frag: "+fragmento.length());
                System.out.println(fragmento);
                while(inicia!=-1 && termina!=6){
                    System.out.println("Haspage");

                    System.out.println("fragmeto: "+fragmento.length()+" Inicia: " + inicia+" termina: "+termina);
                    String page = fragmento.substring(inicia,termina);

                    System.out.println("*******************************************************PAGINA*****************************************************");
                    System.out.println(++cantPages + " CANT PAGES");
                    anterior = termina;
                    inicia = fragmento.indexOf("<html",anterior);
                    termina = fragmento.indexOf("</html",anterior)+7;

                }
                if(inicia!=-1){
                    int p = fragmento.substring(inicia).getBytes().length;
                    start = fragmento.getBytes().length-p;
                    System.out.println("ASDASDASDAhere + "+inicia);
                }
                else{
                    start = fragmento.substring(0,anterior).getBytes().length;
                    System.out.println("not + "+anterior );
                }
                System.out.println(132);
                end = start + longFrag;
                System.out.println("Termino frag: start: "+start+" endL: "+end);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        /*System.out.println(f.length());
        Scanner scan = null;
        try {
            scan = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scan.useDelimiter("\\Z");
        String content = scan.next();
        System.out.println(content.length());*/

    }
}
