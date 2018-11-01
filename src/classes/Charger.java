package classes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Charger {
    private static byte[] getFragment(File f, long startByte, long chunkSize) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        raf.seek(startByte);
        int size = (int) Math.min(chunkSize, raf.length()-startByte);
        byte[] data = new byte[size];
        raf.read(data);
        raf.close();
        return data;
    }

    public static void getFiles(String path){
        File f = new File(path);
        long fileBytes = f.length();
        Map<String, Identifier> coleccionID = new TreeMap<>();
        int longXFragmento = 100000;
        int cantPages = 0;
        try {
            long endFragmento = longXFragmento;
            long startFragmento = 0;
            boolean tienePagina;
            boolean ultima=false;
            long pba = 0;
            long pb2=0;

            while(startFragmento<fileBytes && !ultima){
                if(fileBytes<endFragmento){
                    endFragmento = fileBytes;
                    ultima = true;
                }
                byte[] frag = getFragment(f,startFragmento,endFragmento-startFragmento);
                String fragmento = new String(frag, StandardCharsets.UTF_8);

                int inicia = 0;
                int termina = fragmento.indexOf("</html")+7;
                if (inicia>=termina) termina = fragmento.indexOf("</html",inicia)+7;
                int anterior = 0;
                tienePagina = false;
                while(inicia!=-1 && termina!=6){
                    String page = fragmento.substring(inicia,termina);
                    int indexEmpieza = page.indexOf("<html");
                    page = page.substring(indexEmpieza);
                    Document doc = Jsoup.parse(page);
                    pba=startFragmento+fragmento.substring(0,inicia+indexEmpieza).getBytes().length;
                    pb2=startFragmento+fragmento.substring(0,termina).getBytes().length;
                    Identifier idPage = new Identifier(pba,pb2);
                    coleccionID.put(doc.getElementsByTag("title").toString() + ++cantPages,idPage);
                    anterior = termina;
                    inicia = termina;
                    termina = fragmento.indexOf("</html",anterior)+7;
                    tienePagina = true;

                }
                if(tienePagina) {
                    if (inicia != -1) {
                        int p = fragmento.substring(inicia).getBytes().length;
                        int qq = fragmento.getBytes().length;
                        startFragmento = startFragmento + (qq - p);
                    } else {
                        int p = fragmento.substring(anterior).getBytes().length;
                        int qq = fragmento.getBytes().length;
                        startFragmento = startFragmento + (qq - p);
                    }
                    System.out.println(132);
                    endFragmento = startFragmento + longXFragmento;
                    System.out.println("siugue el frag: start: " + startFragmento + " endL: " + endFragmento);
                }else{
                    endFragmento+=longXFragmento;
                }
            }
            System.out.println("puta: "+pba+ " pb2="+(pb2-pba));
            System.out.println("pb2: "+pb2);
            byte[] frag = getFragment(f,pba,pb2-pba);
            String prueba = new String(frag, StandardCharsets.UTF_8);
            System.out.println("***********************************");
            //System.out.println(prueba);
            int cont=0;
            for(String title : coleccionID.keySet()){
                System.out.println(++cont+" "+title);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
