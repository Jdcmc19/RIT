package LuceneMagement;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import classes.Page;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));

        IndexWriterConfig indexConfig = new IndexWriterConfig(new StandardAnalyzer());
        indexConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //create the indexer
        writer = new IndexWriter(indexDirectory, indexConfig);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(Page page) throws IOException {
        Document document = new Document();
        FieldType fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);

        FieldType fieldType3 = new FieldType();
        fieldType3.setIndexOptions(IndexOptions.NONE);
        fieldType3.setStored(true);

        FieldType fieldType2 = new FieldType();
        fieldType2.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        fieldType2.setStored(true);
        //index file contents


        Field titleField = new Field(LuceneConstants.TITLE,page.getTitle(),fieldType2);

        Field bodyField = new Field(LuceneConstants.BODY,page.getBody(),fieldType);

        Field aField = new Field(LuceneConstants.A,page.getA(),fieldType);

        Field hField = new Field(LuceneConstants.H,page.getH(),fieldType);

        Field sField = new Field(LuceneConstants.byteInicio,Long.toString(page.getStart()),fieldType3);
        Field eField = new Field(LuceneConstants.byteTermina,Long.toString(page.getEnd()),fieldType3);

        document.add(titleField);
        document.add(bodyField);
        document.add(aField);
        document.add(hField);
        document.add(sField);
        document.add(eField);

        return document;
    }

    public void indexFile(Page page) throws IOException {
       // System.out.println("Indexing "+page.getTitle());
        Document document = getDocument(page);
        writer.addDocument(document);
    }
}
