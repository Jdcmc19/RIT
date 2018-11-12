package LuceneMagement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
public class Searcher {
    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;
    TopDocs hits;

    public Searcher(){
    }
    public TopDocs search( String searchQuery) throws IOException, ParseException {
        query = queryParser.parse(searchQuery);
        return indexSearcher.search(query,1000, Sort.RELEVANCE);
    }

    public Document getDocument(ScoreDoc scoreDoc)
            throws CorruptIndexException, IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }
    public void setAll(String indexDirectoryPath,String where) throws IOException {
        IndexReader indexDirectory =
                DirectoryReader.open(FSDirectory.open(new File(indexDirectoryPath).toPath()));

        indexSearcher = new IndexSearcher(indexDirectory);

        Analyzer analyzer = new StandardAnalyzer();
        queryParser = new QueryParser(where,analyzer);
    }

    public void consultar(String searchQuery,String indexDir,String where) throws IOException, ParseException {
        setAll(indexDir,where);
        long startTime = System.currentTimeMillis();
        hits = search(searchQuery);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime) +" ms");
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = getDocument(scoreDoc);
            long start = Long.parseLong(doc.get(LuceneConstants.byteInicio));
            long end = Long.parseLong(doc.get(LuceneConstants.byteTermina));
            System.out.println("File: "+ doc.get(LuceneConstants.TITLE) + " " + start + " " + end);
        }
    }
    public ArrayList<Document> getPage(int page) throws IOException {
        ArrayList<Document> docs = new ArrayList<>();
        for(int i=0;i<20;i++){
            if(hits.totalHits>(i+(20*(page-1)))){
                ScoreDoc sd = hits.scoreDocs[i+(20*(page-1))];
                Document doc = getDocument(sd);
                docs.add(doc);
            }
        }
        return docs;

    }

    public int getHits() {
        return hits.scoreDocs.length;
    }
}
