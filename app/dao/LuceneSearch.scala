package dao

import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig, DirectoryReader}
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.search.{Query, IndexSearcher}
import org.apache.lucene.queryparser.classic.QueryParser

import models.MyTables.Task
import models.MyTables.TasksState

object LuceneSearch {
  
  val directory = new RAMDirectory();
  val analyzer = new RussianAnalyzer() 
  var init = false;
  
  def initDocs(tasksState: Seq[TasksState]) = {
    if(!init)
      for(ts <- tasksState; t <- ts.tasks) {
        indexDocs(t.title, t.descr)
      }
    
    init = true
  }
  
  def indexDocs(title: String, descr: String) = {       
     val indexWriterConfig = new IndexWriterConfig(analyzer)
     val indexWriter = new IndexWriter(directory, indexWriterConfig)
     val d = new Document()
     d.add(new Field("title", title, TextField.TYPE_STORED)) 
     d.add(new Field("description", descr, TextField.TYPE_STORED))  
     indexWriter.addDocument(d)
     indexWriter.commit()
     indexWriter.close()      
  }
  
  
  def searchDescr(phrase: String): Seq[Task] = {

    val ireader = DirectoryReader.open(directory);
    val isearcher = new IndexSearcher(ireader);
    val parser = new QueryParser("description", analyzer)
    val query = parser.parse(phrase)
    val res = isearcher.search(query, 10).scoreDocs.toSeq;
    val tasks = for(r <- res) yield {
                  val d = isearcher.doc(r.doc)
                  Task(None, d.get("title"), d.get("description"), None)
                }
    ireader.close()
    
    tasks
  }
  
    
}