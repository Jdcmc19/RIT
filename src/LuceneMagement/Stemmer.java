package LuceneMagement;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import classes.Page;
import org.apache.lucene.analysis.snowball.*;
import org.apache.lucene.analysis.es.*;
import org.tartarus.snowball.ext.SpanishStemmer;


public class Stemmer {
    //Patrón para dividir las palabras del documento
    private final String splitPattern = "[^A-Za-zÁÉÍÓÚÜáéíóúüÑñ]+";
    private HashSet<String> stopWordsSet;
    private Vector<String> stemmingBodyWords;
    private Vector<String> stemmingHWords ;
    private String pathStop;
    private static final SpanishStemmer spStemmer = new SpanishStemmer();

    //Metodo que retorna todas las stopwords en un hashSet


    public Stemmer() {
        stopWordsSet = new HashSet<>();
    }


    /*public static HashSet<String> getStopWordsSet() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Joseph Salas\\IdeaProjects\\RIT-master\\RIT\\Utils\\Stopwords.txt"));

    }*/
    public void setStopWords(String pathStop){
        this.pathStop = pathStop;
    }

    public HashSet<String> getStopWordsSet() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathStop));

        String line;
        while ((line = bufferedReader.readLine())!= null)
        {
            stopWordsSet.add(line);
        }
        return stopWordsSet;
    }

    public String stemmerToCons(String consulta, Boolean stopwords) throws IOException {
        Vector<String> stemmingQueryWords = new Vector<>();
        HashSet<String> stops = getStopWordsSet();
        String[] splitQueryLine = consulta.toLowerCase().split(splitPattern);
        if(stopwords){
            for (String word : splitQueryLine)
            {
                spStemmer.setCurrent(word);

                if(spStemmer.stem() && ! stops.contains(word))
                {
                    stemmingQueryWords.add(spStemmer.getCurrent());
                }
                consulta = stemmingWordsToString(stemmingQueryWords);
            }
        }
        return normaliceString(consulta);
    }

    public Page stemmerToDoc(String a, String body, String title, String h) throws IOException {
        stemmingBodyWords = new Vector<>();
        stemmingHWords = new Vector<>();
        HashSet<String> stopwords = getStopWordsSet();
        String[] splitBodyLine = body.toLowerCase().split(splitPattern);
        String[] splitHLine = h.toLowerCase().split(splitPattern);

        for (String word : splitBodyLine)
        {
            spStemmer.setCurrent(word);

            if(spStemmer.stem() && ! stopwords.contains(word))
            {
                stemmingBodyWords.add(spStemmer.getCurrent());
            }
        }

        for (String word : splitHLine)
        {
            spStemmer.setCurrent(word);

            if(spStemmer.stem() && ! stopwords.contains(word))
            {
                stemmingHWords.add(spStemmer.getCurrent());
            }
        }
        return new Page(normaliceString(a),stemmingWordsToString(stemmingBodyWords), normaliceString(title),
                stemmingWordsToString(stemmingHWords) );
    }


    public String normaliceString(String s )
    {
        String normaliceString = s.toLowerCase().replaceAll("á","a").replaceAll("é","e")
                .replaceAll("í","i").replaceAll("ó","o").replaceAll("ú","u");
        return normaliceString;
    }

    public String stemmingWordsToString(Vector stringBody) {
        String stemmingBodyWordsToString = "";
        for(int i = 0; i < stringBody.size(); i++) {
            stemmingBodyWordsToString +=  stringBody.get(i) + " ";
        }
        return stemmingBodyWordsToString;
    }

}
