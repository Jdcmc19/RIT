package LuceneMagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import classes.Page;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;



public class Stemmer {
    //Patrón para dividir las palabras del documento
    private List<String> stopWordsSet;
    private String pathStop;

    //Metodo que retorna todas las stopwords en un hashSet


    //SE INICIALIZA LA LISTA DE STOPWORDS VACIA ------------------------------------
    public Stemmer() {
        stopWordsSet = new ArrayList<String>();
    }

    //SETEA EL PATH DE LOS STOPWORDS--------------------------------------------------
    public void setStopWords(String pathStop){
        this.pathStop = pathStop;
    }

    //RETORNA LISTA DE STRINGS CON TODOS LOS STOPWORDS---------------------------------
    public List<String> getStopWordsSet() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathStop));

        String line;
        while ((line = bufferedReader.readLine())!= null)
        {
            stopWordsSet.add(line);
        }
        return stopWordsSet;
    }



    //APLICA STEMMING A CONSULTA---------------------------------------------------------
    public String stemmerToCons(String consulta, Boolean stopwords) throws IOException {
       return tokenizeStopStem(consulta);
    }

    //APLICA STEMMING A CADA PAGINA-------------------------------------------------------
    public Page stemmerToDoc(String a, String body, String title, String h) throws IOException {
        return new Page(normaliceString(a),tokenizeStopStem(body), normaliceString(title),
                tokenizeStopStem(h) );
    }


    //NORMALIZA EL TITLE Y LA ETIQUETA A-------------------------------------------------------------------
    public String normaliceString(String s )
    {
        String normaliceString = s.toLowerCase().replaceAll("á","a").replaceAll("é","e")
                .replaceAll("í","i").replaceAll("ó","o").replaceAll("ú","u");
        return normaliceString;
    }



    //METODO QUE QUITA LOS STOPWORDS, CAMBIA MAYUSCULAS, APLICA PORTER Y DEVUELE EL STRING CON EL STEMMING
    public String tokenizeStopStem(String input) throws IOException {
        TokenStream tokenStream = new StandardTokenizer();
        ((StandardTokenizer) tokenStream).setReader(new StringReader(input));
        tokenStream.reset();
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, StopFilter.makeStopSet(stopWordsSet));
        tokenStream = new SnowballFilter(tokenStream, "Spanish");
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        StringBuilder sb = new StringBuilder();
        while (tokenStream.incrementToken())
        {
            sb.append(attribute.toString());
            sb.append(" ");
        }

        System.out.println(sb);
      return sb.toString();

    }

}
