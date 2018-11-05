package IndexLucene;

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
    private static final String splitPattern = "[^A-Za-zÁÉÍÓÚÜáéíóúüÑñ]+";
    private static HashSet<String> stopWordsSet = new HashSet<String>();
    private static Vector<String> stemmingBodyWords;
    private static Vector<String> stemmingHWords ;
    private static final SpanishStemmer spStemmer = new SpanishStemmer();

    //Metodo que retorna todas las stopwords en un hashSet



    public static HashSet<String> getStopWordsSet() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Utils/Stopwords.txt"));
        String line;
        while ((line = bufferedReader.readLine())!= null)
        {
            stopWordsSet.add(line);
        }
        return stopWordsSet;
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
        return new Page(normaliceATag(a),stemmingBodyWordsToString(stemmingBodyWords), normaliceTitleTag(title),
                stemmingHWordsToString(stemmingHWords) );
    }


    public String normaliceATag(String a )
    {
        String normaliceA = a.toLowerCase().replaceAll("á","a").replaceAll("é","e")
                .replaceAll("í","i").replaceAll("ó","o").replaceAll("ú","u");
        return normaliceA;
    }

    public String normaliceTitleTag(String title) {
        String normaliceTitle = title.toLowerCase().replaceAll("á", "a").replaceAll("é", "e")
                .replaceAll("í", "i").replaceAll("ó", "o").replaceAll("ú", "u");
        return normaliceTitle;
    }

    public String stemmingBodyWordsToString(Vector stringBody) {
        String stemmingBodyWordsToString = "";
        for(int i = 0; i < stringBody.size(); i++) {
            stemmingBodyWordsToString +=  stringBody.get(i) + " ";
        }
        return stemmingBodyWordsToString;
    }

    public String stemmingHWordsToString(Vector stringH) {
        String stemmingHWordsToString = "";
        for(int i = 0; i < stringH.size(); i++) {
            stemmingHWordsToString +=  stringH.get(i) + " ";
        }
        return stemmingHWordsToString;
    }
}
