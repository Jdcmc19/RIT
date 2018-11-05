package LuceneMagement;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
public class Searcher {
    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;

    public Searcher(String indexDirectoryPath) throws IOException {
        IndexReader indexDirectory =
                DirectoryReader.open(FSDirectory.open(new File(indexDirectoryPath).toPath()));

        indexSearcher = new IndexSearcher(indexDirectory);

        Analyzer analyzer = new StandardAnalyzer();
        queryParser = new QueryParser(LuceneConstants.TITLE,analyzer);//TODO VER SI EL PROFE ELIGE DONDE QUIERE CONSULTAR
    }
    public TopDocs search( String searchQuery) throws IOException, ParseException {
        query = queryParser.parse(searchQuery);
        return indexSearcher.search(query, LuceneConstants.TOP);
    }

    public Document getDocument(ScoreDoc scoreDoc)
            throws CorruptIndexException, IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

}
