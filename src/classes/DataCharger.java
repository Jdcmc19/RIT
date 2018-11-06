package classes;

import LuceneMagement.Indexer;
import LuceneMagement.Stemmer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class DataCharger {

    private Indexer indexer;

    public DataCharger(){}

    public void setIndexer(String path) throws IOException {
        indexer = new Indexer(path);
    }
    private byte[] getFragment(File f, long startByte, long chunkSize) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        raf.seek(startByte);
        int size = (int) Math.min(chunkSize, raf.length()-startByte);
        byte[] data = new byte[size];
        raf.read(data);
        raf.close();
        return data;
    }
    private String concatElements(Elements elements){

        String concatenado = "";
        for (Element sentence : elements)
            concatenado+=sentence.text()+" ";
        return concatenado;

    }

    public void getFiles(String path, String stopwordspath){
        long startTime = System.currentTimeMillis();
        Stemmer stemmer = new Stemmer();
        stemmer.setStopWords(stopwordspath);
        File f = new File(path); //TODO PREGUNTAR AL PROFE SI ESTA BIEN
        long fileBytes = f.length(); //Bytes del archivo
        Map<String, Identifier> coleccionID = new TreeMap<>(); // TITULO PAGINA --->> [ByteInicio, ByteFinal] de cada pagina
        int longXFragmento = 1000000; //TAMAÑO DE CADA FRAGMENTO/SEGMENTO
        int cantPages = 0; //PARA SABER CUANTAS PAGINAS HAY
        try {
            long endFragmento = longXFragmento; //DONDE TERMINARÁ EL FRAGMENTO
            long startFragmento = 0; //DONDE EMPIEZA EL FRAGMENTO
            boolean tienePagina; //SI EL FRAGMENTO ACTUAL, TIENE AL MENOS UNA PAGINA DENTRO
            boolean ultima=false; //PARA SABER SI ES LA ULTIMA PAGINA
            long pageStartByte = 0; //PARA CADA PAGINA SE ACTUALIZA, BYTE INICIO DE LA PAGINA
            long pageEndByte=0; //X2

            while(startFragmento<fileBytes && !ultima) {  //MIENTRAS QUE EL INICIO DEL FRAGMENTO SEA MENOR AL ARCHIVO -- ESTE ES EL WHILE DE LOS FRAGMENTOS
                if (fileBytes < endFragmento) { //PARA QUE DETECTE SI ES EL ULTIMO FRAGMENTO
                    endFragmento = fileBytes;
                    ultima = true;
                }
                byte[] frag = getFragment(f, startFragmento, endFragmento - startFragmento); //AGARRA EL FRAGMENTO CORRESPONDIENTE
                String fragmento = new String(frag, StandardCharsets.UTF_8); //PASA EL FRAGMENTO(BYTES) A STRING

                int inicia = 0; //INICIA EN 0 SIEMPRE, NO DEBERIA FALLAR - INDICE DE INICIO DE UNA PAGINA
                int termina = fragmento.indexOf("</html") + 7; //ENCUENTRA EL INDICE DEL FINAL DE LA PAGINA [+7 para abarcar </html>]
                if (inicia >= termina) //EN EL CUADERNO
                    termina = fragmento.indexOf("</html", inicia) + 7;
                int anterior = 0; //INDICE DE DONDE TERMINO LA PAGINA ANTERIOR
                tienePagina = false; //PARA SABER SI EL FRAGMENTO TIENE ALGUNA PAGINA [ENTRÓ AL WHILE O NO]
                while (termina != 6) { //ESTE ES EL WHILE DE LAS PAGINAS

                    String page = fragmento.substring(inicia, termina); //AGARRA DEL FRAGMENTO, UNA PAGINA PERO INCLUYE EL TEXTO DOCTYPE html PUBLIC
                    int indexEmpieza = page.indexOf("<html"); //DE page, DONDE VERDADERAMENTE INICIA LA PAGINA "<html>"
                    page = page.substring(indexEmpieza); //ACTUALIZA LA VARIABLE PARA DEJAR DESDE <html> HASTA </html>

                    Document doc = Jsoup.parse(page);//CREA EL HTML
                    pageStartByte = startFragmento + fragmento.substring(0, inicia + indexEmpieza).getBytes().length; //EL BYTE EXACTO DONDE INICIA LA PAGINA [CADA PAGE]
                    pageEndByte = startFragmento + fragmento.substring(0, termina).getBytes().length; //EL BYTE EXACTO DONDE TERMINA LA PAGINA [CADA PAGE]

                    Identifier idPage = new Identifier(pageStartByte, pageEndByte); //GUARDA EL BYTE INICIO Y FINAL DE CADA PAGINA
                    String title = doc.getElementsByTag("title").text();
                    if (title.isEmpty())
                        title = "SIN TITULO";
                    coleccionID.put(title + ", #" + ++cantPages, idPage); //METE EN EL DICCIONARIO EL TITULO ->> IDENTIFICADOR ^
                    Elements body = doc.select("body");
                    Elements a = doc.getElementsByTag("a");

                    Elements h = doc.select("h1,h2,h3,h4,h5,h6");
                    Page pagina = stemmer.stemmerToDoc(concatElements(a), concatElements(body), title + ", #" + cantPages, concatElements(h));
                    //Page pagina = new Page(concatElements(a), concatElements(body), title + ", #" + cantPages, concatElements(h));
                    //TODO AQUI LLAMAR LUCENE O LA FUNCION [llamarla de la clase Lucene]
                    indexer.indexFile(pagina);
                    //ACTUALIZA INDICES PARA LA PROXIMA PAGINA
                    anterior = termina; //GUARDA DONDE TERMINA LA PAGINA ANTERIOR
                    inicia = termina; //EMPIEZA DONDE TERMINO LA PAGINA ANTERIOR
                    termina = fragmento.indexOf("</html", anterior) + 7;  //TERMINA EN EL PROXIMO </html> QUE ENCUENTRE DESPUES DE LA PAGINA ANTERIOR
                    tienePagina = true; //SI LLEGO AQUI SIGNIFICA QUE SI HABIA UNA PAGINA

                }
                System.out.println(cantPages);
                if (tienePagina) { //SI EL FRAGMENTO ANTERIOR TIENE AL MENOS UNA PAGINA
                    int p = fragmento.substring(anterior).getBytes().length;
                    int qq = fragmento.getBytes().length;
                    startFragmento = startFragmento + (qq - p);
                    endFragmento = startFragmento + longXFragmento;
                } else { //SI EL FRAGMENTO ANTERIOR NO TIENE NINGUNA PAGINA, ALARGA EL FRAGMENTO [LO HACE MÁS GRANDE]
                    endFragmento += longXFragmento;
                }
            }
            indexer.close();
            long endTime = System.currentTimeMillis();
            System.out.println("TIEMPO DE INDEXACION : "+(endTime-startTime)+" ms");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public Indexer getIndexer() {
        return indexer;
    }
}
