package controllers

import javax.inject.{Inject, Singleton}
import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

import scala.concurrent.ExecutionContext

import models.MyTables._
import dao.TablesDAO
import dao.LuceneSearch.indexDocs
import dao.LuceneSearch.searchDescr
import dao.LuceneSearch.initDocs
 

@Singleton
class HomeController @Inject()(tablesDAO: TablesDAO, cc: ControllerComponents)(implicit executionContext: ExecutionContext) 
                                                                                            extends AbstractController(cc) {
                                                                                            
  implicit val stateWrites  = Json.writes[State]
  implicit val taskDeskWrites  = Json.writes[Task]
  implicit val taskDeskStatesWrites  = Json.writes[TasksState]
  // список задач
  def getTaskDesk = Action.async { implicit request: Request[AnyContent] =>    
    tablesDAO.allTasksState().map{ tds =>
      initDocs(tds)
      Ok(Json.toJson(tds)) 
    }  
  }
  
  // поиск задач
  def searchTasks(phrase: String) = Action{ implicit request: Request[AnyContent] =>
    val tasks = searchDescr(phrase)
    Ok(Json.toJson(tasks))   
  }  
    
  implicit val taskReader = Json.reads[Task]
  // создание задачи
  def createTask = Action.async(BodyParsers.parse.json[Task]){ implicit request =>
    val task: Task = request.body
    indexDocs(task.title, task.descr)
    tablesDAO.createTask(task).map{ _ =>
      Ok(Json.obj("success" -> true))
    }
  }
  
  def updateTask(id: Int) = Action.async(BodyParsers.parse.json[Task]){ implicit request =>
    val task: Task = request.body
    indexDocs(task.title, task.descr)
    tablesDAO.updateTask(id, task.title, task.descr).map{ _ =>
      Ok(Json.obj("success" -> true))
    }
  }
  
  def updateTaskState(task_id: Int, state_id: Int) = Action.async { implicit request: Request[AnyContent] =>
    tablesDAO.changeTaskState(task_id, state_id).map {_ =>
      Ok(Json.obj("success" -> true))
    }
  }
  
}
